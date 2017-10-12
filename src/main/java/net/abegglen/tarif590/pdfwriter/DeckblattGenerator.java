package net.abegglen.tarif590.pdfwriter;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DeckblattGenerator {


    public static class LetterGenerator {


        private static String fontPath = "c:\\test\\"; // path to fonts which will be embedded in document

        private static Border BORDER_TYPE = Border.NO_BORDER;

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

                PdfWriter pdfWriter = new PdfWriter(outputStream);
                PdfDocument pdf = new PdfDocument(pdfWriter);

                PageSize pageSize = new PageSize(PageSize.A4);

                Document document = new Document(pdf, pageSize);
                document.setMargins(topMargin, rightMargin, bottomMargin, leftMargin);

                PdfFont fontRegular = PdfFontFactory.createFont(fontPath + "Lato-Regular.ttf", "UTF-8", true);
                PdfFont fontBold = PdfFontFactory.createFont(fontPath + "Lato-Bold.ttf", "UTF-8", true);

                // add image in absolute position left - 150mm, bottom of image from bottom page - 250mm
                if (letterData.getImagePath() != null && !letterData.getImagePath().isEmpty()) {
                    try { // if image can not be loaded, we may continue to build document
                        Image image = new Image(ImageDataFactory.create(letterData.getImagePath()), mmToPoints(150), mmToPoints(250));
                        //scale image to 30mm X 30mm
                        image.scaleAbsolute(mmToPoints(30), mmToPoints(30));
                        document.add(image);
                    }catch (Exception ex){
                        // nothing to do...
                    }
                }

                // firs table to put sender block
                Table table = new Table(new float[]{40f, 60f})
                        .setWidthPercent(100.0f)
                        .setFixedLayout();

                for (int i = 0; i < letterData.getSender().size(); i++) {
                    table.addCell(getSimpleCell(fontRegular, letterData.getSender().getLineByIndex(i))).setTextAlignment(TextAlignment.RIGHT);
                    table.addCell(getSimpleCell(fontRegular, ""));
                }
                document.add(table);

                // put spaces before the date block
                document.add(new Paragraph("\n\n\n\n\n\n")
                        .setFont(fontRegular)
                        .setFontSize(14.0f)
                        .setFixedLeading(12f));

                // put main table with date, title and text blocks
                Table mainTable = new Table(new float[]{100.0f})
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setFixedLayout()
                        .setWidthPercent(90f)
                        .addCell(getSimpleCell(fontRegular, letterData.getDate())
                                .setPaddingLeft(20f)
                                .setHeight(80f) // for spaces between blocks
                                .setVerticalAlignment(VerticalAlignment.TOP))
                        .addCell(getSimpleCell(fontBold, letterData.getTitle())
                                .setPaddingLeft(20f)
                                .setHeight(50f)) // for spaces between blocks
                        .addCell(getSimpleCell(fontRegular, letterData.getText())
                                .setPadding(20f)
                                .setTextAlignment(TextAlignment.JUSTIFIED)
                                .setPaddingRight(3f));
                document.add(mainTable);


                // create a bottom table which will be contains salutation and senderPlus blocks
                Table bottomTable = new Table(new float[]{50f, 50f});
                bottomTable.setFixedLayout();

                // add salutation block in left cell
                bottomTable.addCell(getSimpleCell(fontRegular, letterData.getSalutation())
                                .setPaddingLeft(20.0f));

                // for senderPlus block we create new table and put it in right cell of bottom table
                // it's needed to set correct alignment positions
                Table senderPlusTable = new Table(new float[]{100.0f})
                        .setWidthPercent(90f)
                        .setHorizontalAlignment(HorizontalAlignment.RIGHT);
                for (int i = letterData.getSenderPlus().size() - 1; i >= 0; i--) {
                    senderPlusTable.addCell(getSimpleCell(fontRegular, letterData.getSenderPlus().getLineByIndex(i)).setTextAlignment(TextAlignment.RIGHT));
                }

                bottomTable.addCell(new Cell()
                        .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                        .setVerticalAlignment(VerticalAlignment.BOTTOM)
                        .add(senderPlusTable.setHorizontalAlignment(HorizontalAlignment.RIGHT))
                        .setBorder(BORDER_TYPE));

                float tabWidth = ( 90f * ( pdf.getFirstPage().getPageSize().getWidth() - leftMargin - rightMargin) / 100f );

                // calculating absolute position of bottom table with block sender plus
                float xPos = (pdf.getFirstPage().getPageSize().getWidth() - (tabWidth)) / 2.0f;
                bottomTable.setFixedPosition(xPos, bottomMargin, tabWidth);
                document.add(bottomTable);

                Table recipientTable = new Table(new float[]{100f})
                        .setFixedLayout()
                        .setFixedPosition(mmToPoints(118), mmToPoints(213), new UnitValue(mmToPoints(73), UnitValue.POINT));

                for (int i = letterData.getRecipient().size() - 1; i >= 0; i--) {
                    recipientTable.addCell(getSimpleCell(fontRegular, letterData.getRecipient().getLineByIndex(i))
                            .setTextAlignment(TextAlignment.LEFT)
                            .setPaddingLeft(0f)
                            .setPaddingTop(0f));
                }
                document.add(recipientTable);

                // close document and stream
                document.close();
                outputStream.close();

            }catch (IOException ex){
                throw new LetterGeneratorException(ex.getMessage());
            }

            return outputStream.toByteArray();
        }

        /**
         * Static method to generate pdf format letter and put it in to a file
         * @param letterData data for letter generation
         * @param fileName file name
         * @throws LetterGeneratorException
         */
        public static void generate(GeneralLetterData letterData, String fileName) throws LetterGeneratorException {
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
         * @return экземпляр класса PdfPCell / PdfPCell object
         */
        private static Cell getSimpleCell(PdfFont font, String text){
            text = text.isEmpty() ? "\n" : text;
            return new Cell()
                    .add(new Paragraph()
                            .setFont(font)
                            .setFixedLeading(12f)
                            .add(new Text(text)
                                    .setFontSize(12f)))
                    .setBorder(BORDER_TYPE)
                    .setPadding(0f)
                    .setMargin(0f);

        }
    }
}
