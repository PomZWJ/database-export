package io.github.pomzwj.dbexport.core;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;

import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant;
import org.junit.jupiter.api.Test;

public class PdfTest {
    @Test
    public void downLoad() throws Exception {
        //生成的pdf路径+名称
        String pdf = "D://test.pdf";
        String charPath = DataBaseConfigConstant.SYSTEM_FILE_DIR + File.separator + DataBaseConfigConstant.CALLIGRAPHIC_TEMPLATE;
        PdfFont font = PdfFontFactory.createFont(charPath+",0", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

        //1、创建流对象
        PdfWriter pdfWriter=new PdfWriter(new File(pdf));
        //2、创建文档对象
        PdfDocument pdfDocument=new PdfDocument(pdfWriter);
        //3、创建内容文档对象
        Document document=new Document(pdfDocument);

        //创建内容
        Paragraph paragraph=new Paragraph("报名信息");
        //设置字体，解决中文显示问题
        paragraph.setFont(font);
        paragraph.setFontSize(15);
        paragraph.setBold();
        paragraph.setTextAlignment(TextAlignment.LEFT);//居中
        document.add(paragraph);

        //创建表头
        Cell head1=new Cell(1,1); //一行9列
        Cell head2=new Cell(1,1); //一行9列
        Cell head3=new Cell(1,1); //一行9列
        Cell head4=new Cell(1,1); //一行9列
        //创建标题
        Color customColor = new DeviceRgb(68,114,196); //这是一个橙色示例，您可以替换为其他RGB值
        head1.add(new Paragraph("姓名"))
                .setFont(font) // 设置字体
                .setFontSize(8)
                .setFontColor(new DeviceRgb(255,255,255))
                .setTextAlignment(TextAlignment.LEFT) // 居左
                .setBackgroundColor(customColor); // 设置自定义背景颜色
        head2.add(new Paragraph("性别"))
                .setFont(font) // 设置字体
                .setFontSize(8)
                .setFontColor(new DeviceRgb(255,255,255))
                .setTextAlignment(TextAlignment.LEFT) // 居左
                .setBackgroundColor(customColor); // 设置自定义背景颜色

        head3.add(new Paragraph("是否独生子女"))
                .setFont(font) // 设置字体
                .setFontSize(8)
                .setFontColor(new DeviceRgb(255,255,255))
                .setTextAlignment(TextAlignment.LEFT) // 居左
                .setBackgroundColor(customColor); // 设置自定义背景颜色
        head4.add(new Paragraph("血型"))
                .setFont(font) // 设置字体
                .setFontSize(8)
                .setFontColor(new DeviceRgb(255,255,255))
                .setTextAlignment(TextAlignment.LEFT) // 居左
                .setBackgroundColor(customColor); // 设置自定义背景颜色

        Cell cell1=new Cell().add(new Paragraph("赵文杰").setFont(font).setFontSize(8));
        Cell cell2=new Cell().add(new Paragraph("男").setFont(font).setFontSize(8));
        Cell cell3=new Cell().add(new Paragraph("是").setFont(font).setFontSize(8));
        Cell cell4=new Cell().add(new Paragraph("A").setFont(font).setFontSize(8));

        //设置表格每列宽度
        Table table=new Table(4);
        //设置表格宽度百分比
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);

        //加入表格
        table.addHeaderCell(head1);
        table.addHeaderCell(head2);
        table.addHeaderCell(head3);
        table.addHeaderCell(head4);


        //设置表格每列宽度
        Table table2=new Table(4);
        //设置表格宽度百分比
        table2.setWidth(UnitValue.createPercentValue(100));
        table2.addCell(cell1);
        table2.addCell(cell2);
        table2.addCell(cell3);
        table2.addCell(cell4);

        //加入表格
        table2.addHeaderCell(head1);
        table2.addHeaderCell(head2);
        table2.addHeaderCell(head3);
        table2.addHeaderCell(head4);

        //输出表格
        document.add(table);

        document.add(table2);
        document.close();
        System.out.println("pdf生成完成!");
    }
}
