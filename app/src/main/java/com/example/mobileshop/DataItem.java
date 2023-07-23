package com.example.mobileshop;

public class DataItem {
    private String name;
    private String idCard;
    private String company;
    private String model;
    private String price;

    public DataItem() {
        // Default constructor required for Firebase
    }

    public DataItem(String name, String idCard, String company, String model, String price) {
        this.name = name;
        this.idCard = idCard;
        this.company = company;
        this.model = model;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
