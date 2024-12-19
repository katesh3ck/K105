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
///!!!!!
    public StringProperty nameProperty() {
        return name;
    }
///!!!!!
    public StringProperty departmentNameProperty() {
        return departmentName;
    }

    public double getSalary() {
        return salary.get();
    }

    public void setBonus(double bonus) {
        this.bonus.set(bonus);
    }

    public DoubleProperty bonusProperty() {
        return bonus;
    }

    public ObjectProperty<LocalDate> hireDateProperty() {
        return hireDate;
    }
}
