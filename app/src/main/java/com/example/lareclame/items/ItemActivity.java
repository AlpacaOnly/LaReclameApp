package com.example.lareclame.items;

import  android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lareclame.MainActivity;
import com.example.lareclame.ProfileActivity;
import com.example.lareclame.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ItemActivity extends AppCompatActivity {
    TextView date;
    TextView title;
    TextView description;
    TextView price_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        title = findViewById(R.id.item_title);
        date = findViewById(R.id.item_date);
        description = findViewById(R.id.item_description);
        price_type = findViewById(R.id.tv_price_type);
        Intent intent = getIntent();

        title.setText(intent.getStringExtra("title"));
        date.setText(intent.getStringExtra("date"));
        description.setText(intent.getStringExtra("description"));
        price_type.setText(intent.getStringExtra("price_type"));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
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
                    return true;
            }
            return false;
        });
    }
}
