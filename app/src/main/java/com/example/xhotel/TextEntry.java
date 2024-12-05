package com.example.xhotel;

public class TextEntry {
    private String text;
    private String imageUrl;
    private String price; // Example additional field
    private String numberOfRooms; // Example additional field
    private String numberOfPeople; // Example additional field
    private String numberOfBeds; // Example additional field

    // Default constructor for Firebase
    public TextEntry() {
    }

    // Constructor with all fields
    public TextEntry(String text, String author, String imageUrl, String price, String numberOfRooms,
                     String numberOfPeople, String numberOfBeds) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.price = price;
        this.numberOfRooms = numberOfRooms;
        this.numberOfPeople = numberOfPeople;
        this.numberOfBeds = numberOfBeds;
    }

    // Getters for all fields
    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public String getNumberOfPeople() {
        return numberOfPeople;
    }

    public String getNumberOfBeds() {
        return numberOfBeds;
    }

    // Setters for all fields (added missing setter methods)
    public void setText(String text) {
        this.text = text;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public void setNumberOfPeople(String numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public void setNumberOfBeds(String numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }
}