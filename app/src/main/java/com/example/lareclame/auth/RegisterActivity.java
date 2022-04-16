package com.example.lareclame.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lareclame.R;

public class RegisterActivity extends AppCompatActivity {

    EditText email;
    EditText username;
    EditText password;
    Button sign_up;
    TextView sign_in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.sign_up_email_address);
        username = findViewById(R.id.sign_up_username);
        password = findViewById(R.id.sign_up_password);
        sign_up = findViewById(R.id.sign_up_button);

        sign_up.setOnClickListener(view -> checkIfDataEntered());
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
        if (isEmpty(username)) {
            Toast toast=Toast.makeText(this, "Username is required!", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (!isEmail(email)) {
            email.setError("Enter valid email");
        }
    }
}
