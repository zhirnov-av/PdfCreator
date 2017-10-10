package net.abegglen.tarif590.pdfwriter;

/**
 * Created by heinz on 10.10.2017.
 */
public class GeneralLetterData {

    private LetterBlock sender;
    private LetterBlock recipient;
    private String date;
    private String title;
    private String text;
    private String salutation;
    private LetterBlock senderPlus;
    private String imagePath;

    public LetterBlock getSender() {
        return sender;
    }

    public void setSender(LetterBlock sender) {
        this.sender = sender;
    }

    public LetterBlock getRecipient() {
        return recipient;
    }

    public void setRecipient(LetterBlock recipient) {
        this.recipient = recipient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public LetterBlock getSenderPlus() {
        return senderPlus;
    }

    public void setSenderPlus(LetterBlock senderPlus) {
        this.senderPlus = senderPlus;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
