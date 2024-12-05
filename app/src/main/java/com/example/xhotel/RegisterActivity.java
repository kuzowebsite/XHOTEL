package com.example.xhotel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;  // Бүртгэх товч
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // EditText болон Button элемэнтүүдийг холбох
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        // Firebase Realtime Database холболт
        databaseReference = FirebaseDatabase.getInstance().getReference("users"); // admins -> users

        // Бүртгэх товчийн үйлдэл
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Хэрэглэгчийн нэр болон нууц үгийг Firebase дээр хадгалах
                if (!username.isEmpty() && !password.isEmpty()) {
                    registerUser(username, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "Бүх талбарыг бөглөнө үү", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Firebase дээр хэрэглэгчийн мэдээллийг хадгалах
    private void registerUser(String username, String password) {
        // Хэрэглэгчийн нэр болон нууц үгийг Firebase дээр хадгална
        User user = new User(username, password);

        // Firebase дээр хадгалах
        databaseReference.child(username).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Бүртгэл амжилттай", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Бүртгэлд алдаа гарлаа", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}