package com.example.course.controllers;

import com.example.course.models.Employee;
import com.example.course.dao.EmployeeDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

public class HelloController {

    // Основная таблица
    @FXML private TableView<Employee> tableView;
    @FXML private TableColumn<Employee, Boolean> selectColumn;
    @FXML private TableColumn<Employee, Integer> idColumn;
    @FXML private TableColumn<Employee, String> firstNameColumn;
    @FXML private TableColumn<Employee, String> lastNameColumn;
    @FXML private TableColumn<Employee, String> departmentColumn;
    @FXML private TableColumn<Employee, String> positionColumn;
    @FXML private TableColumn<Employee, Double> salaryColumn;
    @FXML private TableColumn<Employee, LocalDate> hireDateColumn;
    @FXML private TableColumn<Employee, String> experienceColumn;
    @FXML private TableColumn<Employee, Boolean> compensationColumn;
    @FXML private TableColumn<Employee, String> performanceColumn; // новый столбец

    // Сводная таблица
    @FXML private TableView<Employee> summaryTableView;
    @FXML private TableColumn<Employee, String> summaryFirstNameColumn;
    @FXML private TableColumn<Employee, String> summaryLastNameColumn;
    @FXML private TableColumn<Employee, Double> summaryBonusColumn;

    @FXML private Button loadButton;
    @FXML private Button calculateButton;

    private final ObservableList<Employee> employeeData = FXCollections.observableArrayList();
    private final ObservableList<Employee> summaryData = FXCollections.observableArrayList();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    @FXML
    public void initialize() {
        // Устанавливаем редактируемость таблицы
        tableView.setEditable(true);

        // Инициализация основной таблицы
        initializeMainTable();

        // Инициализация сводной таблицы
        initializeSummaryTable();

        // Устанавливаем действия для кнопок
        loadButton.setOnAction(event -> loadDataFromDatabase());
        calculateButton.setOnAction(event -> calculateBonuses());

        tableView.refresh();
    }

    /**
     * Инициализация основной таблицы.
     */
    private void initializeMainTable() {

        // Создаем список с элементами для ComboBox
        ObservableList<String> performanceLevels = FXCollections.observableArrayList(
                "низкий уровень", "средний уровень", "высокий уровень"
        );

        performanceColumn.setCellValueFactory(cellData -> cellData.getValue().performanceProperty());
        performanceColumn.setCellFactory(ComboBoxTableCell.forTableColumn(performanceLevels));
        performanceColumn.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            String newPerformance = event.getNewValue();
            if (newPerformance == null || newPerformance.isEmpty()) {
                employee.setPerformance("средний уровень");
            } else {
                employee.setPerformance(newPerformance);
            }
        });

        // Настройка столбца с чекбоксом
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        // Настройка остальных столбцов
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        positionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));

        // Форматирование зарплаты
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salaryColumn.setCellFactory(column -> new TableCell<>() {
            private final DecimalFormat format = new DecimalFormat("#,##0.00");

            @Override
            protected void updateItem(Double salary, boolean empty) {
                super.updateItem(salary, empty);
                if (empty || salary == null) {
                    setText(null);
                } else {
                    setText(format.format(salary));
                }
            }
        });

        // Форматирование опыта
        experienceColumn.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue();
            int years = employee.getExperienceYears();
            int months = employee.getExperienceMonths();
            return new SimpleStringProperty(years + " лет " + months + " мес");
        });

        // Добавление столбца компенсаций
        compensationColumn.setCellFactory(column -> new TableCell<>() {
            private final VBox vbox = new VBox();
            private final CheckBox nightCheckBox = new CheckBox("Работа в ночное время");
            private final CheckBox holidayCheckBox = new CheckBox("Работа в праздники");

            {
                nightCheckBox.setOnAction(event -> updateCompensation(employee -> {
                    if (nightCheckBox.isSelected()) {
                        employee.setNightHours(showHoursDialog("ночное время"));
                    } else {
                        employee.setNightHours(0);
                    }
                }));
                holidayCheckBox.setOnAction(event -> updateCompensation(employee -> {
                    if (holidayCheckBox.isSelected()) {
                        employee.setHolidayHours(showHoursDialog("праздники"));
                    } else {
                        employee.setHolidayHours(0);
                    }
                }));
            }

            private void updateCompensation(java.util.function.Consumer<Employee> updateAction) {
                Employee employee = getTableView().getItems().get(getIndex());
                updateAction.accept(employee);
                StringBuilder compensation = new StringBuilder();
                if (employee.getNightHours() > 0) {
                    compensation.append("Работа в ночное время;");
                }
                if (employee.getHolidayHours() > 0) {
                    compensation.append("Работа в праздники;");
                }
                employee.setCompensationType(compensation.toString());
            }

            private int showHoursDialog(String type) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Ввод часов");
                dialog.setHeaderText("Введите количество часов для компенсации за " + type);
                dialog.setContentText("Часы:");

                return dialog.showAndWait().map(hours -> {
                    try {
                        int hoursWorked = Integer.parseInt(hours);
                        if (hoursWorked < 0) throw new NumberFormatException();
                        return hoursWorked;
                    } catch (NumberFormatException e) {
                        showError("Ошибка", "Введите корректное количество часов (положительное число).");
                        return 0;
                    }
                }).orElse(0);
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Employee employee = getTableView().getItems().get(getIndex());
                    nightCheckBox.setSelected(employee.getNightHours() > 0);
                    holidayCheckBox.setSelected(employee.getHolidayHours() > 0);
                    vbox.getChildren().setAll(nightCheckBox, holidayCheckBox);
                    setGraphic(vbox);
                }
            }
        });

        tableView.setItems(employeeData);
    }

    /**
     * Инициализация сводной таблицы.
     */
    private void initializeSummaryTable() {
        summaryFirstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        summaryLastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        summaryBonusColumn.setCellValueFactory(new PropertyValueFactory<>("bonus"));

        summaryBonusColumn.setCellFactory(column -> new TableCell<>() {
            private final DecimalFormat format = new DecimalFormat("#,##0.00");

            @Override
            protected void updateItem(Double bonus, boolean empty) {
                super.updateItem(bonus, empty);
                if (empty || bonus == null) {
                    setText(null);
                } else {
                    setText(format.format(bonus));
                }
            }
        });

        summaryTableView.setItems(summaryData);
    }

    /**
     * Загрузка данных из базы данных.
     */
    private void loadDataFromDatabase() {
        List<Employee> employees = employeeDAO.getAllEmployees();
        employeeData.setAll(employees);
        summaryData.clear();
        summaryTableView.refresh();
    }

    /**
     * Расчёт премий сотрудников.
     */
    private void calculateBonuses() {
        summaryData.clear();

        for (Employee employee : employeeData) {
            double nightWorkBonus = employee.getNightHours() * employee.getSalary() * 0.03;
            double holidayWorkBonus = employee.getHolidayHours() * employee.getSalary() * 0.02;
            double totalBonus = employee.getSalary() * 0.1 + nightWorkBonus + holidayWorkBonus;
            employee.setBonus(totalBonus);

            summaryData.add(employee);
            employeeDAO.insertBonus(employee.getId(), 2024, totalBonus);
        }

        summaryTableView.refresh();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
