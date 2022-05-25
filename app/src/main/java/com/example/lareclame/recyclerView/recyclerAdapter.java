package com.example.lareclame.recyclerView;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.lareclame.MainActivity;
import com.example.lareclame.items.Item;
import com.example.lareclame.R;
import com.example.lareclame.items.ItemActivity;
import com.example.lareclame.requests.ImageRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private ArrayList <Item> itemsList;

    public recyclerAdapter(ArrayList <Item> itemsList) {
        this.itemsList =itemsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView tv_name;
        private final TextView tv_date;
        private final TextView tv_price_type;
        private final ImageView item_photo;


        public MyViewHolder(final View View) {
            super(View);
            tv_name = View.findViewById(R.id.item_title);
            tv_date = View.findViewById(R.id.item_date);
            tv_price_type = View.findViewById(R.id.tv_price_type);
            item_photo = View.findViewById(R.id.item_photo);
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

        ArrayList<Bitmap> bitmaps = item.getPictures();
        if (bitmaps.size() > 0) {
            holder.item_photo.setImageBitmap(bitmaps.get(0));
        }

        if (item.getPrice_type().equals("Fixed")) {
            holder.tv_price_type.setText(item.getPrice()+"");
        } else {
            holder.tv_price_type.setText(item.getPrice_type());
        }
        holder.tv_name.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ItemActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("date", item.getDate());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("price_type", item.getPrice_type());


//            Bundle args = new Bundle();
//            args.putSerializable("ARRAYLIST", (Serializable) bytes);
//            intent.putExtra("BUNDLE", args);

            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

}
