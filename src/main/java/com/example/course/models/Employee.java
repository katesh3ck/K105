package com.example.course.models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Employee {
    private final IntegerProperty id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty departmentName;
    private final StringProperty position;
    private final DoubleProperty salary;
    private final DoubleProperty bonus;
    private final ObjectProperty<LocalDate> hireDate;
    private final BooleanProperty selected;
    private final StringProperty compensationType;
    private final IntegerProperty compensationHours; // Общие часы
    private final IntegerProperty nightHours;        // Часы ночной работы
    private final IntegerProperty holidayHours;      // Часы работы в праздники
    private final SimpleStringProperty performance;
    private int experienceYears;
    private int experienceMonths;

    public Employee(int id, String firstName, String lastName, String departmentName, String position,
                    double salary, LocalDate hireDate, int experienceYears, int experienceMonths) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.departmentName = new SimpleStringProperty(departmentName);
        this.position = new SimpleStringProperty(position);
        this.salary = new SimpleDoubleProperty(salary);
        this.bonus = new SimpleDoubleProperty(0.0);
        this.selected = new SimpleBooleanProperty(false);
        this.hireDate = new SimpleObjectProperty<>(hireDate);
        this.experienceYears = experienceYears;
        this.experienceMonths = experienceMonths;
        this.compensationType = new SimpleStringProperty("Не выбрано");
        this.compensationHours = new SimpleIntegerProperty(0);
        this.nightHours = new SimpleIntegerProperty(0);
        this.holidayHours = new SimpleIntegerProperty(0);
        this.performance = new SimpleStringProperty("средний уровень"); // Значение по умолчанию
    }


    // Конструктор для новых сотрудников
    public Employee(String firstName, String lastName, String departmentName, String position,
                    double salary, LocalDate hireDate, int experienceYears, int experienceMonths) {
        this.id = new SimpleIntegerProperty();
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.departmentName = new SimpleStringProperty(departmentName);
        this.position = new SimpleStringProperty(position);
        this.salary = new SimpleDoubleProperty(salary);
        this.bonus = new SimpleDoubleProperty(0.0);
        this.selected = new SimpleBooleanProperty(false);
        this.hireDate = new SimpleObjectProperty<>(hireDate);
        this.experienceYears = experienceYears;
        this.experienceMonths = experienceMonths;
        this.compensationType = new SimpleStringProperty("Не выбрано");
        this.compensationHours = new SimpleIntegerProperty(0);
        this.nightHours = new SimpleIntegerProperty(0);
        this.holidayHours = new SimpleIntegerProperty(0);
        this.performance = new SimpleStringProperty("средний уровень");
    }


    // Геттеры и сеттеры для новых полей

    public String getPerformance() { return performance.get(); }
    public SimpleStringProperty performanceProperty() { return performance; }
    public void setPerformance(String performance) { this.performance.set(performance); }

    public int getNightHours() {
        return nightHours.get();
    }

    public void setNightHours(int nightHours) {
        this.nightHours.set(nightHours);
    }

    public IntegerProperty nightHoursProperty() {
        return nightHours;
    }

    public int getHolidayHours() {
        return holidayHours.get();
    }

    public void setHolidayHours(int holidayHours) {
        this.holidayHours.set(holidayHours);
    }

    public IntegerProperty holidayHoursProperty() {
        return holidayHours;
    }


    // Геттеры и сеттеры для всех полей

    public String getCompensationType() {
        return compensationType.get();
    }

    public void setCompensationType(String compensationType) {
        this.compensationType.set(compensationType);
    }

    public StringProperty compensationTypeProperty() {
        return compensationType;
    }

    public int getCompensationHours() {
        return compensationHours.get();
    }

    public void setCompensationHours(int compensationHours) {
        this.compensationHours.set(compensationHours);
    }

    public IntegerProperty compensationHoursProperty() {
        return compensationHours;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
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

    public String getPosition() {
        return position.get();
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public StringProperty positionProperty() {
        return position;
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

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public int getExperienceMonths() {
        return experienceMonths;
    }

    public void setExperienceMonths(int experienceMonths) {
        this.experienceMonths = experienceMonths;
    }
}
