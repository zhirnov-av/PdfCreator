import net.abegglen.tarif590.pdfwriter.GeneralLetterData;
import net.abegglen.tarif590.pdfwriter.LetterBlock;
import net.abegglen.tarif590.pdfwriter.LetterGenerator;

public class Main {

    public static void main(String[] args) {
        try{
            GeneralLetterData generalLetterData = new GeneralLetterData();
            generalLetterData.setRecipient(new LetterBlock("3000 Bern\nTannenweg 25\nFrau Kairin Muster\nFirma Muster\n"));
            LetterGenerator.generate(generalLetterData, "c:\\tmp\\test.pdf");
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }
}
