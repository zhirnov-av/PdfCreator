/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.abegglen.tarif590.pdfwriter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import java.sql.Connection;


/**
 *
 * @author Zhirnov.AV
 */
public class PdfCommon {
    
    /**
     * Тип границы для всех основных элементов PDF, за исключением тех, где 
     * нужно явно их указать.<p>
     * <b>BORDER_TYPE</b> в библиотеке <i>itextpdf</i> определяется как число получаемое из 
     * четырех младших битов:<p>
     * <table width=100%>
     * <tr><td width=40%>двоичное представление</td><td width=20%>десятичное представление</td><td width=40%>Результат</td></tr>
     * <tr><td>0000 0000</td><td>00</td><td>нет границ</td></tr>
     * <tr><td>0000 0001</td><td>01</td><td>верхняя рамка</td></tr>
     * <tr><td>0000 0010</td><td>02</td><td>нижняя рамка</td></tr>
     * <tr><td>0000 0100</td><td>04</td><td>левая рамка</td></tr>
     * <tr><td>0000 1000</td><td>08</td><td>правая рамка</td></tr></table>
     * <table>
     * Комбинацией этих битов можно получить любую рамку, например:
     * <table width=100%>
     * <tr><td width=40%>0000 1111</td><td width=20%>15</td><td width=40%>все границы</td></tr>
     * <tr><td>0000 0011</td><td>03</td><td>верхняя и нижняя</td></tr>
     * <tr><td>0000 0101</td><td>05</td><td>верхняя и левая рамки</td></tr></table>
     * <p>
     * двоичное         десятичное          Результат<p>
     * представление    представление<p>   
     * 0000 0000        00                  нет границ<p>
     * 0000 0001        01                  верхняя рамка<p>
     * 0000 0010        02                  нижняя рамка<p>
     * 0000 0100        04                  левая рамка<p>
     * 0000 1000        08                  правая рамка<p>
     * <p>
     * Комбинацией этих битов можно получить любую рамку, например:<p>
     * 0000 1111        15                  все границы<p>
     * 0000 0011        03                  верхняя и нижняя<p>
     * 0000 0101        05                  верхняя и левая рамки<p>
     */
    protected static int BORDER_TYPE = 0;
    
    /**
     */
    protected static int PADDING_SIZE  = 2;
    
    
    /**
     * Статический метод для добавления ячейки в таблицу, выполняет все обычные
     * манипуляции с созданием параграфа, чанка и т.д. :)
     * @param table таблица в которую добавляем ячейку с текстом
     * @param fnt шрифт текста
     * @param text добавляемый текст
     */
    protected static void addSimpleCell(PdfPTable table, Font fnt, String text){
        
        PdfPCell cell_main = new PdfPCell();

        Paragraph p = new Paragraph(7.0f);
        Chunk c = new Chunk(text, fnt);
        p.add(c);
        p.setAlignment(Element.ALIGN_LEFT);

        cell_main.addElement(p);
        cell_main.setBorder(BORDER_TYPE);

        cell_main.setPadding(PADDING_SIZE);

        table.addCell(cell_main);
    }

    /**
     * Статический метод для добавления ячейки в таблицу, выполняет все обычные
     * манипуляции с созданием параграфа, чанка и т.д. :)
     * @param table таблица в которую добавляем ячейку с текстом
     * @param fnt шрифт текста
     * @param text добавляемый текст
     * @param alignment выравнивание
     */
    protected static void addSimpleCell(PdfPTable table, Font fnt, String text, int alignment){
        
        PdfPCell cell_main = new PdfPCell();

        Paragraph p = new Paragraph(7.0f);
        Chunk c = new Chunk(text, fnt);
        p.add(c);
        p.setAlignment(alignment);

        cell_main.addElement(p);
        cell_main.setBorder(BORDER_TYPE);

        cell_main.setPadding(PADDING_SIZE);

        table.addCell(cell_main);
    }

    /**
     * Статический метод для добавления ячейки в таблицу, выполняет все обычные
     * манипуляции с созданием параграфа, чанка и т.д. :)
     * @param table таблица в которую добавляем ячейку с текстом
     * @param fnt шрифт текста
     * @param text добавляемый текст
     * @param alignment выравнивание
     * @param border тип границы ячейки
     */
    protected static void addSimpleCell(PdfPTable table, Font fnt, String text, int alignment, int border){
        
        PdfPCell cell_main = new PdfPCell();

        Paragraph p = new Paragraph(7.0f);
        Chunk c = new Chunk(text, fnt);
        p.add(c);
        p.setAlignment(alignment);

        cell_main.addElement(p);
        cell_main.setBorder(border); 

        cell_main.setPadding(PADDING_SIZE);

        table.addCell(cell_main);
    }
    
