package com.example.lareclame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Item> itemsList;
    private RecyclerView recyclerView;

    ListView listview;
    String[] name = {"Syryk", "Ayazzzzzzzzz", "DisaLox", "Sabinoid", "Ruslanidze"};

    ArrayAdapter<String> arrayAdapter;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView);
        itemsList= new ArrayList<>();

        setItemInfo();
        setAdapter();

//        listview = findViewById(R.id.listview);
//        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name);
//        listview.setAdapter(arrayAdapter);

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
//        bottomNavigationView.setSelectedItemId(R.id.ic_home);
//
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.ic_home:
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    return true;
//                case R.id.ic_create_announcement:
//                    startActivity(new Intent(getApplicationContext(), CreateItemActivity.class));
//                    return true;
//                case R.id.ic_settings:
//                    return true;
//                case R.id.ic_profile:
//                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//            }
//            return false;
//        });
    }

    private void setAdapter() {
        recyclerAdapter adapter =new recyclerAdapter(itemsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setItemInfo() {
        itemsList.add(new Item("Курсы от Диаса"));
        itemsList.add(new Item("Продам Софию"));
        itemsList.add(new Item("Сырым Лох"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type Here to Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                arrayAdapter.getFilter().filter(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void More_details(View view) {
        Intent intent = new Intent(this, ItemActivity.class);
        startActivity(intent);
    }
}