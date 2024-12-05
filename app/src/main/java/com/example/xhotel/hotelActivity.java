package com.example.xhotel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class hotelActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private StorageReference storageReference; // Firebase Storage-ийн эх үүсвэр
    private LinearLayout textLayout;
    private static final int PICK_IMAGE_REQUEST = 1; // Зураг сонгох код
    private Uri imageUri; // Сонгосон зургийн URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        // Firebase Storage болон Database-ийн эх үүсвэрийг инициализацлана
        storageReference = FirebaseStorage.getInstance().getReference("images");
        databaseReference = FirebaseDatabase.getInstance().getReference("texts");
        textLayout = findViewById(R.id.textLayout);

        // Өгөгдлийг ачаалах
        loadTexts();
    }

    private void loadTexts() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textLayout.removeAllViews(); // Өмнөх мэдээллийг цэвэрлэх

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Мэдээллийг TextEntry объект руу унших
                    TextEntry textEntry = snapshot.getValue(TextEntry.class);
                    if (textEntry != null) {
                        displayTextEntry(textEntry, snapshot.getKey()); // Тус бүрийн текст мэдээллийг харуулах
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(hotelActivity.this, "Өгөгдөл ачаалахад алдаа гарлаа", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTextEntry(TextEntry textEntry, String key) {
        // Текст мэдээллийг харуулах TextView үүсгэх
        TextView textView = new TextView(hotelActivity.this);
        textView.setText("Текст: " + textEntry.getText() + "\n" +
                "Үнэ: " + textEntry.getPrice() + "\n" +
                "Өрөөний тоо: " + textEntry.getNumberOfRooms() + "\n" +
                "Хүний тоо: " + textEntry.getNumberOfPeople() + "\n" +
                "Орны тоо: " + textEntry.getNumberOfBeds());
        textView.setPadding(16, 16, 16, 16);

        // Зураг харуулах ImageView үүсгэх
        ImageView imageView = new ImageView(hotelActivity.this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 500)); // Зургийн өндөр
        imageView.setPadding(16, 16, 16, 16);

        // Glide ашиглан зургийг ачаалах
        Glide.with(hotelActivity.this)
                .load(textEntry.getImageUrl())  // Firebase-ээс зураг URL ачаалах
                .into(imageView);

        // Засах товч
        Button editButton = new Button(hotelActivity.this);
        editButton.setText("Засах");
        editButton.setOnClickListener(v -> showEditDialog(textEntry, key)); // Засах үйлдлийг дуудна

        // Устгах товч
        Button deleteButton = new Button(hotelActivity.this);
        deleteButton.setText("Устгах");
        deleteButton.setOnClickListener(v -> deleteTextEntry(key)); // Устгах үйлдлийг дуудна

        // Layout-д TextView, ImageView, Button-уудыг нэмэх
        textLayout.addView(imageView);
        textLayout.addView(textView);
        textLayout.addView(editButton);
        textLayout.addView(deleteButton);
    }

    private void showEditDialog(TextEntry textEntry, String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(hotelActivity.this);
        builder.setTitle("Өрөөний мэдээллийг засах");

        // TextEntry талбарууд
        final EditText editText = new EditText(hotelActivity.this);
        editText.setText(textEntry.getText());

        final EditText editPrice = new EditText(hotelActivity.this);
        editPrice.setText(textEntry.getPrice());

        final EditText editRooms = new EditText(hotelActivity.this);
        editRooms.setText(textEntry.getNumberOfRooms());

        final EditText editPeople = new EditText(hotelActivity.this);
        editPeople.setText(textEntry.getNumberOfPeople());

        final EditText editBeds = new EditText(hotelActivity.this);
        editBeds.setText(textEntry.getNumberOfBeds());

        // Одоо байгаа зургийг харуулах ImageView үүсгэх
        final ImageView imageView = new ImageView(hotelActivity.this);
        Glide.with(hotelActivity.this).load(textEntry.getImageUrl()).into(imageView);

        // Зураг сонгох товч
        Button selectImageButton = new Button(hotelActivity.this);
        selectImageButton.setText("Зураг сонгох");
        selectImageButton.setOnClickListener(v -> openImageChooser()); // Зураг сонгох үйлдлийг дуудах

        // Layout-т талбаруудыг нэмэх
        LinearLayout layout = new LinearLayout(hotelActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(editPrice);
        layout.addView(editRooms);
        layout.addView(editPeople);
        layout.addView(editBeds);
        layout.addView(imageView);
        layout.addView(selectImageButton);

        builder.setView(layout);

        // "Хадгалах" товч дарж зураг болон текст мэдээллийг шинэчлэх
        builder.setPositiveButton("Хадгалах", (dialog, which) -> {
            textEntry.setText(editText.getText().toString());
            textEntry.setPrice(editPrice.getText().toString());
            textEntry.setNumberOfRooms(editRooms.getText().toString());
            textEntry.setNumberOfPeople(editPeople.getText().toString());
            textEntry.setNumberOfBeds(editBeds.getText().toString());

            if (imageUri != null) { // Хэрэв шинэ зураг сонгосон бол
                StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
                fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            textEntry.setImageUrl(uri.toString());
                            updateTextEntry(key, textEntry); // Firebase-д шинэ мэдээллийг хадгалах
                        })
                ).addOnFailureListener(e -> Toast.makeText(hotelActivity.this, "Зураг байршуулахад алдаа гарлаа", Toast.LENGTH_SHORT).show());
            } else {
                updateTextEntry(key, textEntry); // Зураг солигдоогүй бол шууд хадгалах
            }
        });

        builder.setNegativeButton("Болих", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Зураг сонгох үйлдлийг нээх
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Сонгосон зургийн URI-г авна
            Toast.makeText(hotelActivity.this, "Зураг сонгогдлоо", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTextEntry(String key, TextEntry textEntry) {
        databaseReference.child(key).setValue(textEntry)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(hotelActivity.this, "Текст амжилттай шинэчлэгдлээ", Toast.LENGTH_SHORT).show();
                        loadTexts();  // Өгөгдлийг дахин ачаалах
                    } else {
                        Toast.makeText(hotelActivity.this, "Шинэчлэхэд алдаа гарлаа", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteTextEntry(String key) {
        databaseReference.child(key).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(hotelActivity.this, "Текст амжилттай устгагдлаа", Toast.LENGTH_SHORT).show();
                        loadTexts(); // Өгөгдлийг дахин ачаалах
                    } else {
                        Toast.makeText(hotelActivity.this, "Устгахад алдаа гарлаа", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}