import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
public class Main {
    //Извлечение текст из txt файла
    private static String Get_text_from_file(String pdf_path) throws IOException
    {
        String full_text = "";
        BufferedReader br = new BufferedReader(new FileReader(pdf_path, StandardCharsets.UTF_8));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                for (int i = 0; i < line.length(); i++) {
                    //Эта проверка, чтобы не включать символы, которые не распознаны могут быть кодировкой
                    if (WinAnsiEncoding.INSTANCE.contains(line.charAt(i))) {
                        sb.append(line.charAt(i));
                    }
                }
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            full_text = sb.toString();
        } finally {
            br.close();
        }
        return full_text;
    }
    //Добавление текста, картинку в pdf и сохранение файла
    private static void Add_text_and_image_to_pdf(String pdf_path, String full_text, String img_path) throws Exception
    {
        try
        {
            int pages = 0;
            PDDocument document = new PDDocument();
            // Разделяем весь текст на отдельные строки
            String[] mas_text = full_text.split("\\r?\\n");
            int y_position = 725;
            PDPage page = new PDPage();
            PDPageContentStream stream = new PDPageContentStream(document, page,PDPageContentStream.AppendMode.APPEND, true, true);
            for(int i = 0; i < mas_text.length; i++)
            {
                if(y_position <= 25)
                {
                    y_position = 725;
                    stream.close();
                    document.addPage(page);
                    page = new PDPage();
                    pages = pages+1;
                    stream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
                }
                // Каждую строку делим на отдельные слова, разделенные пробелом,
                // чтобы можно было их вывести и они бы поместились до конца строки
                // Например 12 - число слов максимально на одной строке я подобрал эмпирически
                // так как к сожалению в pdf-box нет автоматичекой возможности переноса текста по строкам
                String[] words = mas_text[i].split(" ");
                int _line_ = 0;
                int _letters_ = 0;
                for (String word:words)
                {
                    if (_line_ == 12)
                    {
                        stream.setFont(PDType1Font.HELVETICA, 10);
                        y_position = y_position - 10;
                        if(y_position <= 25)
                        {
                            y_position = 725;
                            stream.close();
                            document.addPage(page);
                            page = new PDPage();
                            pages = pages+1;
                            stream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
                        }
                        _line_ = 0;
                        _letters_ = 0;
                    }
                    stream.beginText();
                    stream.setFont(PDType1Font.HELVETICA, 10);
                    stream.newLineAtOffset((25 + _letters_), y_position);
                    stream.showText(word + " ");
                    _letters_ = _letters_ + word.length()*6 + 4;
                    _line_ = _line_ + 1;
                    stream.endText();
                }
                y_position = y_position - 10;
            }
            stream.close();
            document.addPage(page);
            // Добавляем картинку в наш документ
            stream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
            PDImageXObject pdImage = PDImageXObject.createFromFile(img_path,document);
            stream.drawImage(pdImage, (float) 250,
                    (float) 50, (float) mmTo1_72inch(100), (float) mmTo1_72inch(70));
            stream.close();
            // Сохраняем pdf
            document.save(pdf_path);
            document.close();
            System.out.println("Текст и картинка записаны");
        }
        catch (IOException e)
        {
            System.out.println("Произошла ошибка");
            System.out.println(e.getCause());
        }

    }
    private static double mmTo1_72inch(double mm)
    {
        return mm / 25.4 * 72;
    }
    public static void main(String[] args) throws Exception
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите путь к тексту: ");
        String pdf_path = in.nextLine();
        System.out.println("Введите путь к картинке: ");
        String image_path = in.nextLine();
        System.out.println("Введите путь для сохранения в pdf: ");
        String save_path = in.nextLine();
        //pdf_path = "TXT_Malynkovsky.txt";
        //image_path = "1.png";
        //save_path = "Malynkovsky_FINAL.pdf";
        String full_text = Get_text_from_file(pdf_path);
        Add_text_and_image_to_pdf(save_path, full_text, image_path);
    }
}