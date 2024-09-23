import java.io.*;
import java.util.*;
public class Stlines{
    public static void main(String argv[]){
        File fdir;
        if (argv.length<1)
            fdir = new File(".");		//用户没有指定目录，默认为当前目录
        else
            fdir = new File(argv[0]);
        int lines=StaticOfPath(fdir);
        System.out.println("总计有"+lines+"行代码");
    }

    public static int StaticOfPath(File fdir){
        File[] AllFiles;
        int lines, cnt=0;
        AllFiles = fdir.listFiles();        //获取文件和目录列表
        for(File eachFile : AllFiles){
            String name = eachFile.getName();    //获取文件或者目录名
            if (eachFile.isFile()){		         //判断是否为文件
                if(name.endsWith(".java")){ //判断是否为java源文件
                    lines = LinesOfFile(eachFile);
                    System.out.println(name+": "+lines+"行");
                    cnt += lines;
                }
            }else{                     //当前是一个目录
                cnt += StaticOfPath(eachFile);
            }
        }
        return cnt;
    }

    public static int LinesOfFile(File eachFile){
        FileReader fileReader;
        BufferedReader bufReader;
        String line;
        int cntLine=0;
        try{
            fileReader = new FileReader(eachFile);
            bufReader = new BufferedReader(fileReader);
            do{
                line = bufReader.readLine();
                if(line!=null){
                    if(line.strip().length()>0)
                        cntLine++;
                }
            }while(line!=null);
            bufReader.close();
        }catch(FileNotFoundException e){	//处理异常
            System.out.println("文件没找到："+eachFile.getName());
        }catch(IOException e){
            System.out.println("读入文件有误:"+eachFile.getName());
        }
        return cntLine;
    }
}