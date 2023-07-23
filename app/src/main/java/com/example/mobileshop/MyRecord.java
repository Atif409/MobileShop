package com.example.mobileshop;

@SuppressWarnings("WeakerAccess")
public class MyRecord {

    public MyRecord(String name, String idCard, String company, String model, String price) {
        this.name = name;
        this.idCard = idCard;
        this.company = company;
        this.model = model;
        this.price = price;

    }

    public String getName() {
        return name;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getCompany() {
        return company;
    }

    public String getModel() {
        return model;
    }

    public String getPrice() {
        return price;
    }
    private String name;
    private String idCard;
    private String company;
    private String model;
    private String price;


}

