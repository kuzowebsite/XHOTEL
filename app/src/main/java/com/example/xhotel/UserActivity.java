package com.example.xhotel;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageTextAdapter adapter;
    private List<ImageTextItem> imageTextItemList;
    private DatabaseReference databaseReference;
    private TextInputEditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        recyclerView = findViewById(R.id.recyclerView);
        imageTextItemList = new ArrayList<>();
        adapter = new ImageTextAdapter(imageTextItemList, this::showItemDetailsDialog);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("texts");
        searchEditText = findViewById(R.id.searchEditText);

        loadImageAndText();
        setupSearchListener();
        setupLogoutButton();
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String priceQuery = s.toString().trim();
                if (priceQuery.isEmpty()) {
                    loadImageAndText();
                } else {
                    searchByPrice(priceQuery);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupLogoutButton() {
        MaterialButton btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadImageAndText() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageTextItemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageTextItem item = snapshot.getValue(ImageTextItem.class);
                    if (item != null) {
                        item.setKey(snapshot.getKey());
                        imageTextItemList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void searchByPrice(String priceQuery) {
        databaseReference.orderByChild("price").startAt(priceQuery).endAt(priceQuery + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        imageTextItemList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ImageTextItem item = snapshot.getValue(ImageTextItem.class);
                            if (item != null) {
                                item.setKey(snapshot.getKey());
                                imageTextItemList.add(item);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }

    private void showItemDetailsDialog(ImageTextItem item) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_item_details, null);
        ImageView imageView = dialogView.findViewById(R.id.dialogImageView);
        TextView priceTextView = dialogView.findViewById(R.id.dialogPriceTextView);
        TextView roomsTextView = dialogView.findViewById(R.id.dialogRoomsTextView);
        TextView peopleTextView = dialogView.findViewById(R.id.dialogPeopleTextView);
        TextView bedsTextView = dialogView.findViewById(R.id.dialogBedsTextView);

        Glide.with(this).load(item.getImageUrl()).into(imageView);
        priceTextView.setText(getString(R.string.price_format, item.getPrice()));
        roomsTextView.setText(getString(R.string.rooms_format, item.getNumberOfRooms()));
        peopleTextView.setText(getString(R.string.people_format, item.getNumberOfPeople()));
        bedsTextView.setText(getString(R.string.beds_format, item.getNumberOfBeds()));

        new MaterialAlertDialogBuilder(this)
                .setTitle(item.getText())
                .setView(dialogView)
                .setPositiveButton(R.string.order, (dialog, which) -> showConfirmationDialog(item))
                .setNegativeButton(R.string.close, null)
                .show();
    }

    private void showConfirmationDialog(ImageTextItem item) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirmation, null);
        TextInputEditText phoneNumberEditText = dialogView.findViewById(R.id.phoneNumberEditText);
        TextInputEditText numberOfNightsEditText = dialogView.findViewById(R.id.numberOfNightsEditText);

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.confirm_order)
                .setView(dialogView)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    String phoneNumber = phoneNumberEditText.getText().toString().trim();
                    String numberOfNights = numberOfNightsEditText.getText().toString().trim();
                    if (isValidInput(phoneNumber, numberOfNights)) {
                        saveReservation(item, phoneNumber, numberOfNights);
                        showPhoneNumberVerifiedDialog();
                    } else {
                        showErrorDialog();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private boolean isValidInput(String phoneNumber, String numberOfNights) {
        return phoneNumber.length() == 8 && !numberOfNights.isEmpty();
    }

    private void saveReservation(ImageTextItem item, String phoneNumber, String numberOfNights) {
        DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservations").push();
        reservationRef.setValue(new Reservation(item, phoneNumber, numberOfNights));
    }

    private void showPhoneNumberVerifiedDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_verification, null);
        ImageView qrCodeImageView = dialogView.findViewById(R.id.qrCodeImageView);
        qrCodeImageView.setImageResource(R.drawable.qrcode);

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.reservation_confirmed)
                .setView(dialogView)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void showErrorDialog() {
        new MaterialAlertDialogBuilder(this)
                .setMessage(R.string.invalid_input)
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}