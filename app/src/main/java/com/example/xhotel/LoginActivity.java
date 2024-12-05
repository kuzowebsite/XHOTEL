package com.example.xhotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private Button adminButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Firebase Realtime Database - users
        databaseReference = FirebaseDatabase.getInstance().getReference();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                loginWithFirebase(username, password);
            }
        });

        // Register товчны үйлдэл
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loginWithFirebase(String username, String password) {
        // Check first in "admins" node
        databaseReference.child("admins").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String firebasePassword = dataSnapshot.child("password").getValue(String.class);
                    if (firebasePassword != null && firebasePassword.equals(password)) {
                        // If credentials match, go to MainActivity
                        navigateToMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Нууц үг буруу байна", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // If not found in admins, check in "users" node
                    checkUserCredentials(username, password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Өгөгдөл уншихад алдаа гарлаа", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserCredentials(String username, String password) {
        // Check in "users" node
        databaseReference.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String firebasePassword = dataSnapshot.child("password").getValue(String.class);
                    if (firebasePassword != null && firebasePassword.equals(password)) {
                        // If user credentials match, navigate to UserActivity
                        navigateToUserActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Нууц үг буруу байна", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Хэрэглэгч олдсонгүй", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Өгөгдөл уншихад алдаа гарлаа", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();  // Close LoginActivity
    }

    private void navigateToUserActivity() {
        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
        startActivity(intent);
        finish();  // Close LoginActivity
    }
}