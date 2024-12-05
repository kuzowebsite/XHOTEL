package com.example.xhotel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ConfirmedOrdersAdapter extends RecyclerView.Adapter<ConfirmedOrdersAdapter.ViewHolder> {

    private List<OrderModel> orders;

    public ConfirmedOrdersAdapter(List<OrderModel> orders) {
        this.orders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_confirmed_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderModel order = orders.get(position);
        holder.phoneNumberTextView.setText("Утасны дугаар: " + order.phoneNumber);
        holder.remainingTimeTextView.setText("Үлдсэн хугацаа: " + formatTime(order.days, order.hours, order.minutes, order.seconds));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private String formatTime(int days, int hours, int minutes, int seconds) {
        return String.format("%d өдөр %02d:%02d:%02d", days, hours, minutes, seconds);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView phoneNumberTextView;
        TextView remainingTimeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
            remainingTimeTextView = itemView.findViewById(R.id.remainingTimeTextView);
        }
    }
}