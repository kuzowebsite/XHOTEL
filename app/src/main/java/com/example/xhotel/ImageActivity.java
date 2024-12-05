package com.example.xhotel;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ImageActivity extends AppCompatActivity {

    private static final String TAG = "ImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("images/1730824883588.jpg"); // Файлын зам
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("images/image1"); // Realtime Database зам

        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            String downloadUrl = uri.toString();
            Log.d(TAG, "Download URL: " + downloadUrl);

            databaseReference.child("imageUrl").setValue(downloadUrl)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Download URL амжилттай хадгалагдлаа"))
                    .addOnFailureListener(e -> Log.e(TAG, "Download URL хадгалахад алдаа гарлаа", e));
        }).addOnFailureListener(e -> Log.e(TAG, "Download URL авахад алдаа гарлаа", e));
    }
}