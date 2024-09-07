package com.example.myapplication.modal;


public class Disease {
    private String name;
    private String symptoms;
    private String whenToSeeDoctor;

    public Disease(String name, String symptoms, String whenToSeeDoctor) {
        this.name = name;
        this.symptoms = symptoms;
        this.whenToSeeDoctor = whenToSeeDoctor;
    }

    public String getName() {
        return name;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getWhenToSeeDoctor() {
        return whenToSeeDoctor;
    }
}

