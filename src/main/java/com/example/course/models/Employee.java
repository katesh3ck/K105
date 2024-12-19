package com.example.course.models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Employee {
    private final IntegerProperty id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty departmentName;
    private final DoubleProperty salary;
    private final DoubleProperty bonus;
    private final ObjectProperty<LocalDate> hireDate;

    public Employee(int id, String firstName, String lastName, String departmentName, double salary, LocalDate hireDate) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.departmentName = new SimpleStringProperty(departmentName); // Инициализация departmentName
        this.salary = new SimpleDoubleProperty(salary);
        this.bonus = new SimpleDoubleProperty(0.0); // Инициализируем бонус как 0
        this.hireDate = new SimpleObjectProperty<>(hireDate);
    }

    // Геттеры и сеттеры для JavaFX Properties
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
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
