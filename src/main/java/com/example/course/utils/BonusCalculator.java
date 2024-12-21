package com.example.course.utils;

import com.example.course.models.Employee;

public class BonusCalculator {

    public double calculateBonus(Employee employee) {
        double baseBonus = employee.getSalary() * 0.07; // Базовая премия 7%

        // Коэффициент стажа
        int experienceYears = employee.getExperienceYears();
        double experienceBonus = (experienceYears / 2) * employee.getSalary() * 0.02; // 2% за каждые 2 года

        // Коэффициент производительности
        double performanceCoefficient;
        switch (employee.getPerformance()) {
            case "низкий уровень":
                performanceCoefficient = -0.01; // уменьшение на 1%
                break;
            case "средний уровень":
                performanceCoefficient = 0.00; // без изменений
                break;
            case "высокий уровень":
                performanceCoefficient = 0.02; // увеличение на 2%
                break;
            default:
                performanceCoefficient = 0.00; // на случай, если значение не выбрано
                break;
        }
        double performanceBonus = employee.getSalary() * performanceCoefficient;

        // Коэффициент компенсации
        double nightWorkBonus = employee.getNightHours() * employee.getSalary() * 0.002; // 0.2% за каждый час ночной работы
        double holidayWorkBonus = employee.getHolidayHours() * employee.getSalary() * 0.001; // 0.1% за праздники

        // Общая премия
        return baseBonus + experienceBonus + performanceBonus + nightWorkBonus + holidayWorkBonus;
    }
}
