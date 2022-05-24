package com.example.lareclame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.lareclame.auth.LoginActivity;
import com.example.lareclame.items.CreateItemActivity;
import com.example.lareclame.requests.UpdateUserRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity {
    EditText et_username;
    EditText et_password;
    EditText et_bio;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_bio = findViewById(R.id.et_bio);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.ic_settings);
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
            }
            return false;
        });

    }

    public void update_info (View view) {
        final String username = et_username.getText().toString();
        final String password = et_password.getText().toString();
        final String bio = et_bio.getText().toString();

        Response.Listener<String> listener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                System.out.println(status);
                if (status.equals("ok")) {
                    SharedPreferences sh =  getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sh.edit();
                    try {
                        JSONObject user = new JSONObject(sh.getString("user", ""));
                        if (!username.equals("")) user.put("username", username);
                        if (!bio.equals("")) user.put("bio", bio);
                        editor.putString("user", user.toString());
                        editor.apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setMessage("Info was updated").setPositiveButton("ok", null).show();
                } else {
                    String error = jsonObject.getString("error");
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setMessage(error).setNegativeButton("Retry", null).create().show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        SharedPreferences sh =  getSharedPreferences("Login", MODE_PRIVATE);
        int user_id = 0;
        try {
            JSONObject user = new JSONObject(sh.getString("user", ""));
            user_id = user.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        UpdateUserRequest updateUserRequest = new UpdateUserRequest(user_id, username, password, bio, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(updateUserRequest);

    }

    public void LogOut(View view) {
        SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("isUserLogin");
        editor.commit();

        finish();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
