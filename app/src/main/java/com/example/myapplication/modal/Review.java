package com.example.myapplication.modal;

public class Review {
    private String feedback;
    private float rating;

    public Review() {
    }

    public Review(String feedback, float rating) {
        this.feedback = feedback;
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public float getRating() {
        return rating;
    }
}