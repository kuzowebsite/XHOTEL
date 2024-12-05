package com.example.xhotel;

public class TextModel {
    private String text;
    private String imageUrl;
    private String price;        // Шинэ талбар үнэ
    private String numberOfRooms; // Шинэ талбар өрөөний тоо
    private String numberOfPeople; // Шинэ талбар хүний тоо
    private String numberOfBeds;   // Шинэ талбар орны тоо

    public TextModel() {
        // Үндсэн конструктор
    }

    public TextModel(String text, String imageUrl, String price, String numberOfRooms, String numberOfPeople, String numberOfBeds) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.price = price;
        this.numberOfRooms = numberOfRooms;
        this.numberOfPeople = numberOfPeople;
        this.numberOfBeds = numberOfBeds;
    }

    // Геттер, Сеттер методууд
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
