package com.example.course.managers;

import com.example.course.models.Employee;
import com.example.course.dao.EmployeeDAO;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import java.time.LocalDate;
import java.util.Optional;

public class EmployeeManager {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    public void addEmployee(ObservableList<Employee> employeeData) {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Добавление сотрудника");

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField departmentField = new TextField();
        TextField positionField = new TextField();
        TextField salaryField = new TextField();
        TextField hireDateField = new TextField();
        TextField experienceYearsField = new TextField();
        TextField experienceMonthsField = new TextField();
        TextField performanceRatingField = new TextField();

        grid.add(new Label("Имя:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Фамилия:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Отдел:"), 0, 2);
        grid.add(departmentField, 1, 2);
        grid.add(new Label("Должность:"), 0, 3);
        grid.add(positionField, 1, 3);
        grid.add(new Label("Зарплата:"), 0, 4);
        grid.add(salaryField, 1, 4);
        grid.add(new Label("Дата найма:"), 0, 5);
        grid.add(hireDateField, 1, 5);
        grid.add(new Label("Стаж (годы):"), 0, 6);
        grid.add(experienceYearsField, 1, 6);
        grid.add(new Label("Стаж (месяцы):"), 0, 7);
        grid.add(experienceMonthsField, 1, 7);
        grid.add(new Label("Производительность:"), 0, 8);
        grid.add(performanceRatingField, 1, 8);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                Employee newEmployee = new Employee(
                        0,
                        firstNameField.getText(),
                        lastNameField.getText(),
                        departmentField.getText(),
                        positionField.getText(),
                        Double.parseDouble(salaryField.getText()),
                        LocalDate.parse(hireDateField.getText()),
                        Integer.parseInt(experienceYearsField.getText()),
                        Integer.parseInt(experienceMonthsField.getText())
                        );
                newEmployee.setPerformance(performanceRatingField.getText());
                employeeDAO.insertEmployee(newEmployee);
                return newEmployee;
            }
            return null;
        });

        Optional<Employee> result = dialog.showAndWait();
        result.ifPresent(employeeData::add);
    }



    public void editEmployee(Employee selectedEmployee, ObservableList<Employee> employeeData) {
        if (selectedEmployee != null) {
            Dialog<Employee> dialog = new Dialog<>();
            dialog.setTitle("Редактирование сотрудника");

            ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField firstNameField = new TextField(selectedEmployee.getFirstName());
            TextField lastNameField = new TextField(selectedEmployee.getLastName());
            TextField departmentField = new TextField(selectedEmployee.getDepartmentName());
            TextField positionField = new TextField(selectedEmployee.getPosition());
            TextField salaryField = new TextField(String.valueOf(selectedEmployee.getSalary()));
            TextField hireDateField = new TextField(String.valueOf(selectedEmployee.getHireDate()));
            TextField experienceYearsField = new TextField(String.valueOf(selectedEmployee.getExperienceYears()));
            TextField experienceMonthsField = new TextField(String.valueOf(selectedEmployee.getExperienceMonths()));
            TextField performanceRatingField = new TextField(selectedEmployee.getPerformance());

            grid.add(new Label("Имя:"), 0, 0);
            grid.add(firstNameField, 1, 0);
            grid.add(new Label("Фамилия:"), 0, 1);
            grid.add(lastNameField, 1, 1);
            grid.add(new Label("Отдел:"), 0, 2);
            grid.add(departmentField, 1, 2);
            grid.add(new Label("Должность:"), 0, 3);
            grid.add(positionField, 1, 3);
            grid.add(new Label("Зарплата:"), 0, 4);
            grid.add(salaryField, 1, 4);
            grid.add(new Label("Дата найма:"), 0, 5);
            grid.add(hireDateField, 1, 5);
            grid.add(new Label("Стаж (годы):"), 0, 6);
            grid.add(experienceYearsField, 1, 6);
            grid.add(new Label("Стаж (месяцы):"), 0, 7);
            grid.add(experienceMonthsField, 1, 7);
            grid.add(new Label("Производительность:"), 0, 8);
            grid.add(performanceRatingField, 1, 8);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    selectedEmployee.setFirstName(firstNameField.getText());
                    selectedEmployee.setLastName(lastNameField.getText());
                    selectedEmployee.setDepartmentName(departmentField.getText());
                    selectedEmployee.setPosition(positionField.getText());
                    selectedEmployee.setSalary(Double.parseDouble(salaryField.getText()));
                    selectedEmployee.setHireDate(LocalDate.parse(hireDateField.getText()));
                    selectedEmployee.setExperienceYears(Integer.parseInt(experienceYearsField.getText()));
                    selectedEmployee.setExperienceMonths(Integer.parseInt(experienceMonthsField.getText()));
                    selectedEmployee.setPerformance(performanceRatingField.getText());
                    return selectedEmployee;
                }
                return null;
            });

            Optional<Employee> result = dialog.showAndWait();
            result.ifPresent(employee -> {
                employeeDAO.updateEmployee(employee);
                employeeData.set(employeeData.indexOf(employee), employee);
            });
        } else {
            showAlert("Пожалуйста, выберите сотрудника для редактирования.");
        }
    }

    public void deleteEmployee(Employee selectedEmployee, ObservableList<Employee> employeeData) {
        System.out.println("Selected Employee ID: " + selectedEmployee.getId()); // Временное сообщение для отладки
        if (selectedEmployee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления");
            alert.setHeaderText("Вы уверены, что хотите удалить выбранного сотрудника?");
            alert.setContentText("Сотрудник будет удален из базы данных.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                employeeDAO.deleteEmployee(selectedEmployee.getId());
                employeeData.remove(selectedEmployee);
                System.out.println("Employee deleted"); // Временное сообщение для отладки
            }
        } else {
            showAlert("Пожалуйста, выберите сотрудника для удаления.");
        }
    }



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Сообщение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
