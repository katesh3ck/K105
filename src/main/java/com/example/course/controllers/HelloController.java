package com.example.course.controllers;

import com.example.course.models.Employee;
import com.example.course.dao.EmployeeDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
    @FXML private TableColumn<Employee, String> compensationColumn;


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
        // Инициализация основной таблицы
        initializeMainTable();

        // Инициализация сводной таблицы
        initializeSummaryTable();

        // Устанавливаем действия для кнопок
        loadButton.setOnAction(event -> loadDataFromDatabase());
        calculateButton.setOnAction(event -> calculateBonuses());
    }

    /**
     * Инициализация основной таблицы с подробными данными сотрудников.
     */
    private void initializeMainTable() {
        // Настройка столбца с чекбоксом
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setPrefWidth(60);
        selectColumn.setResizable(false);

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

        // Перенос текста для отдела
        departmentColumn.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();

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

        // Перенос текста для должности
        positionColumn.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();

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

        // Форматирование стажа
        experienceColumn.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue();
            int years = employee.getExperienceYears();
            int months = employee.getExperienceMonths();
            String experience = years + " лет " + months + " мес"; // Формат для отображения
            return new SimpleStringProperty(experience);
        });

        // Добавление столбца компенсаций

        compensationColumn.setCellFactory(column -> new TableCell<>() {
            private final VBox vbox = new VBox();
            private final CheckBox nightCheckBox = new CheckBox("Работа в ночное время");
            private final CheckBox holidayCheckBox = new CheckBox("Работа в праздники");

            {
                nightCheckBox.setOnAction(event -> updateCompensation());
                holidayCheckBox.setOnAction(event -> updateCompensation());
            }

            private void updateCompensation() {
                Employee employee = getTableView().getItems().get(getIndex());
                StringBuilder compensation = new StringBuilder();
                if (nightCheckBox.isSelected()) compensation.append("Работа в ночное время;");
                if (holidayCheckBox.isSelected()) compensation.append("Работа в праздники;");
                employee.setCompensationType(compensation.toString());
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Employee employee = getTableView().getItems().get(getIndex());
                    nightCheckBox.setSelected(employee.getCompensationType().contains("Работа в ночное время"));
                    holidayCheckBox.setSelected(employee.getCompensationType().contains("Работа в праздники"));
                    vbox.getChildren().setAll(nightCheckBox, holidayCheckBox);
                    setGraphic(vbox);
                }
            }
        });


        // Настройка автоизменения ширины столбцов
        autoResizeColumns();
    }

    private void showError(String title, String message) {
        // Создание диалогового окна с сообщением об ошибке
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title); // Заголовок окна
        alert.setHeaderText(null); // Без заголовка
        alert.setContentText(message); // Текст сообщения

        // Отображение диалогового окна
        alert.showAndWait();
    }



    /**
     * Инициализация сводной таблицы с именами, фамилиями и премиями сотрудников.
     */
    private void initializeSummaryTable() {
        // Настройка столбцов сводной таблицы
        summaryFirstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        summaryLastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        summaryBonusColumn.setCellValueFactory(new PropertyValueFactory<>("bonus"));

        // Форматирование премии
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

        // Установка данных для сводной таблицы
        summaryTableView.setItems(summaryData);

        // Настройка автоизменения ширины столбцов для сводной таблицы
        summaryTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        for (TableColumn<Employee, ?> column : summaryTableView.getColumns()) {
            column.setPrefWidth(150);
        }

        summaryTableView.widthProperty().addListener((observable, oldWidth, newWidth) -> {
            double totalWidth = summaryTableView.getColumns().stream().mapToDouble(TableColumn::getWidth).sum();
            double availableWidth = newWidth.doubleValue();

            if (totalWidth < availableWidth) {
                double extraSpace = (availableWidth - totalWidth) / summaryTableView.getColumns().size();
                for (TableColumn<Employee, ?> column : summaryTableView.getColumns()) {
                    column.setPrefWidth(column.getWidth() + extraSpace);
                }
            }
        });
    }

    /**
     * Метод для автоизменения ширины столбцов основной таблицы.
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
        List<Employee> employees = employeeDAO.getAllEmployees();
        employeeData.clear();
        employeeData.addAll(employees);
        tableView.setItems(employeeData);

        // Очистка сводной таблицы при загрузке новых данных
        summaryData.clear();
        summaryTableView.refresh();
    }

    /**
     * Расчёт и сохранение премий сотрудников, а также обновление сводной таблицы.
     */

    private void calculateBonuses() {
        // Очистка старых данных
        summaryData.clear();

        for (Employee employee : employeeData) {
            double compensationBonus = 0;
            if (employee.getCompensationType().contains("Работа в ночное время")) {
                compensationBonus += employee.getCompensationHours() * 200;
            }
            if (employee.getCompensationType().contains("Работа в праздники")) {
                compensationBonus += employee.getCompensationHours() * 300;
            }
            double totalBonus = employee.getSalary() * 0.1 + compensationBonus;
            employee.setBonus(totalBonus);

            // Добавление данных в сводную таблицу
            summaryData.add(employee);

            // Сохранение в базу данных
            employeeDAO.insertBonus(employee.getId(), 2024, totalBonus);
        }

        // Обновление сводной таблицы
        summaryTableView.refresh();
    }



}
