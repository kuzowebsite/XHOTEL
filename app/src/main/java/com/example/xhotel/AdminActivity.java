package com.example.xhotel;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private ListView userListView;
    private ListView adminListView;
    private DatabaseReference databaseReference;
    private UserListAdapter userListAdapter;
    private AdminListAdapter adminListAdapter;
    private List<User> userList;
    private List<User> adminList;
    private CheckBox adminCheckBox;
    private EditText searchEditText;  // Хайлтын EditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        userListView = findViewById(R.id.userListView);
        adminListView = findViewById(R.id.adminListView);
        adminCheckBox = findViewById(R.id.adminCheckBox);
        searchEditText = findViewById(R.id.searchEditText);  // Хайлтын EditText-г оруулах

        databaseReference = FirebaseDatabase.getInstance().getReference();
        userList = new ArrayList<>();
        adminList = new ArrayList<>();

        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            boolean isAdmin = adminCheckBox.isChecked();

            if (!username.isEmpty() && !password.isEmpty()) {
                registerUser(username, password, isAdmin);
            } else {
                Toast.makeText(AdminActivity.this, "Бүх талбарыг бөглөнө үү", Toast.LENGTH_SHORT).show();
            }
        });

        loadUsers();
        loadAdmins();

        // Хайлтын үйлдэл
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Хүснэгт эхлэхээс өмнө юу ч хийхгүй
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Хайлтын текст өөрчлөгдсөөр байх үед жагсаалт шүүх
                filterUsers(charSequence.toString());
                filterAdmins(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Хүснэгт өөрчлөгдсөний дараа юу ч хийхгүй
            }
        });
    }

    private void registerUser(String username, String password, boolean isAdmin) {
        User user = new User(username, password);

        if (isAdmin) {
            databaseReference.child("admins").child(username).setValue(user)
                    .addOnCompleteListener(adminTask -> {
                        if (adminTask.isSuccessful()) {
                            Toast.makeText(AdminActivity.this, "Админ бүртгэл амжилттай", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminActivity.this, "Админ бүртгэлд алдаа гарлаа", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            databaseReference.child("users").child(username).setValue(user)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminActivity.this, "Бүртгэл амжилттай", Toast.LENGTH_SHORT).show();
                            loadUsers(); // Хэрэглэгчдийн жагсаалтыг шинэчлэх
                        } else {
                            Toast.makeText(AdminActivity.this, "Бүртгэлд алдаа гарлаа", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void loadUsers() {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                userListAdapter = new UserListAdapter(AdminActivity.this, userList);
                userListView.setAdapter(userListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminActivity.this, "Өгөгдөл уншихад алдаа гарлаа", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadAdmins() {
        databaseReference.child("admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adminList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        adminList.add(user);
                    }
                }
                adminListAdapter = new AdminListAdapter(AdminActivity.this, adminList);
                adminListView.setAdapter(adminListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminActivity.this, "Өгөгдөл уншихад алдаа гарлаа", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Хэрэглэгчийг хайх функц
    private void filterUsers(String query) {
        List<User> filteredUsers = new ArrayList<>();
        for (User user : userList) {
            if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                filteredUsers.add(user);
            }
        }
        userListAdapter = new UserListAdapter(AdminActivity.this, filteredUsers);
        userListView.setAdapter(userListAdapter);
    }

    // Админыг хайх функц
    private void filterAdmins(String query) {
        List<User> filteredAdmins = new ArrayList<>();
        for (User user : adminList) {
            if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                filteredAdmins.add(user);
            }
        }
        adminListAdapter = new AdminListAdapter(AdminActivity.this, filteredAdmins);
        adminListView.setAdapter(adminListAdapter);
    }
}