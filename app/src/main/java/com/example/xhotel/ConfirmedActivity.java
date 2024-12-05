package com.example.xhotel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.os.Looper;
import android.graphics.Color;
import android.view.View;

public class ConfirmedActivity extends AppCompatActivity {

    private TextView confirmedTextView;
    private TextView phoneNumberTextView;
    private int totalSeconds;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateTimeRunnable;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed);

        confirmedTextView = findViewById(R.id.confirmedTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);

        // Get data from Intent
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        int days = intent.getIntExtra("days", 0);

        phoneNumberTextView.setText("Утасны дугаар: " + phoneNumber);

        // Load saved time or calculate new time
        SharedPreferences prefs = getSharedPreferences("ConfirmedOrders", MODE_PRIVATE);
        totalSeconds = prefs.getInt(phoneNumber, days * 24 * 60 * 60);

        // Start the countdown
        startCountdown();
    }

    private void startCountdown() {
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                if (totalSeconds > 0) {
                    updateTimeDisplay();
                    totalSeconds--;
                    saveRemainingTime();
                    handler.postDelayed(this, 1000);
                } else {
                    showTimeUpMessage();
                }
            }
        };
        handler.post(updateTimeRunnable);
    }

    private void updateTimeDisplay() {
        int days = totalSeconds / (24 * 60 * 60);
        int hours = (totalSeconds % (24 * 60 * 60)) / (60 * 60);
        int minutes = (totalSeconds % (60 * 60)) / 60;
        int seconds = totalSeconds % 60;

        String time = String.format("%d өдөр %02d:%02d:%02d", days, hours, minutes, seconds);
        confirmedTextView.setText(time);
    }

    private void showTimeUpMessage() {
        confirmedTextView.setText("Үйлчлүүлэгчийн хугацаа дууссан");
        confirmedTextView.setTextColor(Color.RED);
        confirmedTextView.setBackgroundColor(Color.YELLOW);
    }

    private void saveRemainingTime() {
        SharedPreferences.Editor editor = getSharedPreferences("ConfirmedOrders", MODE_PRIVATE).edit();
        editor.putInt(phoneNumber, totalSeconds);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && updateTimeRunnable != null) {
            handler.removeCallbacks(updateTimeRunnable);
        }
        saveRemainingTime();
    }
}