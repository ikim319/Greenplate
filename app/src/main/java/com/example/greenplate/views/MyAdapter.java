package com.example.greenplate.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenplate.R;
import com.example.greenplate.model.ShoppingListModel;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<ShoppingListModel> list;

    public MyAdapter(Context context, ArrayList<ShoppingListModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ShoppingListModel shop = list.get(position);
        holder.ingName.setText(shop.getIngredientName());
        holder.quantAmount.setText(shop.getQuantity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {

        TextView ingName;
        TextView quantAmount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ingName = itemView.findViewById(R.id.ingName);
            quantAmount = itemView.findViewById(R.id.quantAmount);
        }
    }
}
