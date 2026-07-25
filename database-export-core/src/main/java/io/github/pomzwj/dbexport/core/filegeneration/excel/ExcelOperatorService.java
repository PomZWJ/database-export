package io.github.pomzwj.dbexport.core.filegeneration.excel;

import io.github.pomzwj.dbexport.core.domain.DbBaseInfo;
import io.github.pomzwj.dbexport.core.domain.DbColumnInfo;
import io.github.pomzwj.dbexport.core.domain.DbExportConfig;
import io.github.pomzwj.dbexport.core.domain.DbIndexInfo;
import io.github.pomzwj.dbexport.core.domain.DbTable;
import io.github.pomzwj.dbexport.core.filegeneration.AbstractFileGenerationService;
import io.github.pomzwj.dbexport.core.utils.ClassUtils;
import io.github.pomzwj.dbexport.core.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 导出Excel，每张表一个sheet
 *
 * @author PomZWJ
 */
public class ExcelOperatorService extends AbstractFileGenerationService {
    static final Logger log = LoggerFactory.getLogger(ExcelOperatorService.class);

    /** Excel sheet 名称最大长度限制 */
    private static final int SHEET_NAME_MAX_LENGTH = 31;

    private ExcelOperatorService() {
    }

    private static volatile ExcelOperatorService instance;

    /**
     * 获取单例实例（双重检查锁定）。
     *
     * @return 单例实例
     */
    public static ExcelOperatorService getInstance() {
        if (instance == null)
            synchronized (ExcelOperatorService.class) {
                if (instance == null)
                    instance = new ExcelOperatorService();
            }
        return instance;
    }

    /**
     * 将表数据写入 Excel 文件流，每张表一个 sheet。
     *
     * @param tableList  表数据列表
     * @param targetFile 目标文件
     * @throws Exception 抛出异常信息
     */
    @Override
    protected void makeFileStream(List<DbTable> tableList, File targetFile) throws Exception {
        DbExportConfig dbExportConfig = dbExportConfigThreadLocal.get();
        Class<? extends DbColumnInfo> columnInfoClazz = dbExportConfig.getDbColumnInfoDynamicClazz();
        Class<? extends DbIndexInfo> indexInfoClazz = dbExportConfig.getDbIndexInfoDynamicClazz();

        Workbook workbook = new XSSFWorkbook();
        try {
            //表头加粗样式
            CellStyle headerStyle = createHeaderStyle(workbook);

            for (int i = 0; i < tableList.size(); i++) {
                DbTable dbTable = tableList.get(i);
                String sheetName = buildSheetName(i, dbTable);
                Sheet sheet = workbook.createSheet(sheetName);
                int rowIndex = 0;

                //写入字段信息表头
                List<String> columnHeaderName = this.getColumnHeaderName(columnInfoClazz);
                Row headerRow = sheet.createRow(rowIndex++);
                for (int c = 0; c < columnHeaderName.size(); c++) {
                    Cell cell = headerRow.createCell(c);
                    cell.setCellValue(columnHeaderName.get(c));
                    cell.setCellStyle(headerStyle);
                }
                //写入字段信息数据
                List<List<String>> tableColumnData = this.getTableColumnData(dbTable, columnInfoClazz);
                for (List<String> rowValue : tableColumnData) {
                    Row row = sheet.createRow(rowIndex++);
                    for (int c = 0; c < rowValue.size(); c++) {
                        row.createCell(c).setCellValue(rowValue.get(c));
                    }
                }

                //写入索引信息（若有），与字段信息之间空一行
                if (CollectionUtils.isNotEmpty(dbTable.getTabsIndex())) {
                    rowIndex++; //空行分隔
                    //索引小标题
                    Row indexTitleRow = sheet.createRow(rowIndex++);
                    Cell indexTitleCell = indexTitleRow.createCell(0);
                    indexTitleCell.setCellValue("索引信息");
                    indexTitleCell.setCellStyle(headerStyle);

                    List<String> indexHeaderName = this.getIndexHeaderName(indexInfoClazz);
                    Row indexHeaderRow = sheet.createRow(rowIndex++);
                    for (int c = 0; c < indexHeaderName.size(); c++) {
                        Cell cell = indexHeaderRow.createCell(c);
                        cell.setCellValue(indexHeaderName.get(c));
                        cell.setCellStyle(headerStyle);
                    }
                    List<List<String>> tableIndexData = this.getTableIndexData(dbTable, indexInfoClazz);
                    for (List<String> rowValue : tableIndexData) {
                        Row row = sheet.createRow(rowIndex++);
                        for (int c = 0; c < rowValue.size(); c++) {
                            row.createCell(c).setCellValue(rowValue.get(c));
                        }
                    }
                }
            }

            try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                workbook.write(fos);
            }
        } finally {
            workbook.close();
        }
    }

    /**
     * 构造 sheet 名称。
     * Excel sheet 名限制：最长31字符，不能包含 \ / ? * [ ] : 等特殊字符
     */
    private String buildSheetName(int index, DbTable dbTable) {
        String tableName = dbTable.getTableName();
        String tableComments = dbTable.getTableComments();
        String baseName = StringUtils.isNotEmpty(tableComments) ? (tableName + "(" + tableComments + ")") : tableName;
        //替换 Excel sheet 名不允许的特殊字符
        String safeName = baseName.replaceAll("[\\\\/?*\\[\\]:]", "_");
        if (safeName.length() > SHEET_NAME_MAX_LENGTH) {
            //超长则截断表名后追加序号，保证唯一性
            safeName = safeName.substring(0, SHEET_NAME_MAX_LENGTH - 4) + "_" + (index + 1);
        }
        return safeName;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
