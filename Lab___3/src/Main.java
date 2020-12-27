import java.io.*;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main
{
    private static void makeCatalogTree(ZipOutputStream zip, String currentDir, String zipEntryName) throws IOException
    {
        // Проход по всему каталогу
        File dir = new File(currentDir);
        for(File file : dir.listFiles())
        {
            if (file.isHidden())
                continue; // пропускаем скрытые файлы и подпапки
            else
            {
                if (file.isFile() && file.exists()) // Добавляем файл в архив
                {
                    putEntry(zip, zipEntryName+file.getName(), file.getPath());
                }
                else // создаем подпапку и рекурсивно вызваем эту же функцию для ее заполнения
                {
                    String temp = zipEntryName;
                    zipEntryName = zipEntryName + file.getName() + "\\";
                    makeCatalogTree(zip, file.getPath(), zipEntryName);
                    zipEntryName = temp;
                }
            }
        }
    }
    private static void putEntry(ZipOutputStream zip, String entryName, String currentDir) throws IOException
    {
        File file = new File(currentDir);
        ZipEntry entry = new ZipEntry(entryName);
        zip.putNextEntry(entry);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        zip.write(buffer);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException
    {

        Scanner in = new Scanner(System.in);
        System.out.println("Введите путь к каталогу, который нужно архивировать: ");
        String dirPath = in.nextLine();
        File dir = new File(dirPath);

        System.out.println("Введите путь и название для zip-архива: ");
        String nameLine = in.nextLine();
        File name = new File(nameLine);

        if (!dir.exists() || name.exists())
            System.out.println("Указанной папки не существует или уже есть zip с таким именем");
        else
        {
            FileOutputStream fos = new FileOutputStream(name);
            ZipOutputStream zip = new ZipOutputStream(fos);
            zip.setLevel(0);
            zip.setMethod(ZipOutputStream.DEFLATED);

            makeCatalogTree(zip, dirPath, "");

            zip.finish();
        }


    }
}





