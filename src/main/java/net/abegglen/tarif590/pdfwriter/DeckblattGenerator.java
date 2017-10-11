package net.abegglen.tarif590.pdfwriter;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DeckblattGenerator {


    public static class LetterGenerator {

        // path to fonts which will be embedded in document
        private static String fontPath = "c:\\tmp\\";

        private static Border BORDER_TYPE = Border.NO_BORDER;
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

                PdfWriter pdfWriter = new PdfWriter(outputStream);
                PdfDocument pdf = new PdfDocument(pdfWriter);

                Document document = new Document(pdf, PageSize.A4);


                //Document document = new Document(PageSize.A4, leftMargin, rightMargin, topMargin, bottomMargin);

                //document.open();

                // create embedded base fonts regular and bold by loading it from files
                //BaseFont baseFontReg = BaseFont.createFont(fontPath + "Lato-Regular.ttf", "UTF-8", true);
                //BaseFont baseFontBold = BaseFont.createFont(fontPath + "Lato-Bold.ttf", "UTF-8", true);

                // create fonts to use in document based on embedded base fonts
                //Font fontRegular = new Font(baseFontReg, 12, Font.NORMAL);
                //Font fontBold = new Font(baseFontBold, 12, Font.NORMAL);

                PdfFont fontRegular = PdfFontFactory.createFont(fontPath + "Lato-Regular.ttf", "UTF-8", true);
                PdfFont fontBold = PdfFontFactory.createFont(fontPath + "Lato-Bold.ttf", "UTF-8", true);

                // get access to content of pdf for put objects in absolute positions
                //PdfCanvas pdfCanvas = new PdfCanvas(document.getPdfDocument().getFirstPage());
                //PdfContentByte cb = pdfWriter.getDirectContent();

                // firs table to put sender block
                Table table = new Table(new float[]{40f, 60f});
                table.setWidthPercent(100.0f);
                table.setFixedLayout();
                table.setSpacingRatio(0f);
                //table.setWidthPercentage(100.0f);
                //table.setSpacingAfter(0);
                //table.setSpacingBefore(0);
                for (int i = 0; i < letterData.getSender().size(); i++) {
                    table.addCell(getSimpleCell(fontRegular, letterData.getSender().getLineByIndex(i), TextAlignment.RIGHT, HorizontalAlignment.RIGHT));
                    table.addCell(getSimpleCell(fontRegular, "", TextAlignment.RIGHT, HorizontalAlignment.RIGHT));
                }
                document.add(table);

                // put spaces before the date block
                Paragraph paragraph = new Paragraph(new Text("\n\n\n\n\n\n")).setFont(fontRegular).setFontSize(14.0f);

                document.add(paragraph);

                // put main table with date, title and text blocks
                Table table2 = new Table(new float[]{90.0f});
                Cell cell = getSimpleCell(fontRegular, letterData.getDate(), TextAlignment.LEFT, HorizontalAlignment.LEFT);
                cell.setPaddingLeft(20.0f);
                cell.setHeight(80.0f); // for spaces between blocks
                //cell.setFixedHeight(80.0f);
                table2.addCell(cell);

                cell = getSimpleCell(fontBold, letterData.getTitle(), TextAlignment.LEFT, HorizontalAlignment.LEFT);
                cell.setPaddingLeft(20.0f);
                cell.setHeight(50.0f); // for spaces between blocks
                table2.addCell(cell);

                cell = getSimpleCell(fontRegular, letterData.getText(), TextAlignment.LEFT, HorizontalAlignment.LEFT);
                cell.setPaddingLeft(20.0f);
                table2.addCell(cell);

                document.add(table2);

                // add image in absolute position left - 150mm, bottom of image from bottom page - 248mm
                Image image = new Image(ImageDataFactory.create(letterData.getImagePath()));
                image.setFixedPosition(mmToPoints(150), mmToPoints(248));
                document.add(image);
                //cb.addImage(image);

                // create a bottom table which will be contains salutation and senderPlus blocks
                Table bottomTable = new Table(new float[]{45, 45});

                //bottomTable.setTotalWidth(table.getTotalWidth());
                //bottomTable.setLockedWidth(true);
                //bottomTable.setSpacingAfter(0);
                //bottomTable.setSpacingBefore(0);

                // add salutation block in left cell
                cell = getSimpleCell(fontRegular, letterData.getSalutation(), TextAlignment.LEFT, HorizontalAlignment.LEFT);
                cell.setPaddingLeft(20.0f);
                bottomTable.addCell(cell);
                bottomTable.setBorderBottom(new SolidBorder(Color.BLACK, 1));
                bottomTable.setBorderTop(new SolidBorder(Color.BLACK, 1));
                bottomTable.setBorderLeft(new SolidBorder(Color.BLACK, 1));
                bottomTable.setBorderRight(new SolidBorder(Color.BLACK, 1));


                // for senderPlus block we create new table and put it in right cell of bottom table
                // it's needed to set correct alignment positions
                Table senderPlusTable = new Table(new float[]{100.0f});
                senderPlusTable.setWidthPercent(100f);
                senderPlusTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                for (int i = letterData.getSenderPlus().size() - 1; i >= 0; i--) {
                    senderPlusTable.addCell(getSimpleCell(fontRegular, letterData.getSenderPlus().getLineByIndex(i), TextAlignment.RIGHT, HorizontalAlignment.RIGHT));
                }

                // create cell object for putting as right cell
                cell = new Cell()
                        .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                        .setVerticalAlignment(VerticalAlignment.BOTTOM)
                        .add(senderPlusTable)
                        .setBorder(BORDER_TYPE);

                bottomTable.addCell(cell);



                float tabWidth = 90f * pdf.getFirstPage().getPageSize().getWidth() / 100f;
                float tabHeight = getTableHeight(document, bottomTable, tabWidth);



                // calculating absolute position of bottom table with block sender plus
                float xPos = (pdf.getFirstPage().getPageSize().getWidth() - (tabWidth + leftMargin + rightMargin)) / 2.0f;



                bottomTable.setFixedPosition(xPos + leftMargin, (bottomMargin + tabHeight), tabWidth);
                document.add(bottomTable);

                /*
                // write table at absolute position
                bottomTable.writeSelectedRows(0, 1, xPos + leftMargin, (bottomMargin + tabHeight), cb);

                //add recipient block at absolute position (text must be from bottom to top)
                */
                writeBlockAtAbsolutePosition(pdf, fontRegular, mmToPoints(118), mmToPoints(238), mmToPoints(73), mmToPoints(25), letterData.getRecipient().getLinesArray());

                /*
                PdfPTable recipientTable = new PdfPTable(1);
                for (int i = letterData.getRecipient().size() - 1; i >= 0; i--) {
                    recipientTable.addCell(getSimpleCell(fontRegular, letterData.getRecipient().getLineByIndex(i), Element.ALIGN_LEFT));
                }
                recipientTable.setTotalWidth(mmToPoints(73));
                recipientTable.writeSelectedRows(0, table.getRows().size(), (float) mmToPoints(118), (float) mmToPoints(238), cb);
                // close document and stream
                */
                document.close();
                outputStream.close();

            }catch (IOException ex){
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
         * @param alignment Выравнивание внутри ячейки / alignment
         * @return экземпляр класса PdfPCell / PdfPCell object
         */
        private static Cell getSimpleCell(PdfFont font, String text, TextAlignment alignment, HorizontalAlignment ha){
            Cell cell_main = new Cell();

            Paragraph p = new Paragraph();
            p.setFont(font);
            cell_main.add(p.add(new Text(text)
                    .setFontSize(12.0f)
                    .setTextAlignment(alignment)).setHorizontalAlignment(ha));

            cell_main.setBorder(BORDER_TYPE);
            cell_main.setPadding(PADDING_SIZE);
            cell_main.setHorizontalAlignment(ha);

            cell_main.setPadding(0);


            cell_main.setBorderBottom(new SolidBorder(Color.BLACK, 1));
            cell_main.setBorderTop(new SolidBorder(Color.BLACK, 1));
            cell_main.setBorderLeft(new SolidBorder(Color.BLACK, 1));
            cell_main.setBorderRight(new SolidBorder(Color.BLACK, 1));

            return cell_main;
        }

        private static void writeBlockAtAbsolutePosition(PdfDocument doc, PdfFont font, float x, float y, float width, float height, String[] lines){
            PdfPage page = doc.getFirstPage();
            PdfCanvas pdfCanvas = new PdfCanvas(page);
            Rectangle rectangle = new Rectangle(x, y, width, height);
            //pdfCanvas.rectangle(rectangle);
            pdfCanvas.stroke();
            Canvas canvas = new Canvas(pdfCanvas, doc, rectangle);
            Paragraph p = new Paragraph();
            for(int i = 0; i < lines.length; i++){
                //p.add(new Text(lines[i] + "\n")).setFont(font).setFontSize(12f).set;
            }
            canvas.add(p);
            canvas.close();
            doc.close();
        }

        private static float getTableHeight(Document doc, Table table, float width){

            LayoutResult result = table.createRendererSubTree().setParent(doc.getRenderer()).layout(
                    new LayoutContext(new LayoutArea(1, new Rectangle(0, 0, width, 1e4f))));

            return result.getOccupiedArea().getBBox().getHeight();
        }


    }
}
