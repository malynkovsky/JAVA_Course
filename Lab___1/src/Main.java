import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Main {

    public static void main(String[] args) {

        try
        {
            // Указываем пути к исходному pdf-файлу и к txt-файлу, куда будет записан текст
            File pdf_file = new File("Malynkovsky.pdf");
            File txt_file = new File("TXT_Malynkovsky.txt");

            // Для работы с pdf-файлами мы воспользуемся библиотекой pdfbox
            // Метод load () класса PDDocument используется для загрузки существующего pdf.
            // Чтобы извлечь текст, используем метод getText () класса PDFTextStripper
            // Он извлекает весь текст из данного документа pdf.
            PDFTextStripper pdf_txt_strip = new PDFTextStripper();
            PDDocument pdf_doc = PDDocument.load(pdf_file);
            String pdf_text = pdf_txt_strip.getText(pdf_doc);
            pdf_doc.close();

            // Выводим в консоль весь текст
            System.out.println(pdf_text);

            // Сохраняем в текстовый файл
            // Через конструктор класса FileOutputStream задается файл, в который производится запись
            // Класс BufferedOutputStream создает буфер для потоков вывода
            // Класс DataOutputStream представляет поток вывода и предназначен для записи данных примитивных типов
            // Метод writeUTF записывает в поток строку в кодировке UTF-8
            FileOutputStream fileToSaveFromPDF = new FileOutputStream(txt_file);
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fileToSaveFromPDF));
            outStream.writeUTF(pdf_text);
            outStream.close();
        }
        catch (IOException ioException) // Ошибки, связанные с вводом/выводом данных
        {
            System.out.println("Ошибка при чтении файла: "+ ioException);
        }
        catch (Exception exception) // Остальные ошибки
        {
            System.out.println("Возникла неизвестная ошибка: "+ exception);
        }
    }
}
