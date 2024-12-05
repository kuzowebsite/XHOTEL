package com.example.xhotel;

public class Reservation {
    private String imageUrl;
    private String text;
    private String price;
    private String numberOfRooms;
    private String numberOfPeople;
    private String numberOfBeds;
    private String phoneNumber;
    private String numberOfNights;

    // Default constructor required for Firebase
    public Reservation() {}

    public Reservation(ImageTextItem item, String phoneNumber, String numberOfNights) {
        this.imageUrl = item.getImageUrl();
        this.text = item.getText();
        this.price = item.getPrice();
        this.numberOfRooms = item.getNumberOfRooms();
        this.numberOfPeople = item.getNumberOfPeople();
        this.numberOfBeds = item.getNumberOfBeds();
        this.phoneNumber = phoneNumber;
        this.numberOfNights = numberOfNights;
    }

    // Getters and setters
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(String numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(String numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(String numberOfNights) {
        this.numberOfNights = numberOfNights;
    }
}