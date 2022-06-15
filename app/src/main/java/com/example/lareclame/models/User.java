package com.example.lareclame.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class User {
    private final int id;
    private final String username;
    private String image;

    public User(int id, String username, String image) {
        this.id = id;
        this.username = username;
        try {
            this.image = URLDecoder.decode(image, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getImage() {
        return image;
    }
}
