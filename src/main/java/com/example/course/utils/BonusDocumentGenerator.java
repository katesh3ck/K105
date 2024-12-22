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

        for (Employee employee : employees) {
            // Добавление разделителя для каждого сотрудника
            if (document.getParagraphs().size() > 0) {
                XWPFParagraph pageBreak = document.createParagraph();
                pageBreak.setPageBreak(true);
            }

            // Заголовок документа
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("ПРЕДСТАВЛЕНИЕ");
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.setFontFamily("Times New Roman");

            // Место издания
            XWPFParagraph location = document.createParagraph();
            location.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun locationRun = location.createRun();
            locationRun.setText("Место издания: __________________");
            locationRun.setFontSize(14);
            locationRun.setFontFamily("Times New Roman");

            // Номер
            XWPFParagraph number = document.createParagraph();
            number.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun numberRun = number.createRun();
            numberRun.setText("№ ______________");
            numberRun.setFontSize(14);
            numberRun.setFontFamily("Times New Roman");

            // Основная информация о сотруднике
            addParagraph(document, "Фамилия, имя, отчество работника:", employee.getFirstName() + " " + employee.getLastName());
            addParagraph(document, "Наименование должности (профессии):", employee.getPosition());
            addParagraph(document, "Наименование структурного подразделения:", employee.getDepartmentName());
            addParagraph(document, "Стаж работы в данной организации:", employee.getExperienceYears() + " лет");
            addParagraph(document, "Оценка производственной деятельности:", employee.getPerformance());
            addParagraph(document, "Мотив поощрения:", "За добросовестное выполнение обязанностей и высокую производительность.");
            addParagraphWithSpacing(document, "Основание:", "Итоги работы за год.", 2000); // Увеличенный отступ после основания

            // Подпись руководителя
            addParagraph(document, "Руководитель структурного подразделения", "_________________________");
            addParagraph(document, "Подпись", "_________________________");
            addParagraph(document, "Расшифровка подписи", "_________________________");
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

    // Метод для добавления параграфа с заголовком и текстом
    private void addParagraph(XWPFDocument document, String title, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setFontSize(14);
        run.setFontFamily("Times New Roman");
        run.setBold(true);
        run.setText(title);
        run.addBreak();
        run.setBold(false);
        run.setText(text);
    }

    // Метод для добавления параграфа с увеличенным отступом
    private void addParagraphWithSpacing(XWPFDocument document, String title, String text, int spacingAfter) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingAfter(spacingAfter);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(14);
        run.setFontFamily("Times New Roman");
        run.setBold(true);
        run.setText(title);
        run.addBreak();
        run.setBold(false);
        run.setText(text);
    }
}
