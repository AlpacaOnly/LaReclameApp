package com.example.lareclame.items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.example.lareclame.MainActivity;
import com.example.lareclame.ProfileActivity;
import com.example.lareclame.R;
import com.example.lareclame.recyclerView.RecyclerViewMargin;
import com.example.lareclame.recyclerView.recyclerAdapterItem;
import com.example.lareclame.recyclerView.recyclerAdapterReview;
import com.example.lareclame.requests.GetItemRequest;
import com.example.lareclame.requests.ImageRequest;
import com.example.lareclame.requests.ReviewRequest;
import com.example.lareclame.reviews.Review;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemActivity extends AppCompatActivity {
    TextView date;
    TextView title;
    TextView description;
    TextView price_type;
    TextView author_name;
    ImageSlider image_slider;
    ViewFlipper imageFlipper;
    RatingBar ratingBar;
    CircleImageView owner_image;
    ArrayList<Review> reviewList = new ArrayList<>();
    private RecyclerView recyclerView;
    private recyclerAdapterReview adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        title = findViewById(R.id.item_title);
        date = findViewById(R.id.item_date);
        description = findViewById(R.id.item_description);
        price_type = findViewById(R.id.tv_price_type);
        image_slider = findViewById(R.id.image_slider);
        imageFlipper = (ViewFlipper) findViewById(R.id.image_flipper);
        recyclerView = findViewById(R.id.reviewsRecyclerView);
        ratingBar = findViewById(R.id.rating_bar);
        author_name = findViewById(R.id.username);
        owner_image = findViewById(R.id.item_publisher_photo);
        Intent intent = getIntent();

        title.setText(intent.getStringExtra("title"));
        date.setText(intent.getStringExtra("date"));
        description.setText(intent.getStringExtra("description"));
        if (intent.getStringExtra("price_type").equals("Fixed")) price_type.setText(intent.getStringExtra("price"));
        else price_type.setText(intent.getStringExtra("price_type"));
        String username = intent.getStringExtra("username");
        String owner_image_base64 = intent.getStringExtra("image");

        if(!owner_image_base64.equalsIgnoreCase("") ){
            byte[] b = Base64.decode(owner_image_base64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            owner_image.setImageBitmap(bitmap);
        }

        author_name.setText(username);

        int item_id = intent.getIntExtra("id", 0);

        Response.Listener<String> listener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                try {
                    String status = jsonObject.getString("status");
                    if (status.equals("ok")) {
                        JSONObject item = jsonObject.getJSONObject("item");
                        Response.Listener<String> listener2 = response2 -> {
                            try {
                                JSONObject itemJSON = new JSONObject(response2);
                                boolean is_image_exist = false;
                                try {
                                    JSONArray pictures = itemJSON.getJSONArray("image");
                                    ArrayList<Bitmap> bitmaps = new ArrayList<>();
                                    for (int j = 0; j < pictures.length(); j++) {
                                        String image_base64 = URLDecoder.decode(pictures.getString(j), StandardCharsets.UTF_8.name());
                                        byte[] b = Base64.decode(image_base64, Base64.DEFAULT);
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                                        ImageView image = new ImageView(getApplicationContext());
                                        image.setImageBitmap(bitmap);
                                        imageFlipper.addView(image);
                                        is_image_exist = true;
                                    }

                                    if (!is_image_exist) {
                                        ImageView imgView = new ImageView(getApplicationContext());
                                        Drawable drawable = getResources().getDrawable(R.drawable.no_image);
                                        imgView.setImageDrawable(drawable);
                                        imageFlipper.addView(imgView);
                                    }

                                    imageFlipper.setFlipInterval(2000); //5s intervals
                                    imageFlipper.startFlipping();


                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        };

                        ImageRequest imageRequest = new ImageRequest("item-pictures", item.getJSONArray("pictures"), listener2, System.out::println);
                        RequestQueue requestQueue = Volley.newRequestQueue(this);
                        requestQueue.add(imageRequest);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        GetItemRequest getItemRequest = new GetItemRequest(item_id, listener, null);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getItemRequest);

        setReviewsInfo(item_id);
        setAdapter();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.ic_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                case R.id.ic_create_announcement:
                    startActivity(new Intent(getApplicationContext(), CreateItemActivity.class));
                    return true;
                case R.id.ic_settings:
                    return true;
                case R.id.ic_profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    return true;
            }
            return false;
        });
    }

    private void setReviewsInfo(int item_id) {
        Response.Listener<String> listener1 = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                try {
                    String status = jsonObject.getString("status");
                    if (status.equals("ok")) {
                        JSONArray reviews = jsonObject.getJSONArray("reviews");
                        reviewList = new ArrayList<>();

                        for (int i = 0; i < reviews.length(); i++) {
                            JSONObject reviewJSON = reviews.getJSONObject(i);
                            Review review = new Review(reviewJSON.getInt("id"), reviewJSON.getInt("item_id"), reviewJSON.getInt("user_id"), reviewJSON.getString("title"), reviewJSON.getString("description"), reviewJSON.getInt("rating"), reviewJSON.getString("created"));
                            reviewList.add(review);
                            adapter = new recyclerAdapterReview(reviewList);
                            recyclerView.setAdapter(adapter);
                        }
                    } else {
                        String error = jsonObject.getString("error");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
                        builder.setMessage(error).setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        ReviewRequest reviewRequest = new ReviewRequest(item_id, listener1, System.out::println);

        RequestQueue reviewRequestQueue = Volley.newRequestQueue(this);
        reviewRequestQueue.add(reviewRequest);
    }

    private void    setAdapter() {
        recyclerAdapterReview adapter = new recyclerAdapterReview(reviewList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerViewMargin decoration = new RecyclerViewMargin(10, 1);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        String name = new Timestamp(System.currentTimeMillis()).toString();
        File mypath = new File(directory, name + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
}