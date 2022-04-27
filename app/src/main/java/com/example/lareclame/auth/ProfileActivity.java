package com.example.lareclame.auth;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lareclame.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String nm = sh.getString("username", "");

        final TextView tv_username = (TextView) findViewById(R.id.username);
        tv_username.setText(nm);


    }


}
