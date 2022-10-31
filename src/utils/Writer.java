package utils;

import java.io.*;
import java.util.ArrayList;

public class Writer {
    private File file = null;
    private ArrayList<String> content = new ArrayList<>();
    // private char[] content = new char[81920];
    private int cnum = 0;

    private static Writer writer = new Writer();

    private Writer(){

    }

    public static Writer getInstance(){
        return writer;
    }

    public void writerInit(String filename){
        this.file = new File(filename);

    }

    public void addStr(String str){
        content.add(str);
    }

    public void writeContent(){
        try{
            OutputStreamWriter thewriter = new OutputStreamWriter(new FileOutputStream(file));
            for(String str:content){
                thewriter.write(str);
            }
            thewriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
