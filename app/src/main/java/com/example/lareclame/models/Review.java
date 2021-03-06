package com.example.lareclame.models;

public class Review {
    private int id;
    private int item_id;
    private int user_id;
    private String title;
    private String description;
    private int rating;
    private String created;
    private User author;

    public Review(int id, int item_id, int user_id, String title, String description, int rating, String created, User author) {
        this.id = id;
        this.item_id = item_id;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.created = created;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public int getItem_id() {
        return item_id;
    }

    public int getRating() {
        return rating;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getCreated() {
        return created;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public User getAuthor() {
        return author;
    }
}
