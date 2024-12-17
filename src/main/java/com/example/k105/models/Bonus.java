package com.example.k105.models;

public class Bonus {
    private int year;
    private double bonusAmount;

    public Bonus(int year, double bonusAmount) {
        this.year = year;
        this.bonusAmount = bonusAmount;
    }

    // Геттеры и сеттеры
    public int getYear() { return year; }
    public double getBonusAmount() { return bonusAmount; }
}
