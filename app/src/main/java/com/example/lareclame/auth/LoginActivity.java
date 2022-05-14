package com.example.lareclame.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.lareclame.ProfileActivity;
import com.example.lareclame.R;
import com.example.lareclame.requests.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sh = getSharedPreferences("Login", MODE_PRIVATE);
        boolean isUserLogin = sh.getBoolean("isUserLogin", false);

        if (isUserLogin) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sh = getSharedPreferences("Login", MODE_PRIVATE);
        try {
            JSONObject user = new JSONObject(sh.getString("user", ""));
            username.setText(user.getString("username"));
            password.setText(user.getString("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void no_account_onClick (View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onLoginClick(View view) {
        final String Username = username.getText().toString();
        final String Password = password.getText().toString();

        Response.Listener <String> listener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status =jsonObject.getString("status");

                if (status.equals("ok")) {
                    JSONObject user = jsonObject.getJSONObject("user");

                    SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("user", user.toString());
                    editor.putBoolean("isUserLogin", true);
                    editor.apply();

                    Intent intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);

                } else {
                    String error = jsonObject.getString("error");
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(error).setNegativeButton("Retry", null).create().show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        LoginRequest loginRequest =new LoginRequest(Username, Password, listener, System.out::println);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(loginRequest);
    }

}
