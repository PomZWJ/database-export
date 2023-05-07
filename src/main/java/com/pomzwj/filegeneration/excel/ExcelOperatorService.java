package com.pomzwj.filegeneration.excel;

import com.pomzwj.anno.DataColumnName;
import com.pomzwj.constant.DataBaseType;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbColumnInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.filegeneration.FileGenerationService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出excel工具类
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
@Service
public class ExcelOperatorService implements FileGenerationService<XSSFWorkbook> {
    static final Logger log = LoggerFactory.getLogger(ExcelOperatorService.class);
    static final int COLUMN_WIDTH = 5000;

    /**
     *
     * @param dbBaseInfo
     * @param tableList
     * @return XSSFWorkbook
     * @throws Exception
     */
    @Override
    public XSSFWorkbook makeFile(DbBaseInfo dbBaseInfo, List<DbTable> tableList) throws Exception {
        String dbKind = dbBaseInfo.getDbKind();
        String dbName = dbBaseInfo.getDbName();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        Sheet sheet = ExcelOperatorService.createSheet(xssfWorkbook, dbName);
        DataBaseType dataBaseKind = DataBaseType.matchType(dbKind);
        List<String> columnNames = dataBaseKind.getColumnName();
        //合并单元格的数量
        int mergeColumnNum = columnNames.size();
        //设置每一列的宽度
        for (int i = 0; i < columnNames.size(); i++) {
            sheet.setColumnWidth(i, COLUMN_WIDTH);
        }
        try {
            int rowNum = -1;
            for (int i = 0; i < tableList.size(); i++) {
                rowNum++;
                DbTable dbTable = tableList.get(i);
                //创建表名列
                this.createTitleRow(sheet, mergeColumnNum, rowNum, dbTable.getTableName() + "(" + dbTable.getTableComments() + ")");
                //显示表头
                List<String> zhCnColumnName = this.getZhCnColumnName(columnNames);
                rowNum++;
                this.createHeaderRow(sheet, rowNum, zhCnColumnName);
                //创建每一列
                rowNum++;
                List<List<String>> tableColumnData = this.getTableColumnData(dbTable, columnNames);
                rowNum = this.createDataRow(sheet, rowNum, tableColumnData);
            }
        } catch (Exception e) {
            log.error("创建excel文档失败，原因是={}",e);
        }
        return xssfWorkbook;
    }

    private int createDataRow(Sheet sheet, int rowNum, List<List<String>> tableColumnData) {
        int valueLength = tableColumnData.size();
        for (int i = 0; i < valueLength; i++) {
            Row row = sheet.createRow(rowNum);
            List<String> columnValueList = tableColumnData.get(i);
            for (int j = 0; j < columnValueList.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(columnValueList.get(j));
            }
            rowNum++;
        }
        return rowNum++;
    }

    private void createHeaderRow(Sheet sheet, int rowNum, List<String> vals) {
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < vals.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(vals.get(i));
        }

    }

    private void createTitleRow(Sheet sheet, int mergeColumnNum, int rowNum, String val) {
        Row row = sheet.createRow(rowNum);
        Cell cell = row.createCell(0);
        cell.setCellValue(val);
        cell.setCellStyle(setMyCellStyle(sheet.getWorkbook()));
        CellRangeAddress titleRegion = new CellRangeAddress(rowNum, rowNum, 0, mergeColumnNum - 1);
        sheet.addMergedRegion(titleRegion);
        setCellRangeAddressBorderStyle(sheet, titleRegion);
    }

    /**
     * 获取表头的中文名
     *
     * @param columnNames
     * @return
     * @throws Exception
     */
    private List<String> getZhCnColumnName(List<String> columnNames) throws Exception {
        Class<DbColumnInfo> dbColumnInfoClass = DbColumnInfo.class;
        //获取表头
        List<String> headerList = new ArrayList<>();
        for (int i = 0; i < columnNames.size(); i++) {
            String filed = columnNames.get(i);
            Field declaredField = dbColumnInfoClass.getDeclaredField(filed);
            DataColumnName annotation = declaredField.getAnnotation(DataColumnName.class);
            String annoName = annotation.name();
            headerList.add(annoName);
        }
        return headerList;
    }

    private List<List<String>> getTableColumnData(DbTable dbTable, List<String> columnNames) throws Exception {
        Class<DbColumnInfo> dbColumnInfoClass = DbColumnInfo.class;
        List<DbColumnInfo> tabsColumn = dbTable.getTabsColumn();
        List<List<String>> dataBodys = new ArrayList<>();
        for (int k = 0; k < tabsColumn.size(); k++) {
            DbColumnInfo dbColumnInfo = tabsColumn.get(k);
            List<String> dataBody = new ArrayList<>();
            for (int j = 0; j < columnNames.size(); j++) {
                String fieldName = columnNames.get(j);
                fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = dbColumnInfoClass.getMethod("get" + fieldName, new Class[0]);
                dataBody.add(method.invoke(dbColumnInfo, new Object[0]).toString());
            }
            dataBodys.add(dataBody);
        }
        return dataBodys;
    }

    /**
     * 设置合并单元格边框 - 线条
     */
    private static void setCellRangeAddressBorderStyle(Sheet sheet, CellRangeAddress region) {
        // 合并单元格左边框样式
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setLeftBorderColor(IndexedColors.BLACK.getIndex(), region, sheet);

        // 合并单元格上边框样式
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setTopBorderColor(IndexedColors.BLACK.getIndex(), region, sheet);

        // 合并单元格右边框样式
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setRightBorderColor(IndexedColors.BLACK.getIndex(), region, sheet);

        // 合并单元格下边框样式
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBottomBorderColor(IndexedColors.BLACK.getIndex(), region, sheet);


    }

    private static CellStyle setMyCellStyle(Workbook workbook) {
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
        //左右居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //上下居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置背景色
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        //填充模式
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontHeightInPoints((short) 16);
        font.setFontName("宋体");
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 创建sheet
     *
     * @param wb
     * @param sheetName
     * @return
     */
    private static Sheet createSheet(Workbook wb, String sheetName) {
        Sheet sheet = wb.getSheet(sheetName);
        if (sheet == null) {
            sheet = wb.createSheet(sheetName);
        }
        return sheet;
    }


}
