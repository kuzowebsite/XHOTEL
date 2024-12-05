package com.example.xhotel;

public class OrderModel {

    public String phoneNumber;
    public String text;
    public String price;
    public int days;
    public int hours;
    public int minutes;
    public int seconds;

    public OrderModel(String phoneNumber, String text, String price, int days, int hours, int minutes, int seconds) {
        this.phoneNumber = phoneNumber;
        this.text = text;
        this.price = price;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    // Getters and Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getText() {
        return text;
    }

    public String getPrice() {
        return price;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }
}