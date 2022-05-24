package com.example.lareclame.items;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.lareclame.MainActivity;
import com.example.lareclame.ProfileActivity;
import com.example.lareclame.R;
import com.example.lareclame.SettingsActivity;
import com.example.lareclame.requests.ItemRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreateItemActivity extends AppCompatActivity{

    final int REQUEST_EXTERNAL_STORAGE = 100;
    EditText et_title;
    EditText et_body;
    EditText et_price;
    Spinner spinner_category;
    Spinner spinner_price;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        et_price = findViewById(R.id.et_price);
        et_title = findViewById(R.id.title);
        et_body = findViewById(R.id.body);
        spinner_category = findViewById(R.id.spinner);
        spinner_price = findViewById(R.id.spinner_price);

        final ArrayList<String> list = new ArrayList<String>();
        list.add("Fixed");
        list.add("Free");
        list.add("Negotiable");

        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_price.setAdapter(adp1);

        ArrayList<String> arraySpinner = new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences("Categories", MODE_PRIVATE);
        try {
            JSONArray categories = new JSONArray(preferences.getString("categories", ""));
            for (int i = 0; i < categories.length(); i++) {
                JSONObject category = categories.getJSONObject(i);
                arraySpinner.add(category.getString("category_name"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.ic_create_announcement);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.ic_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                case R.id.ic_create_announcement:
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

    public void create_item(View view) {
        final String title = et_title.getText().toString();
        final String body = et_body.getText().toString();
        final String spinner_text = spinner_category.getSelectedItem().toString();
        final String price_type = spinner_price.getSelectedItem().toString().toLowerCase();
        final int price = Integer.parseInt(et_price.getText().toString());


        int category_id = -1;
        SharedPreferences preferences = getSharedPreferences("Categories", MODE_PRIVATE);
        try {
            JSONArray categories = new JSONArray(preferences.getString("categories", ""));
            for (int i = 0; i < categories.length(); i++) {
                JSONObject category = categories.getJSONObject(i);
                String category_name = category.getString("category_name");
                if (category_name.equals(spinner_text)) {
                    category_id = category.getInt("id");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response.Listener<String> listener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                System.out.println(status);
                if (status.equals("ok")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateItemActivity.this);
                    builder.setMessage("Successfully added").setPositiveButton("ok", null).show();
                } else {
                    String error = jsonObject.getString("error");
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateItemActivity.this);
                    builder.setMessage(error).setNegativeButton("Retry", null).create().show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };


        SharedPreferences sh = getSharedPreferences("Login", MODE_PRIVATE);
        int user_id = -1;
        try {
            JSONObject user = new JSONObject(sh.getString("user", ""));
            user_id = user.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ItemRequest itemRequest = new ItemRequest(user_id, category_id, title, body, price_type, price, listener, System.out::println);

        RequestQueue itemQueue = Volley.newRequestQueue(this);
        itemQueue.add(itemRequest);
    }

    public void choose_image(View view) {
        if (ActivityCompat.checkSelfPermission(CreateItemActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreateItemActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
//                    return;
        } else {
            launchGalleryIntent();
        }
    }

    public void launchGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    launchGalleryIntent();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXTERNAL_STORAGE  && resultCode == RESULT_OK) {
            final TextView file_count = findViewById(R.id.file_count);
            final List<Bitmap> bitmaps = new ArrayList<>();
            ClipData clipData = data.getClipData();

            if (clipData != null) {
                //multiple images selected
                file_count.setText(clipData.getItemCount()+" images uploaded");
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    Log.d("URI", imageUri.toString());
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //single image selected
                file_count.setText("1 image uploaded");
                Uri imageUri = data.getData();
                Log.d("URI", imageUri.toString());
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.add(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
