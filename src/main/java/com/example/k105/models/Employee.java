package com.example.k105.models;

import java.time.LocalDate;

public class Employee {
    private int id;
    private String name;
    private String department;
    private double salary;
    private double bonus;
    private LocalDate hireDate;

    public Employee(int id, String name, String department, LocalDate hireDate) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.hireDate = hireDate;
    }

    public Employee(int id, String name, String department, double salary, double bonus, LocalDate hireDate) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.bonus = bonus;
        this.hireDate = hireDate;
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

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public void setSalary(double salary) { this.salary = salary; }

    public LocalDate getHireDate() {
        return hireDate;
    }
}
