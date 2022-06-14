package com.example.lareclame.items;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.lareclame.MainActivity;
import com.example.lareclame.ProfileActivity;
import com.example.lareclame.R;
import com.example.lareclame.recyclerView.RecyclerViewMargin;
import com.example.lareclame.recyclerView.recyclerAdapter;
import com.example.lareclame.requests.GetItemRequest;
import com.example.lareclame.requests.GetItemsRequest;
import com.example.lareclame.requests.ImageRequest;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemActivity extends AppCompatActivity {
    TextView date;
    TextView title;
    TextView description;
    TextView price_type;
    ImageSlider image_slider;
    ViewFlipper imageFlipper;
    private RecyclerView recyclerView;
    private recyclerAdapter adapter;
    String fileName = "/app/res\\drawable\\no_image.png";

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
        Intent intent = getIntent();

        title.setText(intent.getStringExtra("title"));
        date.setText(intent.getStringExtra("date"));
        description.setText(intent.getStringExtra("description"));
        price_type.setText(intent.getStringExtra("price_type"));
        int item_id = intent.getIntExtra("id", 0);

        Response.Listener<String> listener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                try {
                    String status = jsonObject.getString("status");
                    System.out.println(jsonObject.toString());
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
                                        Drawable drawable  = getResources().getDrawable(R.drawable.no_image);
                                        imgView.setImageDrawable(drawable);
                                        imageFlipper.addView(imgView);
                                    }

                                    imageFlipper.setFlipInterval( 2000 ); //5s intervals
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
