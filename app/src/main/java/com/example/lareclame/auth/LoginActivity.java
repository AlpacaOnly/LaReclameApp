package com.example.lareclame.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.lareclame.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=findViewById(R.id.email_address);
        password=findViewById(R.id.password);
        sign_in=findViewById(R.id.sign_in_button);
    }

    public void onLoginClick(View view) {
        final String Username = username.getText().toString();
        final String Password = password.getText().toString();

        Response.Listener <String> listener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status =jsonObject.getString("status");

                if (status.equals("ok")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Successfully logged in").setNegativeButton("Retry", null).create().show();
                } else {
                    String error = jsonObject.getString("error");
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(error).setNegativeButton("Retry", null).create().show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        LoginRequest loginRequest =new LoginRequest(Username, Password, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(loginRequest);


    }
}
