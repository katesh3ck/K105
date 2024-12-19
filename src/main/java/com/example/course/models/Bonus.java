package com.example.course.models;

public class Bonus {
    private int employeeId; // ID сотрудника
    private int year;       // Год премии
    private double bonusAmount; // Сумма премии

    // Конструктор
    public Bonus(int employeeId, int year, double bonusAmount) {
        this.employeeId = employeeId;
        this.year = year;
        this.bonusAmount = bonusAmount;
    }

    // Геттеры
    public int getEmployeeId() {
        return employeeId;
    }

    public int getYear() {
        return year;
    }

    public double getBonusAmount() {
        return bonusAmount;
    }

    // Сеттеры (если нужны)
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setBonusAmount(double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }
}
