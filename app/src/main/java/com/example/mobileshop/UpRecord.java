package com.example.mobileshop;

public class UpRecord {
    private String company;
    private String model;
    private String name;
    private String idCard;
    private String price;

    public UpRecord() {
        // Empty constructor required for Firebase
    }

    public UpRecord(String company, String model, String name, String idCard, String price) {
        this.company = company;
        this.model = model;
        this.name = name;
        this.idCard = idCard;
        this.price = price;
    }

    public String getCompany() {
        return company;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getPrice() {
        return price;
    }
}
