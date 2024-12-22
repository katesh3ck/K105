package com.example.course.utils;

import com.example.course.models.Employee;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class BonusDocumentGenerator {

    public void generateBonusDocument(List<Employee> employees) {
        // Создание документа
        XWPFDocument document = new XWPFDocument();

        // Создание заголовка
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Представление о поощрении");
        titleRun.setBold(true);
        titleRun.setFontSize(16);

        // Добавление подзаголовка
        XWPFParagraph subtitle = document.createParagraph();
        subtitle.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun subtitleRun = subtitle.createRun();
        subtitleRun.setText("Информация о сотрудниках");
        subtitleRun.setFontSize(14);
        subtitleRun.setItalic(true);
        subtitleRun.addBreak();

        for (Employee employee : employees) {
            // Создание таблицы для каждого сотрудника
            XWPFTable table = document.createTable();
            table.setWidth("100%");

            // Добавление строки заголовков таблицы
            XWPFTableRow headerRow = table.getRow(0);
            headerRow.getCell(0).setText("Параметр");
            headerRow.addNewTableCell().setText("Значение");

            // Добавление данных о сотруднике в таблицу
            addTableRow(table, "Фамилия, имя, отчество", employee.getFirstName() + " " + employee.getLastName());
            addTableRow(table, "Наименование должности", employee.getPosition());
            addTableRow(table, "Структурное подразделение", employee.getDepartmentName());
            addTableRow(table, "Стаж работы", employee.getExperienceYears() + " лет");
            addTableRow(table, "Оценка производственной деятельности", employee.getPerformance());
            addTableRow(table, "Мотив поощрения", "За добросовестное выполнение обязанностей и высокую производительность.");
            addTableRow(table, "Основание", "Итоги работы за год.");

            // Добавление разделителя между сотрудниками
            XWPFParagraph separator = document.createParagraph();
            separator.setPageBreak(true);
        }

        // Сохранение документа в указанную папку
        String projectPath = "C:\\Users\\User\\IdeaProjects\\K105";
        File outputFile = new File(projectPath, "BonusDocument_AllEmployees.docx");

        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            document.write(out);
            System.out.println("Document created successfully in " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error creating document: " + e.getMessage());
        }
    }

    // Метод для добавления строки в таблицу
    private void addTableRow(XWPFTable table, String parameter, String value) {
        XWPFTableRow row = table.createRow();
        row.getCell(0).setText(parameter);
        row.getCell(1).setText(value);
    }
}
