package com.example.xhotel;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Энд image болон text үүсгэж тохируулах
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        // Intent-ээр ирсэн өгөгдлийг авах
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String text = getIntent().getStringExtra("text");

        // Энд зураг болон текстийг тохируулах
        // Glide.with(this).load(imageUrl).into(imageView);
        textView.setText(text);
    }
}