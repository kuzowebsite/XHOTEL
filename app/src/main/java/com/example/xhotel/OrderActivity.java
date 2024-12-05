package com.example.xhotel;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private LinearLayout orderLayout;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Firebase-ийн "reservations" лавлагааг ашиглан захиалгуудыг унших
        databaseReference = FirebaseDatabase.getInstance().getReference("reservations");

        orderLayout = findViewById(R.id.orderLayout);
        searchEditText = findViewById(R.id.searchEditText);

        // Эхэндээ бүх захиалгуудыг ачаалах
        loadOrders();

        // Хайлт хийх талбарын текст өөрчлөгдөх бүрт дуудагдах listener
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = searchEditText.getText().toString().trim();
                if (query.isEmpty()) {
                    loadOrders(); // Хайлтын талбар хоосон байвал бүх захиалгуудыг ачаалах
                } else {
                    searchOrderByPhone(query); // Хайлт хийж байх
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadOrders() {
        // Бүх захиалгуудыг ачаалах
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderLayout.removeAllViews(); // Өмнөх өгөгдлийг арилгана

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    displayOrder(snapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Алдааг зохицуулах
            }
        });
    }

    private void searchOrderByPhone(String phoneQuery) {
        // Файрбейсээс утасны дугаарын дагуу хайх
        databaseReference.orderByChild("phoneNumber")
                .startAt(phoneQuery) // Дуганаас эхлэх
                .endAt(phoneQuery + "\uf8ff") // Бүх тохирох утгыг дуусгах
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        orderLayout.removeAllViews(); // Өмнөх өгөгдлийг арилгана
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            displayOrder(snapshot); // Захиалгыг харуулах
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Алдааг зохицуулах
                    }
                });
    }

    private void displayOrder(DataSnapshot snapshot) {
        String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
        String imageUrl = snapshot.child("imageUrl").getValue(String.class);
        String text = snapshot.child("text").getValue(String.class);
        String price = snapshot.child("price").getValue(String.class);
        String numberOfRooms = snapshot.child("numberOfRooms").getValue(String.class);
        String numberOfPeople = snapshot.child("numberOfPeople").getValue(String.class);
        String numberOfBeds = snapshot.child("numberOfBeds").getValue(String.class);
        String numberOfNights = snapshot.child("numberOfNights").getValue(String.class);  // Хоногийн тоо

        // Захиалгын мэдээлэл тус бүрийг TextView дээр харуулах
        TextView orderDetailsTextView = new TextView(OrderActivity.this);
        orderDetailsTextView.setText("Дугаар: " + phoneNumber + "\n" +
                "Байр: " + text + "\n" +
                "Үнэ: " + price + "\n" +
                "Өрөөний тоо: " + numberOfRooms + "\n" +
                "Хүний тоо: " + numberOfPeople + "\n" +
                "Орны тоо: " + numberOfBeds + "\n" +
                "Хоногийн тоо: " + numberOfNights + "\n"); // Хоногийн тоог нэмсэн
        orderDetailsTextView.setPadding(16, 16, 16, 16);

        // Зургийг харуулах ImageView үүсгэх
        ImageView orderImageView = new ImageView(OrderActivity.this);
        orderImageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                500)); // Зургийн өндөр 500px байх
        orderImageView.setPadding(16, 16, 16, 16);

        // Glide-ээр зургийг ачаалах
        Glide.with(OrderActivity.this)
                .load(imageUrl)
                .into(orderImageView);

        // Устгах товч үүсгэх
        Button deleteButton = new Button(OrderActivity.this);
        deleteButton.setText("Устгах");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(snapshot.getKey());
            }
        });

        // Баталгаажуулах товч үүсгэх
        Button confirmButton = new Button(OrderActivity.this);
        confirmButton.setText("Баталгаажуулах");
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOrder(snapshot);
            }
        });

        // Layout-д зураг, текст, устгах, баталгаажуулах товч нэмэх
        orderLayout.addView(orderImageView);
        orderLayout.addView(orderDetailsTextView);
        orderLayout.addView(deleteButton);
        orderLayout.addView(confirmButton);
    }

    private void confirmOrder(DataSnapshot snapshot) {
        String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
        String text = snapshot.child("text").getValue(String.class);
        String price = snapshot.child("price").getValue(String.class);
        String numberOfNights = snapshot.child("numberOfNights").getValue(String.class);

        // Convert number of nights to days
        int days = Integer.parseInt(numberOfNights);

        // Firebase дээр баталгаажуулсан захиалга нэмэх
        DatabaseReference confirmedReference = FirebaseDatabase.getInstance().getReference("confirmedOrders");
        String orderKey = snapshot.getKey();  // Захиалгын түлхүүр
        OrderModel confirmedOrder = new OrderModel(phoneNumber, text, price, days, 0, 0, 0);

        confirmedReference.child(orderKey).setValue(confirmedOrder).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Захиалгыг баталгаажуулсны дараа анхны захиалгыг устгах
                databaseReference.child(orderKey).removeValue().addOnCompleteListener(removeTask -> {
                    if (removeTask.isSuccessful()) {
                        loadOrders(); // OrderActivity-г шинэчлэх

                        // ConfirmedActivity руу шилжих
                        Intent intent = new Intent(OrderActivity.this, ConfirmedActivity.class);
                        intent.putExtra("phoneNumber", phoneNumber);
                        intent.putExtra("days", days);
                        startActivity(intent);


                    }
                });
            }
        });
    }

    private void showDeleteConfirmationDialog(String orderKey) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Энэ захиалгыг устгах уу?")
                .setCancelable(false)
                .setPositiveButton("Тийм", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        databaseReference.child(orderKey).removeValue(); // Захиалгыг устгах
                        loadOrders(); // Захиалгуудыг дахин ачаалах
                    }
                })
                .setNegativeButton("Үгүй", null)
                .show();
    }
}