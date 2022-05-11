package com.example.lareclame.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lareclame.items.Item;
import com.example.lareclame.R;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private ArrayList <Item> itemsList;

    public recyclerAdapter(ArrayList <Item> itemsList) {
        this.itemsList =itemsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView tv_name;
        private final TextView tv_date;

        public MyViewHolder(final View View) {
            super(View);
            tv_name = View.findViewById(R.id.item_name);
            tv_date = View.findViewById(R.id.date);
        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        Item item = itemsList.get(position);
        holder.tv_name.setText(item.getTitle());
        holder.tv_date.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

}
