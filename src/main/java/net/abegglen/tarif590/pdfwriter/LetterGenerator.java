package net.abegglen.tarif590.pdfwriter;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static net.abegglen.tarif590.pdfwriter.PdfCommon.BORDER_TYPE;
import static net.abegglen.tarif590.pdfwriter.PdfCommon.getSimpleCell;

public class LetterGenerator {

    private static PdfWriter pdfWriter;
    private static BaseFont baseFontReg;
    private static String fontPath = "c:\\tmp\\";

    public static byte[] generate(GeneralLetterData letterData) throws DocumentException, IOException {
        //TODO Change throws to LetterGeneratorException
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4, 10, 10, 10, 10);
        pdfWriter = PdfWriter.getInstance(document, outputStream);
        document.open();

        //TODO need to know which encoding must be
        baseFontReg = BaseFont.createFont(fontPath + "Lato-Regular.ttf", "CP1251", true);
        BaseFont baseFontBold = BaseFont.createFont(fontPath + "Lato-Bold.ttf", "CP1251", true);

        Font fontRegular = new Font(baseFontReg, 12, Font.NORMAL);
        Font fontBold = new Font(baseFontBold, 12, Font.NORMAL);

        Paragraph paragraph = new Paragraph(14.0f, "test embedded font\ntest embedded font\ntest embedded font", fontRegular);
        paragraph.add(new Chunk("test embedded font\ntest embedded font\ntest embedded font", fontRegular));
        paragraph.add(new Chunk("test embedded font\ntest embedded font\ntest embedded font", fontRegular));
        document.add(paragraph);

        paragraph = new Paragraph(14.0f, "test embedded bold font\ntest embedded bold font\ntest embedded bold font", fontBold);
        document.add(paragraph);

        //add recipient block at absolute position (text must be from bottom to top)
        PdfPTable table = new PdfPTable(1);
        for (int i = letterData.getRecipient().size() - 1; i >= 0 ; i--){
            table.addCell(getSimpleCell(fontRegular, letterData.getRecipient().getLineByIndex(i), Element.ALIGN_LEFT));
        }
        table.setTotalWidth(mmToPoints(73));
        PdfContentByte cb = pdfWriter.getDirectContent();
        table.writeSelectedRows(0,table.getRows().size(), (float)mmToPoints(118), (float)mmToPoints(238), cb);
        //done

        document.close();
        outputStream.close();

        return outputStream.toByteArray();
    }

    public static void generate(GeneralLetterData letterData, String fileName) throws Exception {
        byte[] pdfDocument = generate(letterData);

        FileOutputStream fileOutputStream = new FileOutputStream(fileName, false);
        fileOutputStream.write(pdfDocument);
        fileOutputStream.close();
    }

    private static int mmToPoints(int mm){
        return (int)Math.round(mm * 2.8346);
    }

}
