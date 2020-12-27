import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{
    public static class IniFile
    {
        final Pattern _section  = Pattern.compile( "\\s*\\[([^]]*)\\]\\s*" );
        final Pattern  _keyValue = Pattern.compile( "\\s*([^=]*)=(.*)" );
        final Map<String, Map<String, String>>  _entries  = new HashMap<>();
        final ByteArrayOutputStream _iniContent = new ByteArrayOutputStream();

        private void load_ini_file() throws Exception
        {
            // Получаем путь к файлу с консоли (путь: malynkovsky.ini, файл лежит в нашей же папке)
            Scanner in = new Scanner(System.in);
            String filePath = in.nextLine();
            FileReader iniFile = new FileReader(filePath);
            // Считываем ini-файл
            try(BufferedReader br = new BufferedReader(iniFile))
            {
                String line;
                String section = null;
                while((line = br.readLine()) != null)
                {
                    // Применяем регулярное выражение к строке
                    Matcher m = _section.matcher(line);
                    // Если шаблон подходит, то мы нашли секцию
                    if( m.matches())
                    {
                        section = m.group(1).trim();
                        System.out.println("section: "+section);
                    }
                    else // Иначе мы мы нашли строку ключ-значение (если строка не пустая)
                        if(section != null)
                        {
                            m = _keyValue.matcher(line);
                            // Проверяем по шаблону
                            if(m.matches())
                            {
                                // Выводим в консоль ключ и его значение
                                String key = m.group(1).trim(); // trim() для отсечения пробелов
                                System.out.println("key: "+key);
                                String value = m.group(2).trim();
                                System.out.println("value: "+value);
                                // Добавляем в _entries считанную пару, если такой ранее там не было
                                Map<String, String> kv = _entries.get(section);
                                if(kv == null)
                                    _entries.put(section, kv = new HashMap<>());
                                kv.put(key, value);
                            }
                        }
                    System.out.println();
                }
                System.out.println("Всего было выведено entries: " + _entries.size());
            }
        }

    }

    public static void main(String[] args) throws Exception
    {
        IniFile inif = new IniFile();
        inif.load_ini_file();
    }

}

