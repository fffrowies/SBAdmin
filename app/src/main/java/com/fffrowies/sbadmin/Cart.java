package com.fffrowies.sbadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.fffrowies.sbadmin.Database.Database;
import com.fffrowies.sbadmin.Model.Order;
import com.fffrowies.sbadmin.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recycler_cart;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;

    TextView txvTotalPrice;
    FButton btnPlaceOrder;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        database = FirebaseDatabase.getInstance();
        request = database.getReference("Requests");

        //Init
        recycler_cart = (RecyclerView) findViewById(R.id.listCart);
        recycler_cart.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(layoutManager);

        txvTotalPrice = (TextView) findViewById(R.id.txvTotalPrice);
        btnPlaceOrder = (FButton) findViewById(R.id.btnPlaceOrder);

        loadListProducts();
    }

    private void loadListProducts() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        recycler_cart.setAdapter(adapter);

        //Calculate total price
        int total = 0;
        for(Order order:cart)
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txvTotalPrice.setText(fmt.format(total));
    }
}
