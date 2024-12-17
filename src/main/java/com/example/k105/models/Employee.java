package com.example.k105.models;

import java.time.LocalDate;

public class Employee {
    private int id;
    private String name;
    private String department;
    private double bonus;
    private LocalDate hireDate;

    // Конструктор для hireDate
    public Employee(int id, String name, String department, LocalDate hireDate) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.hireDate = hireDate;
    }

    // Конструктор для бонусов
    public Employee(int id, String name, String department, double bonus) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.bonus = bonus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }
}
