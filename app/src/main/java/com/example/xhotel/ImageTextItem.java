package com.example.xhotel;

public class ImageTextItem {
    private String key;
    private String imageUrl;
    private String text;
    private String price;
    private String numberOfRooms;
    private String numberOfPeople;
    private String numberOfBeds;

    // Default constructor required for Firebase
    public ImageTextItem() {}

    public ImageTextItem(String imageUrl, String text, String price, String numberOfRooms, String numberOfPeople, String numberOfBeds) {
        this.imageUrl = imageUrl;
        this.text = text;
        this.price = price;
        this.numberOfRooms = numberOfRooms;
        this.numberOfPeople = numberOfPeople;
        this.numberOfBeds = numberOfBeds;
    }

    // Getters and setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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
}