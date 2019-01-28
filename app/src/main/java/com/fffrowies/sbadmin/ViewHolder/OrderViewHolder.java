package com.fffrowies.sbadmin.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fffrowies.sbadmin.Interface.ItemClickListener;
import com.fffrowies.sbadmin.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txvOrderId, txvOrderStatus, txvOrderPhone, txvOrderAddress;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txvOrderId = (TextView) itemView.findViewById(R.id.order_id);
        txvOrderStatus = (TextView) itemView.findViewById(R.id.order_status);
        txvOrderPhone = (TextView) itemView.findViewById(R.id.order_phone);
        txvOrderAddress = (TextView) itemView.findViewById(R.id.order_address);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
