package com.example.xhotel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditItemActivity extends AppCompatActivity {

    private EditText editTextDescription, editTextPrice, editTextRooms, editTextPeople, editTextBeds;
    private Button btnSaveChanges;
    private DatabaseReference databaseReference;
    private String itemKey; // Тухайн зүйлийн түлхүүр

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Firebase reference авах
        databaseReference = FirebaseDatabase.getInstance().getReference("texts");

        // Intent-ээс өгөгдлийг авах
        itemKey = getIntent().getStringExtra("itemKey");
        String description = getIntent().getStringExtra("text");
        String price = getIntent().getStringExtra("price");
        String rooms = getIntent().getStringExtra("numberOfRooms");
        String people = getIntent().getStringExtra("numberOfPeople");
        String beds = getIntent().getStringExtra("numberOfBeds");

        // Талбаруудыг харах
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextRooms = findViewById(R.id.editTextRooms);
        editTextPeople = findViewById(R.id.editTextPeople);
        editTextBeds = findViewById(R.id.editTextBeds);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // Intent-ээр ирсэн өгөгдлийг тохируулах
        editTextDescription.setText(description);
        editTextPrice.setText(price);
        editTextRooms.setText(rooms);
        editTextPeople.setText(people);
        editTextBeds.setText(beds);

        // Хадгалах товч дээр дарсан үед
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges(); // Өгөгдлийг шинэчлэх
            }
        });
    }

    private void saveChanges() {
        // Шинэчилсэн утгыг авах
        String updatedDescription = editTextDescription.getText().toString();
        String updatedPrice = editTextPrice.getText().toString();
        String updatedRooms = editTextRooms.getText().toString();
        String updatedPeople = editTextPeople.getText().toString();
        String updatedBeds = editTextBeds.getText().toString();

        // Firebase-д шинэчилсэн утгуудыг хадгалах
        if (itemKey != null) {
            databaseReference.child(itemKey).child("text").setValue(updatedDescription);
            databaseReference.child(itemKey).child("price").setValue(updatedPrice);
            databaseReference.child(itemKey).child("numberOfRooms").setValue(updatedRooms);
            databaseReference.child(itemKey).child("numberOfPeople").setValue(updatedPeople);
            databaseReference.child(itemKey).child("numberOfBeds").setValue(updatedBeds)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditItemActivity.this, "Өөрчлөлт амжилттай хадгалагдлаа", Toast.LENGTH_SHORT).show();
                            finish(); // Хуудас хаах
                        } else {
                            Toast.makeText(EditItemActivity.this, "Алдаа гарлаа", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Алдаа: Зүйлийн түлхүүр байхгүй", Toast.LENGTH_SHORT).show();
        }
    }
}