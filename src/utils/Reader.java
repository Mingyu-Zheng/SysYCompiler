package utils;

import frontend.token.Char;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Reader {
    private File file = null;
    private ArrayList<String> content = new ArrayList<>();
    //private char[] content = new char[819200];
    private int cptr = 0;
    private int lptr = 0;
    private int lnum = 0;

    private static Reader reader = new Reader();

    private Reader(){

    }

    public static Reader getInstance(){
        return reader;
    }

    public int getLptr(){
        return this.lptr;
    }

    public void getchar(Char thechar){
        if(lptr >= lnum){
            thechar.setValue('#');
            thechar.setStatus(-1);
            return;
        }
        String s = content.get(lptr);
        thechar.setValue(s.charAt(cptr++));
        if(cptr >= s.length()){
            cptr = 0;
            lptr++;
        }
    }

    public void retract(){
        cptr--;
        if(cptr < 0){
            lptr--;
            cptr = content.get(lptr).length() - 1;
        }
    }

    public void readerInit(String filename){
        this.file = new File(filename);
        this.lnum = 0;
        try {
            InputStreamReader thereader = new InputStreamReader(new FileInputStream(file));

            BufferedReader bufferedReader = new BufferedReader(thereader);
            String lineText = null;
            while((lineText = bufferedReader.readLine())!=null){
                content.add(lineText + "\n");
            }
            this.lnum = content.size();
            thereader.close();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
