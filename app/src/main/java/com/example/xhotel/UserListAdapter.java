package com.example.xhotel;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<User> {
    private Context context;
    private List<User> userList;

    public UserListAdapter(Context context, List<User> userList) {
        super(context, R.layout.user_item, userList);
        this.context = context;
        this.userList = userList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.user_item, parent, false);
        }

        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        TextView passwordTextView = convertView.findViewById(R.id.passwordTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        User user = userList.get(position);

        // Хэрэглэгчийн нэр болон нууц үгийг харуулах
        // Нэрийн өмнө "Админ:" эсвэл "Хэрэглэгч:" гэдэг үгийг оруулах
        if (isAdmin(user.getUsername())) {
            usernameTextView.setText("Админ: " + user.getUsername());
        } else {
            usernameTextView.setText("Хэрэглэгч: " + user.getUsername());
        }

        passwordTextView.setText(user.getPassword());

        // Устгах товчлуур дээр дархад confirmDeleteUser() ажиллах
        deleteButton.setOnClickListener(v -> confirmDeleteUser(user.getUsername()));

        return convertView;
    }

    private void confirmDeleteUser(final String username) {
        new AlertDialog.Builder(context)
                .setMessage("Устгахдаа итгэлтэй байна уу?")
                .setCancelable(false)
                .setPositiveButton("Тийм", (dialog, id) -> deleteUser(username))
                .setNegativeButton("Үгүй", null)
                .show();
    }

    private void deleteUser(String username) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(username).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Хэрэглэгч устгагдлаа", Toast.LENGTH_SHORT).show();
                        ((AdminActivity) context).loadUsers();  // Хэрэглэгчийн жагсаалтыг шинэчлэх
                    } else {
                        Toast.makeText(context, "Устгахад алдаа гарлаа", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Админ хэрэглэгчийг ялгах зориулалттай функц
    private boolean isAdmin(String username) {
        // Энэ нь админ хэрэглэгчийн жагсаалтыг шалгах код байж болно
        // Жишээ нь, хэрэв username "admin" бол админ гэж үзэж болно
        return username.equals("admin");  // Эсвэл Firebase-ийн админ хэрэглэгчийн мэдээллээс шалгах
    }
}