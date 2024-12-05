package com.example.xhotel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    private EditText editText, priceEditText, numberOfRoomsEditText, numberOfPeopleEditText, numberOfBedsEditText;
    private ImageView imageView;
    private Button uploadButton, viewHotelButton, viewOrdersButton, viewAdminButton, usedButton, loginButton;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Firebase тохиргоо
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference("texts");

        // Views-ийг олох
        editText = findViewById(R.id.editText);
        priceEditText = findViewById(R.id.priceEditText);
        numberOfRoomsEditText = findViewById(R.id.numberOfRoomsEditText);
        numberOfPeopleEditText = findViewById(R.id.numberOfPeopleEditText);
        numberOfBedsEditText = findViewById(R.id.numberOfBedsEditText);
        imageView = findViewById(R.id.imageView);
        uploadButton = findViewById(R.id.uploadButton);
        viewHotelButton = findViewById(R.id.viewHotelButton);
        viewOrdersButton = findViewById(R.id.viewOrdersButton);
        viewAdminButton = findViewById(R.id.viewAdminButton);
        usedButton = findViewById(R.id.usedButton);
        loginButton = findViewById(R.id.loginButton);

        // Зураг сонгох
        imageView.setOnClickListener(v -> openFileChooser());

        // Үзүүлэлтүүдийг хадгалах
        uploadButton.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadImage();
            } else {
                Toast.makeText(MainActivity.this, "Зураг сонгоно уу!", Toast.LENGTH_SHORT).show();
            }
        });

        viewHotelButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, hotelActivity.class)));

        viewOrdersButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, OrderActivity.class)));

        viewAdminButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AdminActivity.class)));

        usedButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ConfirmedActivity.class)));

        loginButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
    }

    // Файл сонгох
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Зураг сонгох"), PICK_IMAGE_REQUEST);
    }

    // Сонгосон зургийг боловсруулах
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri); // Зураг дэлгэц дээр үзүүлэх
        }
    }

    // Зургийг Firebase Storage руу ачаалах
    private void uploadImage() {
        StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    saveText(editText.getText().toString(), downloadUrl);
                }))
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Ачлахад алдаа гарлаа: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Мэдээллийг Firebase Realtime Database-д хадгалах
    private void saveText(String text, String imageUrl) {
        String price = priceEditText.getText().toString();
        String numberOfRooms = numberOfRoomsEditText.getText().toString();
        String numberOfPeople = numberOfPeopleEditText.getText().toString();
        String numberOfBeds = numberOfBedsEditText.getText().toString();

        String id = databaseRef.push().getKey();
        TextModel textModel = new TextModel(text, imageUrl, price, numberOfRooms, numberOfPeople, numberOfBeds);

        databaseRef.child(id).setValue(textModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Амжилттай хадгалагдлаа!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Хадгалалт амжилтгүй боллоо!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}