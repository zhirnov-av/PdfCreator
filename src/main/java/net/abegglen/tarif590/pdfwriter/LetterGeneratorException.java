package net.abegglen.tarif590.pdfwriter;

public class LetterGeneratorException extends Exception {
    public LetterGeneratorException(String message){
        super(message);
    }
    public LetterGeneratorException(Throwable e){
        super(e);
    }
}