    /**
     * Статический метод для добавления ячейки в таблицу, выполняет все обычные
     * манипуляции с созданием параграфа, чанка и т.д. :)
     * @param table таблица в которую добавляем ячейку с текстом
     * @param fnt шрифт текста
     * @param text добавляемый текст
     * @param alignment выравнивание
     * @param border тип границы ячейки
     * @param colspan количество объединяемых ячеек
     * @param rowspan количество объединяемых строк
     */
    protected static void addSimpleCell(PdfPTable table, Font fnt, String text, int alignment, int border, int colspan, int rowspan){
        
        PdfPCell cell_main = new PdfPCell();

        Paragraph p = new Paragraph(7.0f);
        Chunk c = new Chunk(text, fnt);
        p.add(c);
        p.setAlignment(alignment);

        cell_main.addElement(p);
        cell_main.setBorder(border); 

        cell_main.setPadding(PADDING_SIZE);
        cell_main.setRowspan(rowspan);
        cell_main.setColspan(colspan);

        table.addCell(cell_main);
    }
        
    
    /**
     * Статический метод для создания ячейки, возвращает созданную ячейку.
     * @param font Шрифт
     * @param text Текст в ячейке
     * @param alignment Выравнивание внутри ячейки
     * @return экземпляр класса PdfPCell
     */
    protected static PdfPCell getSimpleCell(Font font, String text, int alignment){
        
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
    
    /**
     * Статический метод для создания ячейки, возвращает созданную ячейку.
     * @param font Шрифт
     * @param text Текст в ячейке
     * @param alignment Выравнивание внутри ячейки
     * @param border Тип границы
     * @return экземпляр класса PdfPCell
     */
    protected static PdfPCell getSimpleCell(Font font, String text, int alignment, int border){
        
        PdfPCell cell_main = new PdfPCell();

        Paragraph p = new Paragraph(7.0f);
        Chunk c = new Chunk(text, font);
        p.add(c);
        p.setAlignment(alignment);

        cell_main.addElement(p);
        cell_main.setBorder(border);

        cell_main.setPadding(PADDING_SIZE);

        return cell_main;
    }
    
    /**
     * Статический метод для установки аттрибутов ячейки.
     * @param cell экземпляр ячейки, для которой настраиваем аттрибуты
     * @param border тип границы
     * @param padding отступы
     * @param colSpan сколько колонок занимает ячейка
     */
    protected static void addCellAttr(PdfPCell cell, int border, int padding, int colSpan){
        cell.setBorder(border);
        cell.setPadding(padding);
        cell.setColspan(colSpan);
    }

    /**
     * Статический метод для установки аттрибутов ячейки.
     * @param cell экземпляр ячейки, для которой настраиваем аттрибуты
     * @param border тип границы
     * @param padding отступы
     * @param colSpan сколько колонок занимает ячейка
     * @param rowSpan сколько строк занимает ячейка
     */
    protected static void addCellAttr(PdfPCell cell, int border, int padding, int colSpan, int rowSpan){
        cell.setBorder(border);
        cell.setPadding(padding);
        cell.setColspan(colSpan);
        cell.setRowspan(rowSpan);
    }
    
    /**
     * Статический метод для установки аттрибутов ячейки.
     * @param cell экземпляр ячейки, для которой настраиваем аттрибуты
     * @param border тип границы
     * @param padding отступы
     */
    protected static void addCellAttr(PdfPCell cell, int border, int padding){
        cell.setBorder(border);
        cell.setPadding(padding);
    }
    
    /**
     * Метод возвращает новый параграф
     * @param text текст
     * @param font шрифт
     * @return экземпляр класса <b>Paragraph</b>
     */
    protected static Paragraph getNewParagraph(String text, Font font){
        Paragraph p = new Paragraph(7.0f, new Chunk(text, font));
        p.setSpacingAfter(2f);
        
        return p;
    }
    
    /**
     * Метод, который возвращает ячейку, с расположенной в ней разделительной
     * линией формата: <p>
     * ------------- text -------------
     * @param font шрифт
     * @param text текст между точками
     * @return ячейка с разделительной линией (экземпляр класса <b>PdfPCell</b>)
     */
    protected static PdfPCell getCuttingLine(Font font, String text){
        
        
        PdfPTable table = new PdfPTable( new float[] {43.0f, 14.0f, 43.0f} );
        table.setWidthPercentage(100);
        table.setSpacingAfter(0);
        table.setSpacingBefore(0);
        
        PdfPCell cell = new PdfPCell();
        addCellAttr(cell, BORDER_TYPE, 2);
        
        DottedLineSeparator dottedLine = new DottedLineSeparator();
        dottedLine.setGap(3);
        dottedLine.setLineColor(BaseColor.BLACK);
        dottedLine.setLineWidth(1.0f);
        dottedLine.setAlignment(Element.ALIGN_MIDDLE);
        
        Paragraph p = new Paragraph(new Chunk(dottedLine));
        p.setAlignment(Element.ALIGN_CENTER);
        
        cell.addElement(dottedLine);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        table.addCell(cell);        


        PdfPCell cell2 = getSimpleCell(font, text, Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell2);        
        

        table.addCell(cell);        
        
        cell = new PdfPCell();
        addCellAttr(cell, BORDER_TYPE, 2);
        
        cell.addElement(table);
        
        return cell;
    }
    
    /**
     * Метод возвращающий новую таблицу, с предустановленными стандартными 
     * параметрами.
     * <p>Width=100%, SpacingAfter=0, SpacingBefore=0
     * @param arrayWidth массив, каждый элемент - ширина колонки
     * @return экземпляр класса <b>PdfPTable</b>
     */
    protected static PdfPTable getNewTable(float arrayWidth[]){
        
        PdfPTable table = new PdfPTable(arrayWidth);
        table.setWidthPercentage(100);
        table.setSpacingAfter(0);
        table.setSpacingBefore(0);
        
        return table;
    }
    
}
