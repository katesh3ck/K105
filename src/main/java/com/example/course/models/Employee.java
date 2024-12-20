package com.example.course.models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Employee {
    private final IntegerProperty id; // Уникальный идентификатор
    private final StringProperty firstName; // Имя
    private final StringProperty lastName; // Фамилия
    private final StringProperty departmentName; // Отдел
    private final StringProperty position; // Должность
    private final DoubleProperty salary; // Зарплата
    private final DoubleProperty bonus; // Премия
    private final ObjectProperty<LocalDate> hireDate; // Дата приема на работу
    private final BooleanProperty selected; // Выбран для операции
    private int experienceYears; // Годы опыта
    private int experienceMonths; // Месяцы опыта

    /**
     * Конструктор класса Employee
     *
     * @param id              Уникальный идентификатор
     * @param firstName       Имя
     * @param lastName        Фамилия
     * @param departmentName  Название отдела
     * @param position        Должность
     * @param salary          Зарплата
     * @param hireDate        Дата приема на работу
     * @param experienceYears Опыт работы в годах
     * @param experienceMonths Опыт работы в месяцах
     */
    public Employee(int id, String firstName, String lastName, String departmentName, String position,
                    double salary, LocalDate hireDate, int experienceYears, int experienceMonths) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.departmentName = new SimpleStringProperty(departmentName);
        this.position = new SimpleStringProperty(position);
        this.salary = new SimpleDoubleProperty(salary);
        this.bonus = new SimpleDoubleProperty(0.0); // Бонус по умолчанию 0
        this.selected = new SimpleBooleanProperty(false); // Не выбран по умолчанию
        this.hireDate = new SimpleObjectProperty<>(hireDate);
        this.experienceYears = experienceYears;
        this.experienceMonths = experienceMonths;
    }

    // Геттеры и сеттеры для всех полей

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
