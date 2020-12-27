import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название файла (без расширения):");
        String fileName = scanner.nextLine();
        System.out.println("Пароль для 1-го листа:");
        String pass_1 = scanner.nextLine();
        System.out.println("Пароль для 2-го листа:");
        String pass_2 = scanner.nextLine();

        //Создаем excel-документ, задаем стиль
        Workbook book = new HSSFWorkbook();

        CellStyle style = book.createCellStyle();
        style.setWrapText(true);
        Font font = book.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short)9);
        style.setFont(font);


        CellStyle style1 = book.createCellStyle();
        style1.setWrapText(true);
        Font font1 = book.createFont();
        font1.setFontName("Arial");
        font1.setColor((short)54);
        font1.setBold(true);
        font1.setFontHeightInPoints((short)15);
        style1.setFont(font1);

        //Настройка первого листа
        Sheet list1 = book.createSheet("Лист 1");
        list1.protectSheet(pass_1); //установка пароля на 1-ый лист
        list1.setColumnWidth(0, Math.round(30 * 256));
        list1.setColumnWidth(1, Math.round(45 * 256));

        Row nameRow1 = list1.createRow(0);
        Cell nameCell1_1 = nameRow1.createCell(0, CellType.STRING);
        nameCell1_1.setCellValue("Столбец чисел");
        nameCell1_1.setCellStyle(style1);

        Cell nameCell1_2 = nameRow1.createCell(1, CellType.STRING);
        nameCell1_2.setCellValue("Столбец синусов для чисел из левого столбца");
        nameCell1_2.setCellStyle(style1);

        int rowIndex1 = 1;
        Row row1 = null;
        Cell cell1 = null;
        for (int i = 1; i < 10; i++)
        {
            row1 = list1.createRow(rowIndex1++);

            cell1 = row1.createCell(0, CellType.STRING);
            cell1.setCellValue(rowIndex1);
            cell1.setCellStyle(style);

            cell1 = row1.createCell(1, CellType.FORMULA);
            cell1.setCellFormula("SIN(A"+rowIndex1 + ")");
            cell1.setCellStyle(style);
        }

        //Настройка второго листа
        Sheet list2 = book.createSheet("Лист 2");
        list2.protectSheet(pass_2); //установка пароля на 1-ый лист
        list2.setColumnWidth(0, Math.round(15 * 256));
        list2.setColumnWidth(1, Math.round(10 * 256));

        Row nameRow2 = list2.createRow(0);
        Cell nameCell2_1 = nameRow2.createCell(0, CellType.STRING);
        nameCell2_1.setCellStyle(style1);
        nameCell2_1.setCellValue("Студенты");


        Cell nameCell2_2 = nameRow2.createCell(3, CellType.STRING);
        nameCell2_2.setCellStyle(style1);
        nameCell2_2.setCellValue("Тест");


        int rowIndex2 = 1;
        Row row2 = null;
        Cell cell2 = null;
        Random random = new Random();
        for (int i = 1; i < 13; i++)
        {
            row2 = list2.createRow(rowIndex2++);

            cell2 = row2.createCell(0, CellType.STRING);
            cell2.setCellValue("Студент " + (rowIndex2-1));
            cell2.setCellStyle(style);

            cell2 = row2.createCell(3, CellType.NUMERIC);
            cell2.setCellValue(random.nextInt(10));
            cell2.setCellStyle(style);
        }

        book.write(new FileOutputStream(fileName + ".xls"));
        book.close();
    }
}