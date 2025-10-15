package com.example.eduviast;

public class OnboardItem {
    int image;
    String title, description, subtext, buttonText;

    public OnboardItem(int image, String title, String description, String subtext, String buttonText) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.subtext = subtext;
        this.buttonText = buttonText;
    }
}