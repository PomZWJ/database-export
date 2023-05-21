package com.pomzwj.filegeneration.md;

import com.google.common.base.Joiner;
import com.pomzwj.anno.DataColumnName;
import com.pomzwj.constant.DataBaseType;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbColumnInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.filegeneration.AbstractFileGenerationService;
import com.pomzwj.filegeneration.FileGenerationService;
import com.pomzwj.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出MD
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
@Service
public class MarkdownOperatorService extends AbstractFileGenerationService {
    static final Logger log = LoggerFactory.getLogger(MarkdownOperatorService.class);
    static final String lineSeparator = System.getProperty("line.separator");

    @Override
    protected void makeFileStream(DbBaseInfo dbBaseInfo, List<DbTable> tableList, File targetFile) throws Exception {
        String dbKind = dbBaseInfo.getDbKind();
        DataBaseType dataBaseKind = DataBaseType.matchType(dbKind);
        List<String> columnNames = dataBaseKind.getColumnName();
        StringBuffer content = new StringBuffer("");
        List<String>tableSpliter = new ArrayList<>();
        for(int i=0;i<columnNames.size();i++){
            tableSpliter.add(":-----:");
        }
        for (int i = 0; i < tableList.size(); i++) {
            DbTable dbTable = tableList.get(i);
            //创建表名列
            this.createTitleRow(i,dbTable.getTableName(), dbTable.getTableComments(), content);
            //显示表头
            List<String> zhCnColumnName = this.getZhCnColumnName(columnNames);
            this.createDataRow(dbTable, zhCnColumnName, columnNames, tableSpliter, content);
        }
        FileUtils.write(targetFile,content.toString(),"utf-8");
    }

    private void createDataRow(DbTable dbTable,List<String> zhCnColumnName,List<String> columnNames,
                               List<String>tableSpliter,StringBuffer content)throws Exception{
        content.append("|").append(Joiner.on("|").join(zhCnColumnName)).append("|").append(lineSeparator);
        content.append("|").append(Joiner.on("|").join(tableSpliter)).append("|").append(lineSeparator);
        List<List<String>> tableColumnData = this.getTableColumnData(dbTable, columnNames);
        for(int i=0;i<tableColumnData.size();i++){
            List<String> rowValue = tableColumnData.get(i);
            content.append("|").append(Joiner.on("|").join(rowValue)).append("|").append(lineSeparator);
        }

    }
    private void createTitleRow(int index,String tableName,String tableComments,StringBuffer content){
        content.append(lineSeparator).append("### ").append(index+1).append(".").append(tableName);
        if(StringUtils.isNotEmpty(tableComments)){
            content.append("(").append(tableComments).append(")").append(lineSeparator);
        }
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


}
