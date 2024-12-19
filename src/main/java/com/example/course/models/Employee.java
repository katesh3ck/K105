package com.example.course.models;

import java.time.LocalDate;

public class Employee {
    private int id;
    private String name;
    private String departmentName;
    private double salary;
    private double bonus;
    private LocalDate hireDate;

    public Employee(int id, String name, String departmentName, double salary, LocalDate hireDate) {
        this.id = id;
        this.name = name;
        this.departmentName = departmentName;
        this.salary = salary;
        this.hireDate = hireDate;
        this.bonus = 0.0; // Установим бонус в 0 при создании объекта
    }


    // Геттеры и сеттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDepartmentName() { return departmentName; }
    public double getSalary() { return salary; }
    public double getBonus() { return bonus; }
    public LocalDate getHireDate() { return hireDate; }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBonus(double bonus) { this.bonus = bonus; }
}
