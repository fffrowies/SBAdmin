package com.fffrowies.sbadmin.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.fffrowies.sbadmin.Interface.ItemClickListener;
import com.fffrowies.sbadmin.Model.Order;
import com.fffrowies.sbadmin.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txv_cart_name, txv_price;
    public ImageView img_cart_count;

    private ItemClickListener itemClickListener;

    public void setTxv_cart_name(TextView txv_cart_name) {
        this.txv_cart_name = txv_cart_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txv_cart_name = (TextView) itemView.findViewById(R.id.cart_item_name);
        txv_price = (TextView) itemView.findViewById(R.id.cart_item_price);
        img_cart_count = (ImageView) itemView.findViewById(R.id.cart_item_count);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter  extends RecyclerView.Adapter<CartViewHolder> {
    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listData.get(position).getQuantity(), Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()))
                * (Integer.parseInt(listData.get(position).getQuantity()));
        holder.txv_price.setText(fmt.format(price));

        holder.txv_cart_name.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
