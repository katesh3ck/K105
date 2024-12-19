package com.example.course.models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Employee {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty departmentName;
    private final DoubleProperty salary;
    private final DoubleProperty bonus;
    private final ObjectProperty<LocalDate> hireDate;

    public Employee(int id, String name, String departmentName, double salary, LocalDate hireDate) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.departmentName = new SimpleStringProperty(departmentName);
        this.salary = new SimpleDoubleProperty(salary);
        this.bonus = new SimpleDoubleProperty(0.0);
        this.hireDate = new SimpleObjectProperty<>(hireDate);
    }

    // Геттеры и сеттеры для JavaFX Properties
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDepartmentName() {
        return departmentName.get();
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName.set(departmentName);
    }

    public StringProperty departmentNameProperty() {
        return departmentName;
    }

    public double getSalary() {
        return salary.get();
    }

    public void setSalary(double salary) {
        this.salary.set(salary);
    }

    public DoubleProperty salaryProperty() {
        return salary;
    }

    public double getBonus() {
        return bonus.get();
    }

    public void setBonus(double bonus) {
        this.bonus.set(bonus);
    }

    public DoubleProperty bonusProperty() {
        return bonus;
    }

    public LocalDate getHireDate() {
        return hireDate.get();
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate.set(hireDate);
    }

    public ObjectProperty<LocalDate> hireDateProperty() {
        return hireDate;
    }
}
