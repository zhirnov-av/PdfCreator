package net.abegglen.tarif590.pdfwriter;

public class Main {

    public static void main(String[] args) {
        try{
            GeneralLetterData generalLetterData = new GeneralLetterData();
            generalLetterData.setRecipient(new LetterBlock("3000 Bern\nTannenweg 25\nFrau Kairin Muster\nFirma Muster\n"));
            generalLetterData.setSender(new LetterBlock("Musterfirma AG\nHauptstrasse 78\nCH-8375 Niederbergen\n\nTelefon +41 (0)71 667 30 90\nDirekt +41 (0)71 667 30 77\n\njosef.minder@musterfirma.ch\nwww.musterfirma.ch"));
            generalLetterData.setDate("Niederbergen, 11.10.2017");
            generalLetterData.setTitle("Terminbestatigung");
            generalLetterData.setText("First row\n\nRow #3... is continue...\nlong text long text long text long text long text long text long text long text long text long text long text long text long text long text long text long text " +
                    "long text long text long text long text long text long text long text long text long text long text long text long text long text long text long text long text long text long text long text long text long text long text " +
                    " long text long text long text long text long text long text long text long text long text long text long text long text long text long text long text long text long text long text\n" +
                    "\n\tNext long text next long text next long text verylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongword");
            generalLetterData.setSalutation("Freundliche Grusse\n\n\nJosef Minder\nVerkhaufs- und Marketingleiter\n\n\nBeilagen\n> Anfahrtsplan");
            generalLetterData.setSenderPlus(new LetterBlock("Telefon +41 (0)71 667 30 90\nDirekt +41 (0)71 667 30 77"));
            generalLetterData.setImagePath("c:\\tmp\\intellij-logo.png");
            DeckblattGenerator.LetterGenerator.generate(generalLetterData, "c:\\tmp\\test.pdf");
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }
}
