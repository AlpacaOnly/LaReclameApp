package com.example.lareclame.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.lareclame.R;
import com.example.lareclame.requests.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText et_barcode;
    EditText et_username;
    EditText et_password;
    EditText et_password_re_enter;
    RelativeLayout loadingPanel;
    Button sign_up;

    private long mLastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_barcode = findViewById(R.id.sign_up_barcode);
        et_username = findViewById(R.id.sign_up_username);
        et_password = findViewById(R.id.sign_up_password);
        et_password_re_enter = findViewById(R.id.password_re_enter);
        sign_up = findViewById(R.id.sign_up_button);
        loadingPanel = findViewById(R.id.loadingPanel);

        loadingPanel.setVisibility(View.GONE);
    }

    public void onRegisterClick(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        final String username = et_username.getText().toString();
        final String barcode = et_barcode.getText().toString();
        final String password = et_password.getText().toString();
        final String password_re_enter = et_password_re_enter.getText().toString();

        if (!password.equals(password_re_enter)) {
            et_password.setError("Passwords doesn't match");
            return;
        }
        if (et_password.length() < 8) {
            et_password.setError("Password should contain at least 8 characters");
            return;
        }

        WindowManager.LayoutParams WMLP = getWindow().getAttributes();
        WMLP.screenBrightness = 0.15F;
        getWindow().setAttributes(WMLP);
        loadingPanel.setVisibility(View.VISIBLE);

        Response.Listener<String> listener = response -> {
            try {
                loadingPanel.setVisibility(View.GONE);

                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");

                if (status.equals("ok")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Successfully registered");
                    builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    });
                    builder.create().show();
                } else {
                    String error = jsonObject.getString("error");
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage(error).setNegativeButton("Retry", null).create().show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(username, password, barcode, listener, System.out::println);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(registerRequest);

    }
}
