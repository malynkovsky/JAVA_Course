import java.io.*;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class Main {
    public static void putStamp(PDDocument _pdf, double offsetX, double offsetY, double height, double width,
                         File stampTemplate, String new_file) throws IOException, Exception
    {
        PDPage page = _pdf.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(_pdf, page, PDPageContentStream.AppendMode.APPEND, true, true);
        PDImageXObject pdImage = PDImageXObject.createFromFileByContent(stampTemplate,_pdf);
        contentStream.drawImage(pdImage, (float) offsetX,
                (float) offsetY, (float) mmTo1_72inch(height), (float) mmTo1_72inch(width));
        contentStream.close();
        _pdf.save(new File(new_file));
    }

    private static double mmTo1_72inch(double mm)
    {
        return mm / 25.4 * 72;
    }

    public static void main(String[] args) throws Exception
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Укажите путь к исходному PDF-файлу: ");
            String pfd_path = scanner.nextLine();
            System.out.println("Укажите путь к штампу: ");
            String stamp_path = scanner.nextLine();

            System.out.println("Укажите имя PDF-файла со штампом: ");
            String new_pdf_path = scanner.nextLine();

            File pdf_file = new File(pfd_path);
            PDDocument pdf_doc = PDDocument.load(pdf_file);
            File stamp_file = new File(stamp_path);

            putStamp(pdf_doc, 300.0, 100.0, 70, 50,
                    stamp_file, new_pdf_path);
        }
        catch (IOException ioException)
        {
            System.out.println("Ошибка при чтении файла: "+ ioException);
        }
        catch (Exception exception)
        {
            System.out.println("При выполнении программы возникла неизвестная ошибка: "+ exception);
        }
    }
}
