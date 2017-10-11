package net.abegglen.tarif590.pdfwriter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by heinz on 10.10.2017.
 * changed by Andrei on 10.10.2017.
 */
public class LetterBlock {

    private ArrayList<String> lines = new ArrayList<>();

    public LetterBlock(String lines) {
        //  replacing two line breaks with any char between line breaks
        //  need to excluding losing empty lines...
        lines = lines.replace("\n\n", "\n_\n");

        String[] linesArray = lines.split("\n");
        this.lines = new ArrayList<>(Arrays.asList(linesArray));

        // And now we must replace our char by space.
        for (int i = 0; i< this.lines.size(); i++)
            if (this.lines.get(i).equals("_"))
                this.lines.set(i, " ");

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
    //  This method need for building table in pdf line by line (get line by index)
    public String getLineByIndex(int index){
        return lines.get(index);
    }

    //  This method need for building table in pdf line by line (to get lines count)
    public int size(){
        return lines.size();
    }

    public String[] getLinesArray(){
        String[] array = new String[lines.size()];
        for (int i = 0; i<lines.size(); i++){
            array[i] = lines.get(i);
        }
        return array;
    }

    public static void main(String[] args) {
        LetterBlock letterBlock = new LetterBlock("1","2","3");
        System.out.println(letterBlock.getLinesFromBottom(4));
        LetterBlock letterBlock2 = new LetterBlock();
        System.out.println(letterBlock2.getLinesFromBottom(4));
    }

}
