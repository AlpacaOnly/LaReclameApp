package com.example.lareclame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.lareclame.items.CreateItemActivity;
import com.example.lareclame.models.Item;
import com.example.lareclame.models.User;
import com.example.lareclame.recyclerView.RecyclerViewMargin;
import com.example.lareclame.recyclerView.recyclerAdapterItem;
import com.example.lareclame.requests.GetItemsRequest;
import com.example.lareclame.requests.GetUserInfoRequest;
import com.example.lareclame.requests.ImageRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Item> itemsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private recyclerAdapterItem adapter;
    private SearchView searchView;

    ArrayAdapter<String> arrayAdapter;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        itemsList = new ArrayList<>();
        searchView = findViewById(R.id.search);


        setItemInfo("", 0);
        setAdapter();

        searchView.setQueryHint("Type Here to Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String search_text = searchView.getQuery().toString().toLowerCase();
                setItemInfo(search_text, 0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.ic_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.ic_home:
                    return true;
                case R.id.ic_create_announcement:
                    startActivity(new Intent(getApplicationContext(), CreateItemActivity.class));
                    return true;
                case R.id.ic_settings:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    return true;
                case R.id.ic_profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
            return false;
        });
    }

    private void setAdapter() {
        recyclerAdapterItem adapter = new recyclerAdapterItem(itemsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerViewMargin decoration = new RecyclerViewMargin(10, 1);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setItemInfo(String search_text, int category_id) {
        Response.Listener<String> listener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");

                if (status.equals("ok")) {
                    JSONArray items = jsonObject.getJSONArray("items");
                    itemsList = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject itemJSON = items.getJSONObject(i);
                        int user_id = itemJSON.getInt("user_id");

                        Response.Listener<String> listener2 = response2 -> {
                            try {
                                JSONObject jsonObject2 = new JSONObject(response2);
                                try {
                                    JSONArray pictures = jsonObject2.getJSONArray("image");
                                    ArrayList<Bitmap> bitmaps = new ArrayList<>();
                                    for (int j = 0; j < pictures.length(); j++) {
                                        String image_base64 = URLDecoder.decode(pictures.getString(j), StandardCharsets.UTF_8.name());
                                        byte[] b = Base64.decode(image_base64, Base64.DEFAULT);
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                                        bitmaps.add(bitmap);
                                    }

                                    Response.Listener<String> listener3 = response3 -> {
                                        try {
                                            JSONObject jsonObject3 = new JSONObject(response3);
                                            int id = jsonObject3.getInt("id");
                                            String username = jsonObject3.getString("username");

                                            User user = new User(id, username);
                                            Item item = new Item(itemJSON.getInt("id"), itemJSON.getString("title"), itemJSON.getString("description"), itemJSON.getString("created"), itemJSON.getString("price_type"), itemJSON.getInt("price"), bitmaps, user);
                                            itemsList.add(item);
                                            adapter = new recyclerAdapterItem(itemsList);
                                            recyclerView.setAdapter(adapter);
                                        } catch (JSONException | ParseException e) {
                                            e.printStackTrace();
                                        }
                                    };

                                    GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest(user_id, listener3, System.out::println);
                                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                                    requestQueue.add(getUserInfoRequest);

                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        };

                        ImageRequest imageRequest = new ImageRequest("item-pictures", itemJSON.getJSONArray("pictures"), listener2, System.out::println);
                        RequestQueue requestQueue = Volley.newRequestQueue(this);
                        requestQueue.add(imageRequest);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        GetItemsRequest getItemsRequest = new GetItemsRequest(listener, System.out::println, search_text, category_id, 0);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getItemsRequest);
    }

    private void setImage(ImageView item_photo, String picturePath) {

    }

    public void accommodations(View view) {
        setItemInfo("", 1);
    }

    public void buy_sale(View view) {
        setItemInfo("", 2);
    }

    public void lost_and_found(View view) {
        setItemInfo("", 3);
    }

    public void project(View view) {
        setItemInfo("", 4);
    }

    public void services(View view) {
        setItemInfo("", 5);
    }

    public void other(View view) {
        setItemInfo("", 6);
    }


}