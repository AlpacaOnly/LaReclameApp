package com.example.lareclame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.lareclame.requests.UploadImageRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ArrayList<Item> itemsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private recyclerAdapterItem adapter;
    private ImageView ProfileImage;
    private static final int PICK_IMAGE = 1;
    TextView tv_rating;
    Uri ImageUrl;

    boolean not_owner = false;
    int user_id = 0;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        recyclerView = findViewById(R.id.recyclerView);
        tv_rating=findViewById(R.id.tv_rating);
        ProfileImage = (ImageView) findViewById(R.id.user_photo);
        TextView tv_username = (TextView) findViewById(R.id.username);
        TextView tv_bio = (TextView) findViewById(R.id.tv_bio);

        Intent intent = getIntent();

        not_owner = intent.getBooleanExtra("not_owner", false);

        int rating = 0;
        String username = "", bio = "", picture = "";

        if (not_owner) {

        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
            try {
                JSONObject user = new JSONObject(sharedPreferences.getString("user", ""));
                user_id = user.getInt("id");
                rating = user.getInt("rating");
                username = user.getString("username");
                bio = user.getString("bio");
                picture = sharedPreferences.getString("profile-image", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        tv_username.setText(username);
        tv_bio.setText(bio);
        tv_rating.setText(Integer.toString(rating));

        setItemInfo("", 0, user_id);
        setAdapter();


        if(!picture.equalsIgnoreCase("") ){
            byte[] b = Base64.decode(picture, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            ProfileImage.setImageBitmap(bitmap);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.ic_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.ic_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                case R.id.ic_create_announcement:
                    startActivity(new Intent(getApplicationContext(), CreateItemActivity.class));
                    return true;
                case R.id.ic_settings:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    return true;
                case R.id.ic_profile:
                    return true;
            }
            return false;
        });

    }


    public void change_photo(View view) {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallery,"Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            ImageUrl = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ImageUrl);
                String encodedImage = encodeImage(bitmap);
                SaveImage(encodedImage);
                SaveToDb(encodedImage);
                ProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void SaveImage(String encodedImage) {
        SharedPreferences sh = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();
        edit.putString("profile-image", encodedImage);
        edit.apply();
    }

    private void SaveToDb(String encodedImage) {
        UploadImageRequest uploadImageRequest = new UploadImageRequest(user_id, encodedImage, null, System.out::println);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(uploadImageRequest);
    }

    private String encodeImage(Bitmap realImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        realImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void setItemInfo(String search_text, int category_id, int user_id) {
        Response.Listener<String> listener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");

                if (status.equals("ok")) {
                    JSONArray items = jsonObject.getJSONArray("items");
                    itemsList = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject itemJSON = items.getJSONObject(i);
                        int item_user_id = itemJSON.getInt("user_id");

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
                                            String image = jsonObject3.getString("image");

                                            User user = new User(id, username, image);
                                            Item item = new Item(itemJSON.getInt("id"), itemJSON.getString("title"), itemJSON.getString("description"), itemJSON.getString("created"), itemJSON.getString("price_type"), itemJSON.getInt("price"), bitmaps, user);
                                            itemsList.add(item);
                                            adapter = new recyclerAdapterItem(itemsList);
                                            recyclerView.setAdapter(adapter);
                                        } catch (JSONException | ParseException e) {
                                            e.printStackTrace();
                                        }
                                    };

                                    GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest(item_user_id, listener3, System.out::println);
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

        GetItemsRequest getItemsRequest = new GetItemsRequest(listener, System.out::println, search_text, category_id, user_id);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getItemsRequest);
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


}
