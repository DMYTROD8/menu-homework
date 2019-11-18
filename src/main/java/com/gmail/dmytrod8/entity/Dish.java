package com.gmail.dmytrod8.entity;

import javax.persistence.*;

@Entity
@Table(name = "Dishes")
public class Dish {
    @Id
    @GeneratedValue
    private long id;

    private String title;
    private int weight; //in grams
    private double price;
    private boolean discount;
    private int discountAmount;

    public Dish(String title, int weight, double price, int discountAmount) {
        this.title = title;
        this.weight = weight;
        this.price = price;
        this.discountAmount = discountAmount;
        if (discountAmount == 0) {
            this.discount = false;
        } else {
            this.discount = true;
        }
    }

    public Dish() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", weight=" + weight +
                ", price=" + price +
                ", discount=" + discount +
                ", discountAmount=" + discountAmount +
                '}';
    }
}

