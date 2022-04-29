package com.example.lareclame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String nm = sh.getString("username", "");

        final TextView tv_username = (TextView) findViewById(R.id.username);
        tv_username.setText(nm);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.ic_profile);
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


}
