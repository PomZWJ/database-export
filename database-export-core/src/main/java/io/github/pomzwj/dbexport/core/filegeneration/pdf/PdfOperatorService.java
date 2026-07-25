package io.github.pomzwj.dbexport.core.filegeneration.pdf;

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
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import io.github.pomzwj.dbexport.core.anno.DataColumnName;
import io.github.pomzwj.dbexport.core.anno.DbIndexName;
import io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant;
import io.github.pomzwj.dbexport.core.domain.*;
import io.github.pomzwj.dbexport.core.filegeneration.AbstractFileGenerationService;
import io.github.pomzwj.dbexport.core.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出 PDF 文件，基于 iText 生成包含表字段与索引信息的表格。
 *
 * @author PomZWJ
 */
public class PdfOperatorService extends AbstractFileGenerationService {
    static final Logger log = LoggerFactory.getLogger(PdfOperatorService.class);
    //表格头颜�?
    static final Color headerCustomColor = new DeviceRgb(68, 114, 196);
    static final PdfFont font;
    static final Paragraph indexParagraph;
    static {
        try {
            String charPath = DataBaseConfigConstant.SYSTEM_FILE_DIR + File.separator + DataBaseConfigConstant.CALLIGRAPHIC_TEMPLATE;
            font = PdfFontFactory.createFont(charPath+",0", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        indexParagraph = new Paragraph("索引信息");
        //设置字体，解决中文显示问�?
        indexParagraph.setFont(font);
        indexParagraph.setFontSize(15);
        indexParagraph.setBold();
        indexParagraph.setTextAlignment(TextAlignment.LEFT);
    }

    private PdfOperatorService() {

    }

    private static volatile PdfOperatorService instance;

    /**
     * 获取单例实例（双重检查锁定）。
     *
     * @return 单例实例
     */
    public static PdfOperatorService getInstance() {
        if (instance == null)
            synchronized (PdfOperatorService.class) {
                if (instance == null)
                    instance = new PdfOperatorService();
            }
        return instance;
    }

    /**
     * 将表数据写入 PDF 文件流。
     *
     * @param tableList  表数据列表
     * @param targetFile 目标文件
     * @throws Exception 抛出异常信息
     */
    @Override
    protected void makeFileStream(List<DbTable> tableList, File targetFile) throws Exception {
        DbExportConfig dbExportConfig = dbExportConfigThreadLocal.get();
        DbBaseInfo dbBaseInfo = dbBaseInfoThreadLocal.get();
        boolean searchIndex = dbExportConfig.isSearchIndex();
        Class<? extends DbColumnInfo> columnInfoClazz = dbExportConfig.getDbColumnInfoDynamicClazz();
        Class<? extends DbIndexInfo> indexInfoClazz = dbExportConfig.getDbIndexInfoDynamicClazz();
        List<Field> columnFields = ClassUtils.sortColumnField(columnInfoClazz);
        List<Field> indexFields = ClassUtils.sortIndexField(indexInfoClazz);
        List<Cell> columHeaderCells = createColumnHeaderCell(columnFields);
        List<Cell> indexHeaderCells = createIndexHeaderCell(indexFields);
        //1、创建流对象
        PdfWriter pdfWriter = new PdfWriter(targetFile);
        //2、创建文档对�?
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        //3、创建内容文档对�?
        Document document = new Document(pdfDocument);
        try {
            for (int i = 0; i < tableList.size(); i++) {
                DbTable dbTable = tableList.get(i);

                document.add(getTitle(i, dbTable));

                //设置表格每列宽度
                Table table = new Table(columHeaderCells.size());
                //设置表格宽度百分�?
                table.setWidth(UnitValue.createPercentValue(100));
                //加入表格
                for (Cell cell : columHeaderCells) {
                    table.addHeaderCell(cell);
                }
                List<List<String>> tableColumnData = this.getTableColumnData(dbTable, columnInfoClazz);
                for (List<String> item : tableColumnData) {
                    this.createColumnCell(table, item);
                }
                //输出表格
                document.add(table);

                if(searchIndex){
                    document.add(indexParagraph);

                    //设置表格每列宽度
                    Table indexTable = new Table(indexHeaderCells.size());
                    //设置表格宽度百分�?
                    indexTable.setWidth(UnitValue.createPercentValue(100));
                    //加入表格
                    for (Cell cell : indexHeaderCells) {
                        indexTable.addHeaderCell(cell);
                    }
                    List<List<String>> tableIndexData = this.getTableIndexData(dbTable, indexInfoClazz);
                    for (List<String> item : tableIndexData) {
                        this.createIndexCell(indexTable, item);
                    }
                    //输出表格
                    document.add(indexTable);
                }
            }
        } finally {
            document.close();
        }
    }

    private Paragraph getTitle(int index, DbTable dbTable) {
        String tableName = dbTable.getTableName();
        String tableComments = dbTable.getTableComments();
        Paragraph paragraph = new Paragraph((index+1) + "." + tableName + "(" + tableComments + ")");
        //设置字体，解决中文显示问�?
        paragraph.setFont(font);
        paragraph.setFontSize(15);
        paragraph.setBold();
        paragraph.setTextAlignment(TextAlignment.LEFT);
        return paragraph;
    }

    private void createColumnCell(Table table, List<String> tableColumnItem) {
        for (String item : tableColumnItem) {
            table.addCell(new Cell().add(new Paragraph(item == null ? "" : item).setFont(font).setFontSize(8))); // 设置自定义背景颜�?
        }
    }
    private void createIndexCell(Table table, List<String> tableIndexItem) {
        for (String item : tableIndexItem) {
            table.addCell(new Cell().add(new Paragraph(item == null ? "" : item).setFont(font).setFontSize(8))); // 设置自定义背景颜�?
        }
    }

    private List<Cell> createColumnHeaderCell(List<Field> columnFields) {
        List<Cell> resultList = new ArrayList<>(columnFields.size());
        for (Field field : columnFields) {
            Cell header = new Cell(1, 1); //一�?�?
            DataColumnName annotation = field.getAnnotation(DataColumnName.class);
            header.add(new Paragraph(annotation.name()))
                    .setFont(font) // 设置字体
                    .setFontSize(8)
                    .setFontColor(new DeviceRgb(255, 255, 255))
                    .setTextAlignment(TextAlignment.LEFT) // 居左
                    .setBackgroundColor(headerCustomColor); // 设置自定义背景颜�?
            resultList.add(header);
        }
        return resultList;
    }

    private List<Cell> createIndexHeaderCell(List<Field> indexFields) {
        List<Cell> resultList = new ArrayList<>(indexFields.size());
        for (Field field : indexFields) {
            Cell header = new Cell(1, 1); //一�?�?
            DbIndexName annotation = field.getAnnotation(DbIndexName.class);
            header.add(new Paragraph(annotation.name()))
                    .setFont(font) // 设置字体
                    .setFontSize(8)
                    .setFontColor(new DeviceRgb(255, 255, 255))
                    .setTextAlignment(TextAlignment.LEFT) // 居左
                    .setBackgroundColor(headerCustomColor); // 设置自定义背景颜�?
            resultList.add(header);
        }
        return resultList;
    }
}
