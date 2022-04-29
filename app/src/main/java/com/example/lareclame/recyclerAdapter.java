package com.example.lareclame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private ArrayList <Item> itemsList;

    public recyclerAdapter(ArrayList <Item> itemsList) {
        this.itemsList =itemsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;

        public MyViewHolder(final View View) {
            super(View);
            tv_name=View.findViewById(R.id.item_name);
        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String name= itemsList.get(position).getName();
        holder.tv_name.setText(name);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

}
