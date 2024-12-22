package com.example.course.controllers;

import com.example.course.managers.EmployeeManager;
import com.example.course.models.Employee;
import com.example.course.dao.EmployeeDAO;
import com.example.course.utils.BonusCalculator;
import com.example.course.utils.BonusDocumentGenerator;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @FXML final EmployeeManager employeeManager = new EmployeeManager(); // Добавляем эту строку


    // Сводная таблица
    @FXML private TableView<Employee> summaryTableView;
    @FXML private TableColumn<Employee, String> summaryFirstNameColumn;
    @FXML private TableColumn<Employee, String> summaryLastNameColumn;
    @FXML private TableColumn<Employee, Double> summaryBonusColumn;
    @FXML private TableColumn<Employee, Double> summarySalaryWithBonusColumn;


    @FXML private Button loadButton;
    @FXML private Button calculateButton;
    @FXML private Button selectAllButton;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML private ChoiceBox<String> filterChoiceBox;
    @FXML private ChoiceBox<String> filterValueChoiceBox;


    private final ObservableList<Employee> employeeData = FXCollections.observableArrayList();
    private final ObservableList<Employee> summaryData = FXCollections.observableArrayList();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    @FXML
    public void initialize() {
        tableView.setEditable(true);

        initializeMainTable();
        initializeSummaryTable();

        loadButton.setOnAction(event -> loadDataFromDatabase());
        calculateButton.setOnAction(event -> calculateBonuses());
        selectAllButton.setOnAction(event -> selectAllEmployees());
        addButton.setOnAction(event -> employeeManager.addEmployee(employeeData)); // Добавляем обработчик для добавления
        editButton.setOnAction(event -> employeeManager.editEmployee(tableView.getSelectionModel().getSelectedItem(), employeeData)); // Добавляем обработчик для редактирования
        deleteButton.setOnAction(event -> employeeManager.deleteEmployee(tableView.getSelectionModel().getSelectedItem(), employeeData)); // Добавляем обработчик для удаления

        filterChoiceBox.setItems(FXCollections.observableArrayList("Отдел", "Должность"));
        filterChoiceBox.setOnAction(event -> updateFilterValues());

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

        // Форматирование зарплаты с учетом премии
        summarySalaryWithBonusColumn.setCellValueFactory(new PropertyValueFactory<>("salaryWithBonus"));
        summarySalaryWithBonusColumn.setCellFactory(column -> new TableCell<>() {
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
        summarySalaryWithBonusColumn.setCellValueFactory(new PropertyValueFactory<>("salaryWithBonus")); // Убедитесь, что этот код добавлен

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
     * Выбор всех сотрудников в таблице.
     */
    private void selectAllEmployees() {
        ObservableList<Employee> currentData = tableView.getItems();
        for (Employee employee : currentData) {
            employee.setSelected(true);
        }
        tableView.refresh();
    }


    /**
     * Обновление значений фильтра в зависимости от выбора пользователя.
     */
    private void updateFilterValues() {
        filterValueChoiceBox.getItems().clear();
        String selectedFilter = filterChoiceBox.getValue();
        if ("Отдел".equals(selectedFilter)) {
            // Заполняем уникальными отделами
            employeeData.stream()
                    .map(Employee::getDepartmentName)
                    .distinct()
                    .forEach(department -> filterValueChoiceBox.getItems().add(department));
        } else if ("Должность".equals(selectedFilter)) {
            // Заполняем уникальными должностями
            employeeData.stream()
                    .map(Employee::getPosition)
                    .distinct()
                    .forEach(position -> filterValueChoiceBox.getItems().add(position));
        }
    }

    /**
     * Применение фильтрации.
     */
    @FXML
    private void applyFilter() {
        String selectedFilter = filterChoiceBox.getValue();
        String selectedValue = filterValueChoiceBox.getValue();
        ObservableList<Employee> filteredData = FXCollections.observableArrayList();

        if ("Отдел".equals(selectedFilter)) {
            for (Employee employee : employeeData) {
                if (employee.getDepartmentName().equals(selectedValue)) {
                    filteredData.add(employee);
                }
            }
        } else if ("Должность".equals(selectedFilter)) {
            for (Employee employee : employeeData) {
                if (employee.getPosition().equals(selectedValue)) {
                    filteredData.add(employee);
                }
            }
        }

        tableView.setItems(filteredData);
    }

    @FXML
    private void addEmployeeAction() {
        employeeManager.addEmployee(employeeData);
    }

    @FXML
    private void editEmployeeAction() {
        Employee selectedEmployee = tableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            employeeManager.editEmployee(selectedEmployee, employeeData);
        } else {
            showError("Сообщение", "Пожалуйста, выберите сотрудника для редактирования."); // Используем showError
        }
    }

    @FXML private void deleteEmployeeAction() {
        Employee selectedEmployee = tableView.getSelectionModel().getSelectedItem();
        ObservableList<Employee> employeeData = tableView.getItems();
        deleteEmployee(selectedEmployee, employeeData);
    }

    public void deleteEmployee(Employee selectedEmployee, ObservableList<Employee> employeeData) {
        if (selectedEmployee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления");
            alert.setHeaderText("Вы уверены, что хотите удалить выбранного сотрудника?");
            alert.setContentText("Сотрудник будет удален из базы данных.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                employeeDAO.deleteEmployee(selectedEmployee.getId());
                employeeData.remove(selectedEmployee);
                tableView.refresh(); // Обновление таблицы
                System.out.println("Employee deleted from application: " + selectedEmployee.getId()); // Сообщение для отладки
            }
        } else {
            showError("Сообщение", "Пожалуйста, выберите сотрудника для удаления.");
        }
    }




    private void refreshTable() {
        tableView.refresh();
    }

    /**
     * Расчёт премий сотрудников только для выбранных.
     */
    private void calculateBonuses() {
        summaryData.clear();
        BonusCalculator bonusCalculator = new BonusCalculator();
        BonusDocumentGenerator documentGenerator = new BonusDocumentGenerator();
        List<Employee> selectedEmployees = new ArrayList<>();

        for (Employee employee : employeeData) {
            if (employee.isSelected()) { // Проверяем, выбран ли сотрудник
                double totalBonus = bonusCalculator.calculateBonus(employee);
                employee.setBonus(totalBonus);
                employee.setSalaryWithBonus(employee.getSalary() + totalBonus);

                summaryData.add(employee);
                employeeDAO.insertBonus(employee.getId(), 2024, totalBonus);
                selectedEmployees.add(employee);
            }
        }

        // Генерация документа для всех выбранных сотрудников
        documentGenerator.generateBonusDocument(selectedEmployees);

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
