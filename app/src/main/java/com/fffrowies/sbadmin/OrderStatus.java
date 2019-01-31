package com.fffrowies.sbadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.fffrowies.sbadmin.Common.Common;
import com.fffrowies.sbadmin.Model.Request;
import com.fffrowies.sbadmin.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recycler_order_status;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recycler_order_status = (RecyclerView) findViewById(R.id.listOrders);
        recycler_order_status.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_order_status.setLayoutManager(layoutManager);

        //If we start OrderStatus activity from Home activity we will no put
        //any extra, so we just loadOrder by phone from Common
        if (getIntent() == null) {

            Toast.makeText(this, "> > > OPCION 'A' > > >", Toast.LENGTH_SHORT).show();

            loadOrders(Common.currentUser.getPhone());
        }
        else {

            Toast.makeText(this, "< < < OPCION 'B' < < <", Toast.LENGTH_SHORT).show();

            loadOrders(Common.currentUser.getPhone());
//            loadOrders(getIntent().getStringExtra("userPhone"));
        }
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txvOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txvOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txvOrderPhone.setText(model.getPhone());
                viewHolder.txvOrderAddress.setText(model.getAddress());
            }
        };
        recycler_order_status.setAdapter(adapter);
    }
}
