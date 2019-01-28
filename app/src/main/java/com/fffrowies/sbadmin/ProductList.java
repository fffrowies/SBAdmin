package com.fffrowies.sbadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.fffrowies.sbadmin.Interface.ItemClickListener;
import com.fffrowies.sbadmin.Model.Product;
import com.fffrowies.sbadmin.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductList extends AppCompatActivity {

    RecyclerView recycler_product;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference productList;

    String categoryId = "";

    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        productList = database.getReference("Products");

        recycler_product = (RecyclerView) findViewById(R.id.recycler_product);
        recycler_product.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_product.setLayoutManager(layoutManager);

        //Get Intent here
        if (getIntent() != null) categoryId = getIntent().getStringExtra("CategoryId");

        if (!categoryId.isEmpty() && categoryId != null) {

            loadProductList(categoryId);
        }
    }

    private void loadProductList(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder> (
                Product.class,
                R.layout.product_item,
                ProductViewHolder.class,
                productList.orderByChild("CategoryId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {

                viewHolder.product_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.product_image);

                final Product local = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new Activity
                        Intent productDetailIntent = new Intent(ProductList.this, ProductDetail.class);
                        productDetailIntent.putExtra("ProductId", adapter.getRef(position).getKey());
                        startActivity(productDetailIntent);
                    }
                });
            }
        };

        //Set Adapter
        Log.d("TAG", ""+adapter.getItemCount());
        recycler_product.setAdapter(adapter);
    }
}
