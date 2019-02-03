package com.fffrowies.sbadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fffrowies.sbadmin.Common.Common;
import com.fffrowies.sbadmin.Database.Database;
import com.fffrowies.sbadmin.Interface.ItemClickListener;
import com.fffrowies.sbadmin.Model.Product;
import com.fffrowies.sbadmin.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductList extends AppCompatActivity {

    RecyclerView recycler_product;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference productList;

    String categoryId = "";

    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;

    //Search functionalty
    FirebaseRecyclerAdapter<Product, ProductViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    //Favorites
    Database localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        productList = database.getReference("Products");

        //Local DB
        localDB = new Database(this);

        recycler_product = (RecyclerView) findViewById(R.id.recycler_product);
        recycler_product.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_product.setLayoutManager(layoutManager);

        //Get Intent here
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");

        if (categoryId != null && !categoryId.isEmpty())
        {
            if (Common.isConnectedToInternet(getBaseContext()))
                loadProductList(categoryId);
            else
            {
                Toast.makeText(ProductList.this, "Please, check your connection!!!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Search
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.search_bar);
        materialSearchBar.setHint("Enter your product");
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // when user type their text, we change suggest list
                List<String> suggest = new ArrayList<String>();
                for (String search:suggestList) {  // loop in suggest List
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) {
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When Search Bar is close restore original adapter
                if (!enabled) {
                    recycler_product.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //When search finish show result of search adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(
                Product.class,
                R.layout.product_item,
                ProductViewHolder.class,
                productList.orderByChild("name").equalTo(text.toString())  // Compare name
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
                        productDetailIntent.putExtra("ProductId", searchAdapter.getRef(position).getKey());
                        startActivity(productDetailIntent);
                    }
                });
            }
        };
        recycler_product.setAdapter(searchAdapter); // Set adapter for Recycler View is Search result
    }

    private void loadSuggest() {
        productList.orderByChild("categoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren()) {

                            Product item = postSnapshot.getValue(Product.class);
                            suggestList.add(item.getName()); // Add product name to suggest list
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadProductList(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder> (
                Product.class,
                R.layout.product_item,
                ProductViewHolder.class,
                productList.orderByChild("categoryId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(final ProductViewHolder viewHolder, final Product model, final int position) {

                viewHolder.product_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.product_image);

                //Add Favorites
                if (localDB.isFavorite(adapter.getRef(position).getKey()))
                    viewHolder.favorite_image.setImageResource(R.drawable.ic_favorite_black_24dp);

                //Click to change status of Favorites
                viewHolder.favorite_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!localDB.isFavorite(adapter.getRef(position).getKey()))
                        {
                            localDB.addToFavorites(adapter.getRef(position).getKey());
                            viewHolder.favorite_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(
                                    ProductList.this,
                                    "" + model.getName() + " was added to Favorites", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            localDB.removeFromFavorites(adapter.getRef(position).getKey());
                            viewHolder.favorite_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(
                                    ProductList.this,
                                    "" + model.getName() + " was removed from Favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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
