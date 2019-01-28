package com.fffrowies.sbadmin.Model;

public class Product {
    private String Name, Image, Description, Price, Discount, CategoryId;

    public Product() {
    }

    public Product(String name, String image, String description, String price, String discount, String categoryId) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        Discount = discount;
        CategoryId = categoryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }
}
