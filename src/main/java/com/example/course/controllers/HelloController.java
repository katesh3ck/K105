package com.example.course.controllers;

import com.example.course.models.Employee;
import com.example.course.dao.EmployeeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class HelloController {

    @FXML private TableView<Employee> tableView;
    @FXML private TableColumn<Employee, Integer> idColumn;
    @FXML private TableColumn<Employee, String> firstNameColumn;
    @FXML private TableColumn<Employee, String> lastNameColumn;
    @FXML private TableColumn<Employee, String> departmentColumn;
    @FXML private TableColumn<Employee, String> positionColumn;
    @FXML private TableColumn<Employee, Double> salaryColumn;
    @FXML private TableColumn<Employee, LocalDate> hireDateColumn;
    @FXML private TableColumn<Employee, Double> bonusColumn;
    @FXML private TableColumn<Employee, String> experienceColumn;
    @FXML private Button loadButton;
    @FXML private Button calculateButton;

    private final ObservableList<Employee> employeeData = FXCollections.observableArrayList();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    @FXML
    public void initialize() {
        // Настройка отображения данных в столбцах
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        positionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));


        // Настройка отображения зарплаты
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salaryColumn.setCellFactory(column -> new TableCell<>() {
            private final DecimalFormat format = new DecimalFormat("#,##0.00");

            @Override
            protected void updateItem(Double salary, boolean empty) {
                super.updateItem(salary, empty);
                if (empty || salary == null) {
                    setText(null);
                } else {
                    setText(format.format(salary)); // Форматируем зарплату
                }
            }
        });

        // Настройка отображения премий
        bonusColumn.setCellValueFactory(new PropertyValueFactory<>("bonus"));
        bonusColumn.setCellFactory(column -> new TableCell<>() {
            private final DecimalFormat format = new DecimalFormat("#,##0.00");

            @Override
            protected void updateItem(Double bonus, boolean empty) {
                super.updateItem(bonus, empty);
                if (empty || bonus == null) {
                    setText(null);
                } else {
                    setText(format.format(bonus)); // Форматируем премию
                }
            }
        });

        // Настройка отображения отдела с переносом текста
        departmentColumn.setCellFactory(column -> new TableCell<>() {
            private final javafx.scene.text.Text text = new javafx.scene.text.Text();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    text.setText(item);
                    text.wrappingWidthProperty().bind(departmentColumn.widthProperty().subtract(10));
                    setGraphic(text);
                    setText(null);
                }
            }
        });

        // Настройка отображения должности с переносом текста
        positionColumn.setCellFactory(column -> new TableCell<>() {
            private final javafx.scene.text.Text text = new javafx.scene.text.Text();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    text.setText(item);
                    text.wrappingWidthProperty().bind(positionColumn.widthProperty().subtract(10));
                    setGraphic(text);
                    setText(null);
                }
            }
        });

        experienceColumn.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue();
            int years = employee.getExperienceYears();
            int months = employee.getExperienceMonths();
            String experience = years + " years " + months + " months"; // Формат для отображения
            return new javafx.beans.property.SimpleStringProperty(experience);
        });


        // Устанавливаем действия для кнопок
        loadButton.setOnAction(event -> loadDataFromDatabase());
        calculateButton.setOnAction(event -> calculateBonuses());
    }


    /**
     * Метод для автоизменения ширины столбцов.
     */
    private void autoResizeColumns() {
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); // Устанавливаем новую политику

        for (TableColumn<Employee, ?> column : tableView.getColumns()) {
            column.setPrefWidth(100); // Устанавливаем базовую ширину
        }

        tableView.widthProperty().addListener((observable, oldWidth, newWidth) -> {
            double totalWidth = tableView.getColumns().stream().mapToDouble(TableColumn::getWidth).sum();
            double availableWidth = newWidth.doubleValue();

            if (totalWidth < availableWidth) {
                double extraSpace = (availableWidth - totalWidth) / tableView.getColumns().size();
                for (TableColumn<Employee, ?> column : tableView.getColumns()) {
                    column.setPrefWidth(column.getWidth() + extraSpace);
                }
            }
        });
    }

    /**
     * Загрузка данных сотрудников из базы данных.
     */
    private void loadDataFromDatabase() {
        employeeData.clear();
        employeeData.addAll(employeeDAO.getAllEmployees());
        tableView.setItems(employeeData);

        // Обновляем ширину столбцов после загрузки данных
        autoResizeColumns();
    }

    /**
     * Расчёт и сохранение премий сотрудников.
     */
    private void calculateBonuses() {
        for (Employee employee : employeeData) {
            double newBonus = employee.getSalary() * 0.1; // Рассчитываем премию как 10% от зарплаты
            employee.setBonus(newBonus); // Устанавливаем премию
            employeeDAO.insertBonus(employee.getId(), 2024, newBonus); // Сохраняем премию в базе данных
        }
        tableView.refresh(); // Обновляем таблицу
    }
}
