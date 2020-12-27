import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{
    public static Boolean Match_data_time(String line)
    {
        Pattern data_time_pattern = Pattern.compile("(\\d\\d\\d\\d)\\/(\\d\\d)\\/(\\d\\d) (\\d\\d)\\:(\\d\\d)\\:(\\d\\d):\\d\\d\\d \\[\\d+,\\d+\\] ERROR v(\\d.\\d.\\d\\d\\d)");
        Matcher matcher;
        matcher = data_time_pattern.matcher(line);
        Boolean isFound = false;
        if(matcher.find())
        {
            String year = matcher.group(1).trim();
            String month = matcher.group(2).trim();
            String day = matcher.group(3).trim();
            String hour = matcher.group(4).trim();
            String minute = matcher.group(5).trim();
            String second = matcher.group(6).trim();

            StringBuilder data = new StringBuilder();
            data.append(day).append(".").append(month).append(".").append(year);
            StringBuilder time = new StringBuilder();
            time.append(hour).append(":").append(minute).append(":").append(second);

            System.out.println("Дата входа в систему: " + data);
            System.out.println("Время входа в систему: " + time);
            isFound = true;
        }
        return isFound;
    }
    public static void Match_user_name(String line)
    {
        Pattern user_name_pattern = Pattern.compile("(\\S+)@");
        Matcher matcher;
        matcher = user_name_pattern.matcher(line);
        if(matcher.find())
        {
            System.out.println("Имя пользователя: " + matcher.group(1).trim());
        }
    }
    public static Boolean Match_client_version(String line)
    {
        Pattern client_version_pattern = Pattern.compile("(\\d.\\d.\\d\\d\\d) \\[\\d+\\] \\\".*\\\"");
        Matcher matcher;
        matcher = client_version_pattern.matcher(line);
        Boolean isInfo = false;
        if(matcher.find())
        {
            System.out.println("Версия клиента: " + matcher.group(1).trim());
            isInfo = true;
        }
        return isInfo;
    }
    public static void Match_server(String line)
    {
        Pattern agent_version_pattern = Pattern.compile("agent ver:(\\d.\\d.\\d\\d\\d)");
        Matcher matcher;
        matcher = agent_version_pattern.matcher(line);
        if(matcher.find())
        {
            System.out.println("Версия сервера: " + matcher.group(1).trim());
            System.out.println();
        }
    }
    public static void Parse_log_file(String log_path) throws IOException
    {
        FileReader reader = new FileReader(log_path);
        BufferedReader buffer = new BufferedReader(reader);
        String line;
        Boolean entry_per_one_user = false;
        while((line = buffer.readLine()) != null)
        {
            // Получаем дату и время входа в систему
            if (entry_per_one_user)
                if (Match_data_time(line))
                    entry_per_one_user = false;
            // Получаем имя пользователя
            Match_user_name(line);
            // Получаем версию клиента
            if (!entry_per_one_user)
                if (Match_client_version(line))
                    entry_per_one_user = true;
            // Получаем версию агента (сервера)
            Match_server(line);
        }
    }

    public static void main(String[] args) throws Exception
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите путь к log-файлу:");
        String log_path = in.nextLine();
        System.out.println();

        Parse_log_file(log_path);
    }
}
