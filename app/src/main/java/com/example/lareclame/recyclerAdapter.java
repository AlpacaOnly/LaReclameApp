package com.example.lareclame;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private ArrayList <Item> itemList;

    public recyclerAdapter(ArrayList <Item> itemList) {
        this.itemList=itemList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.item_name);
        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
