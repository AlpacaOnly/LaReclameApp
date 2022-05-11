package com.example.lareclame.items;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Item {
    private String title;
    private String description;
    private String date;

    public Item(String title, String description, String date) throws ParseException {
        this.title = title;
        this.description = description;
        this.date = formatDate(date);
        System.out.println(this.date);
    }

    public String formatDate(String dateString) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = simpleDateFormat.parse(dateString);
        simpleDateFormat.applyPattern("hh:mm dd-MM-yyyy");
        return simpleDateFormat.format(date);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
