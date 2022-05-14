package com.example.lareclame.items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.lareclame.MainActivity;
import com.example.lareclame.ProfileActivity;
import com.example.lareclame.R;
import com.example.lareclame.SettingsActivity;
import com.example.lareclame.auth.LoginActivity;
import com.example.lareclame.auth.RegisterActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText et_title;
    EditText et_body;
    Spinner spinner;

    ImageView objectImageView;
    private static final int PICK_IMAGE_REQUEST=100;
    private Uri imageFilePath;
    private Bitmap imageToStore;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        et_title = findViewById(R.id.title);
        et_body = findViewById(R.id.body);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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

        Response.Listener <String> listener = response -> {
            try {
                JSONObject jsonObject   = new JSONObject(response);
                String status =jsonObject.getString("status");
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
        String username = sh.getString("username", "");

        ItemRequest itemRequest = new ItemRequest(username, title, body, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        RequestQueue itemQueue = Volley.newRequestQueue(this);
        itemQueue.add(itemRequest);
    }

    public void choose_image(View view) {
        try {
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*");

            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent, PICK_IMAGE_REQUEST);
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
                imageFilePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);

                System.out.println(imageFilePath);
                objectImageView.setImageBitmap(imageToStore);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
