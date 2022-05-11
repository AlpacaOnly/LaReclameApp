package com.example.lareclame.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    EditText et_email;
    EditText et_username;
    EditText et_password;
    EditText et_password_re_enter;
    Button sign_up;
    TextView sign_in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_email = findViewById(R.id.sign_up_email_address);
        et_username = findViewById(R.id.sign_up_username);
        et_password = findViewById(R.id.sign_up_password);
        et_password_re_enter = findViewById(R.id.password_re_enter);
        sign_up = findViewById(R.id.sign_up_button);
    }

    public void onRegisterClick(View view) {
        final String username = et_username.getText().toString();
        final String email = et_email.getText().toString();
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

        Response.Listener <String> listener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status =jsonObject.getString("status");

                if (status.equals("ok")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Successfully registered").show();
                } else {
                    String error = jsonObject.getString("error");
                    if (error.equals("User with such username already exists.")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("User with such username already exists.").setNegativeButton("Retry", null).create().show();
                    }
                    else if (error.equals("User with such email already exists.")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("User with such email already exists.").setNegativeButton("Retry", null).create().show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(username, password, email, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(registerRequest);

    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence c = text.getText().toString();
        return TextUtils.isEmpty(c);
    }

    private void checkIfDataEntered() {
        if (isEmpty(et_username)) {
            Toast toast=Toast.makeText(this, "Username is required!", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (!isEmail(et_email)) {
            et_email.setError("Enter valid email");
        }
    }
}
