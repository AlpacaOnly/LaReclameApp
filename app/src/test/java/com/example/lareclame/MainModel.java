package com.example.lareclame;

public class MainModel {
    Integer categoryLogo;
    String categoryName;

    public MainModel(Integer categoryLogo, String categoryName){
        this.categoryLogo = categoryLogo;
        this.categoryName = categoryName;
    }

    public Integer getCategoryLogo() {
        return categoryLogo;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
