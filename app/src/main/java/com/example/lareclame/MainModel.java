package com.example.lareclame;

public class MainModel {
    Integer categLogo;
    String categName;

    public MainModel(Integer categLogo, String categName) {
        this.categLogo = categLogo;
        this.categName = categName;
    }

    public Integer getCategLogo() {
        return categLogo;
    }

    public String getCategName() {
        return categName;
    }
}
