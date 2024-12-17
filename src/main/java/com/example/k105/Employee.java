package com.example.k105;

public class Employee {
    private int id;
    private String name;
    private String department;
    private double bonus;

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
}
