package net.abegglen.tarif590.pdfwriter;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * created by Andrei Z. 11.10.2017
 */
public class LetterGenerator {

    // path to fonts which will be embedded in document
    private static String fontPath = "c:\\test\\";


    private static int BORDER_TYPE = 0;
    private static int PADDING_SIZE  = 2;

    /**
     * Static method which generating pdf letter from data contained in letterData parameter
     * @param letterData - data for letter
     * @return - binary representation of pdf document
     * @throws LetterGeneratorException
     */
    public static byte[] generate(GeneralLetterData letterData) throws LetterGeneratorException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            float bottomMargin = 50;
            float topMargin = 50;
            float leftMargin = 50;
            float rightMargin = 50;

            Document document = new Document(PageSize.A4, leftMargin, rightMargin, topMargin, bottomMargin);

            PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
            document.open();

            // create embedded base fonts regular and bold by loading it from files
            BaseFont baseFontReg = BaseFont.createFont(fontPath + "Lato-Regular.ttf", "UTF-8", true);
            BaseFont baseFontBold = BaseFont.createFont(fontPath + "Lato-Bold.ttf", "UTF-8", true);

            // create fonts to use in document based on embedded base fonts
            Font fontRegular = new Font(baseFontReg, 12, Font.NORMAL);
            Font fontBold = new Font(baseFontBold, 12, Font.NORMAL);

            // get access to content of pdf for put objects in absolute positions
            PdfContentByte cb = pdfWriter.getDirectContent();

            // firs table to put sender block
            PdfPTable table = new PdfPTable(new float[]{40, 60});
            table.setWidthPercentage(100.0f);
            table.setSpacingAfter(0);
            table.setSpacingBefore(0);
            for (int i = 0; i < letterData.getSender().size(); i++) {
                table.addCell(getSimpleCell(fontRegular, letterData.getSender().getLineByIndex(i), Element.ALIGN_RIGHT));
                table.addCell(getSimpleCell(fontRegular, "", Element.ALIGN_RIGHT));
            }
            document.add(table);
            // done

            // put spaces before the date block
            Paragraph paragraph = new Paragraph(14.0f, "\n\n\n\n\n\n", fontRegular);
            document.add(paragraph);

            // put main table with date, title and text blocks
            table = new PdfPTable(1);
            table.setWidthPercentage(90.0f);
            PdfPCell cell = getSimpleCell(fontRegular, letterData.getDate(), Element.ALIGN_LEFT);
            cell.setPaddingLeft(20.0f);
            cell.setFixedHeight(80.0f); // for spaces between blocks
            table.addCell(cell);

            cell = getSimpleCell(fontBold, letterData.getTitle(), Element.ALIGN_LEFT);
            cell.setPaddingLeft(20.0f);
            cell.setFixedHeight(50.0f); // for spaces between blocks
            table.addCell(cell);

            cell = getSimpleCell(fontRegular, letterData.getText(), Element.ALIGN_LEFT);
            cell.setPaddingLeft(20.0f);
            table.addCell(cell);

            document.add(table);
            // done

            // create a bottom table which will be contains salutation and senderPlus blocks
            PdfPTable bottomTable = new PdfPTable(new float[]{50, 50});
            bottomTable.setTotalWidth(table.getTotalWidth());
            bottomTable.setLockedWidth(true);
            bottomTable.setSpacingAfter(0);
            bottomTable.setSpacingBefore(0);

            // add salutation block in left cell
            cell = getSimpleCell(fontRegular, letterData.getSalutation(), Element.ALIGN_LEFT);
            cell.setPaddingLeft(20.0f);
            bottomTable.addCell(cell);

            // for senderPlus block we create new table and put it in right cell of bottom table
            // it's needed to set correct alignment positions
            PdfPTable senderPlusTable = new PdfPTable(1);
            senderPlusTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            for (int i = letterData.getSenderPlus().size() - 1; i >= 0; i--) {
                senderPlusTable.addCell(getSimpleCell(fontRegular, letterData.getSenderPlus().getLineByIndex(i), Element.ALIGN_RIGHT));
            }

            // create cell object for putting as right cell
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.addElement(senderPlusTable);
            cell.setBorder(BORDER_TYPE);

            bottomTable.addCell(cell);
            float tabHeight = bottomTable.getTotalHeight();

            // calculating absolute position of bottom table with block sender plus
            float xPos = (document.getPageSize().getWidth() - (bottomTable.getTotalWidth() + leftMargin + rightMargin)) / 2.0f;

            // write table at absolute position
            bottomTable.writeSelectedRows(0, 1, xPos + leftMargin, (bottomMargin + tabHeight), cb);
            // done

            //add recipient block at absolute position (text must be from bottom to top)
            PdfPTable recipientTable = new PdfPTable(1);
            for (int i = letterData.getRecipient().size() - 1; i >= 0; i--) {
                recipientTable.addCell(getSimpleCell(fontRegular, letterData.getRecipient().getLineByIndex(i), Element.ALIGN_LEFT));
            }
            recipientTable.setTotalWidth(mmToPoints(73));
            recipientTable.writeSelectedRows(0, table.getRows().size(), (float) mmToPoints(118), (float) mmToPoints(238), cb);
            //done

            //TODO load and put image into document

            // close document and stream
            document.close();
            outputStream.close();

        }catch (DocumentException | IOException ex){
            throw new LetterGeneratorException(ex);
        }

        return outputStream.toByteArray();
    }

    /**
     * Static method to generate pdf format letter and put it in to a file
     * @param letterData data for letter generation
     * @param fileName file name
     * @throws LetterGeneratorException
     */
    private static void generate(GeneralLetterData letterData, String fileName) throws LetterGeneratorException {
        byte[] pdfDocument = generate(letterData);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName, false);
            fileOutputStream.write(pdfDocument);
            fileOutputStream.close();
        }catch(IOException ex){
            throw new LetterGeneratorException(ex);
        }
    }

    /**
     * Simple method to convert millimeters to pdf points
     * @param mm given millimeters
     * @return pdf points
     */
    private static int mmToPoints(int mm){
        return (int)Math.round(mm * 2.8346);
    }


    /**
     * Static method for creating simple cell
     * Статический метод для создания ячейки, возвращает созданную ячейку.
     * @param font Шрифт / font
     * @param text Текст в ячейке / text in cell
     * @param alignment Выравнивание внутри ячейки / alignment
     * @return экземпляр класса PdfPCell / PdfPCell object
     */
    private static PdfPCell getSimpleCell(Font font, String text, int alignment){
        PdfPCell cell_main = new PdfPCell();

        Paragraph p = new Paragraph(12.0f);
        Chunk c = new Chunk(text, font);
        p.add(c);
        p.setAlignment(alignment);

        cell_main.addElement(p);
        cell_main.setBorder(BORDER_TYPE);

        cell_main.setPadding(PADDING_SIZE);

        return cell_main;
    }

}
