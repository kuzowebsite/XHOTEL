package com.example.xhotel;

import android.widget.Button;
import android.widget.Toast;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class AdminListAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> adminList;

    public AdminListAdapter(Context context, List<User> adminList) {
        super(context, R.layout.item_user, adminList);
        this.context = context;
        this.adminList = adminList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        }

        User user = adminList.get(position);

        // Хэрэглэгчийн нэр болон нууц үгийг харуулах
        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        TextView passwordTextView = convertView.findViewById(R.id.passwordTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        // Нэрийн өмнө "Админ:" гэсэн үг оруулах
        usernameTextView.setText("Админ: " + user.getUsername());
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

        // Хэрэглэгч эсвэл админ байхаас үл хамааран устгах
        databaseReference.child("admins").child(username).removeValue()  // Устгах админыг
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Админ устгагдлаа", Toast.LENGTH_SHORT).show();
                        ((AdminActivity) context).loadAdmins();  // Админы жагсаалтыг шинэчлэх
                    } else {
                        // Хэрэглэгчийг устгах
                        databaseReference.child("users").child(username).removeValue()
                                .addOnCompleteListener(userTask -> {
                                    if (userTask.isSuccessful()) {
                                        Toast.makeText(context, "Хэрэглэгч устгагдлаа", Toast.LENGTH_SHORT).show();
                                        ((AdminActivity) context).loadUsers();  // Хэрэглэгчийн жагсаалтыг шинэчлэх
                                    } else {
                                        Toast.makeText(context, "Устгахад алдаа гарлаа", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }
}