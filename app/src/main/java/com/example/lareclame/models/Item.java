package com.example.lareclame.models;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.ArrayAdapter;

import androidx.annotation.RequiresApi;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.lareclame.requests.ImageRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Item {
    private final int id;
    private final String title;
    private final String description;
    private final String date;
    private final String price_type;
    private final int price;
    private User owner;
    private final ArrayList<Bitmap> pictures;

    public Item(int id, String title, String description, String date, String price_type, int price, ArrayList<Bitmap> pictures, User owner) throws ParseException {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = formatDate(date);
        String price_capitalized = price_type.substring(0, 1).toUpperCase() + price_type.substring(1);
        this.price_type = price_capitalized;
        this.price = price;
        this.pictures = pictures;
        this.owner = owner;
    }

    public String formatDate(String dateString) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = simpleDateFormat.parse(dateString);
        simpleDateFormat.applyPattern("hh:mm dd-MM-yyyy");
        return simpleDateFormat.format(date);
    }

    public ArrayList<Bitmap> getPictures() {
        return pictures;
    }

    public int getId() {
        return id;
    }

    public String getPrice_type() {
        return price_type;
    }

    public int getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public User getOwner() {
        return owner;
    }
}
