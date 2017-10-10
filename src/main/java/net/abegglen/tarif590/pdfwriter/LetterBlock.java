package net.abegglen.tarif590.pdfwriter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by heinz on 10.10.2017.
 */
public class LetterBlock {

    private ArrayList<String> lines = new ArrayList<>();

    public LetterBlock(String lines) {
        String[] linesArray = lines.split("\n");
        this.lines = new ArrayList<>(Arrays.asList(linesArray));
    }

    public LetterBlock(ArrayList<String> lines) {
        this.lines = lines;
    }

    public LetterBlock(String... linesArray) {
        this.lines = new ArrayList<>(Arrays.asList(linesArray));
    }

    public String getLinesFromBottom(int number) {
        if(lines==null) return "";
        StringBuilder sb = new StringBuilder();
        int emptyLines = number - lines.size();
        for (int i = 0; i < emptyLines; i++) {
            sb.append(".\n");
        }
        for (int i = 0; i < lines.size(); i++) {
            sb.append(lines.get(i));
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getLinesFromTop(int number) {
        if(lines==null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            sb.append(lines.get(i));
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getLineByIndex(int index){
        return lines.get(index);
    }

    public int size(){
        return lines.size();
    }

    public static void main(String[] args) {
        LetterBlock letterBlock = new LetterBlock("1","2","3");
        System.out.println(letterBlock.getLinesFromBottom(4));
        LetterBlock letterBlock2 = new LetterBlock();
        System.out.println(letterBlock2.getLinesFromBottom(4));
    }

}
