package com.example.myapplication.modal;

public class User {
    public String name;
    public String phone;
    public String imageUrl;
    public double height;
    public double weight;
    public String emergencyContact;
    public String bloodType;
    public String location;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String phone, String imageUrl, double height, double weight, String emergencyContact, String bloodType, String location) {
        this.name = name;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.height = height;
        this.weight = weight;
        this.emergencyContact = emergencyContact;
        this.bloodType = bloodType;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

