import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class RankPanel
{

    //只获得时间
    public String getTimeData(int index) {
        if (index < 0 || index >= 3) {
            return "难度越界";
        }
        String line = readLineFromFile(index);
        if (line != null) {
            String[] parts = line.split(" ");
            if (parts.length > 1) {
                return parts[1];
            } else {
                return "查询失败";
            }
        } else {
            return "查询失败";
        }
    }
    //获得名字+时间
    public String getData(int index){
        if (index < 0 || index >= 3) {
            return "难度越界";
        }
        String line = readLineFromFile(index);
        if (line != null) {
            return line;
        } else {
            return "获取失败";
        }

    }

    //设置数据
    public void setTimeData(String name, String timeString, int index) {
        if (index < 0 || index >= 3) {
            System.out.println("Invalid index");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            Date newTime = sdf.parse(timeString);
            String currentData = getTimeData(index);
            if (currentData != null) {
                Date currentTime = sdf.parse(currentData);
                if (newTime.before(currentTime)) {
                    String data = name + " " + timeString;
                    writeLineToFile(data, index);
                    System.out.println("难度 " + index + " 的排行修改成功");
                } else {
                    System.out.println("没破记录，不给修改");
                }
            } else {
                System.out.println("修改失败 " + index);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //读行
    private String readLineFromFile(int index) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Data/rank.txt"))) {
            String line;
            int currentLine = 0;
            while ((line = reader.readLine()) != null) {
                if (currentLine == index) {
                    return line;
                }
                currentLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //写数据
    private void writeLineToFile(String data, int index) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Data/rank.txt"))) {
            String[] lines = new String[3];
            for (int i = 0; i < 3; i++) {
                lines[i] = reader.readLine();
            }
            lines[index] = data;
            try (PrintWriter writer = new PrintWriter(new FileWriter("Data/rank.txt", false))) {
                for (String line : lines) {
                    writer.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //比较时间大小
    public boolean compareTime(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date1 = sdf.parse(time1);
            Date date2 = sdf.parse(time2);
            return date1.before(date2);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
