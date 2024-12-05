package com.example.xhotel;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ConfirmedOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ConfirmedOrdersAdapter adapter;
    private List<OrderModel> confirmedOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_orders);

        recyclerView = findViewById(R.id.confirmedOrdersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        confirmedOrders = new ArrayList<>();
        adapter = new ConfirmedOrdersAdapter(confirmedOrders);
        recyclerView.setAdapter(adapter);

        loadConfirmedOrders();
    }

    private void loadConfirmedOrders() {
        // TODO: Load confirmed orders from Firebase
        // For each order, create an OrderModel and add it to confirmedOrders
        // Then call adapter.notifyDataSetChanged();
    }
}