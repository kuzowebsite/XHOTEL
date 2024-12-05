package com.example.xhotel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageTextAdapter extends RecyclerView.Adapter<ImageTextAdapter.ViewHolder> {

    private List<ImageTextItem> imageTextItemList;
    private OnItemClickListener onItemClickListener;

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(ImageTextItem item);
    }

    // Constructor
    public ImageTextAdapter(List<ImageTextItem> imageTextItemList, OnItemClickListener onItemClickListener) {
        this.imageTextItemList = imageTextItemList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current item
        ImageTextItem item = imageTextItemList.get(position);

        // Load the image using Glide
        Glide.with(holder.itemView.getContext()).load(item.getImageUrl()).into(holder.imageView);

        // Set the text and price for the current item
        holder.textView.setText(item.getText());
        holder.priceTextView.setText(item.getPrice()); // Display the price

        // Set the click listener for the item
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return imageTextItemList.size();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        TextView priceTextView;  // Price TextView

        public ViewHolder(View itemView) {
            super(itemView);

            // Initialize views
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            priceTextView = itemView.findViewById(R.id.priceTextView); // Bind the price TextView
        }
    }
}