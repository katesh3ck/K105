package com.example.course.models;

import java.time.LocalDate;

public class Employee {
    private int id;
    private String name;
    private String department;
    private double salary;
    private double bonus;
    private LocalDate hireDate;

    public Employee(int id, String name, String department, double salary, double bonus, LocalDate hireDate) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.bonus = bonus;
        this.hireDate = hireDate;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }
    public double getBonus() { return bonus; }
    public LocalDate getHireDate() { return hireDate; }

    public void setBonus(double bonus) { this.bonus = bonus; }
}
