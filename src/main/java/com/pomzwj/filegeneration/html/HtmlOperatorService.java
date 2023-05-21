package com.pomzwj.filegeneration.html;

import com.pomzwj.anno.DataColumnName;
import com.pomzwj.constant.DataBaseType;
import com.pomzwj.constant.SystemConstant;
import com.pomzwj.constant.TemplateFileConstants;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbColumnInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.filegeneration.AbstractFileGenerationService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class HtmlOperatorService extends AbstractFileGenerationService {
    @Override
    protected void makeFileStream(DbBaseInfo dbBaseInfo, List<DbTable> tableList, File targetFile) throws Exception {
        FileInputStream inputStream = null;
        try{
            String dbKind = dbBaseInfo.getDbKind();
            DataBaseType dataBaseKind = DataBaseType.matchType(dbKind);
            List<String> columnNames = dataBaseKind.getColumnName();
            StringBuffer title = new StringBuffer("<ol>");
            StringBuffer content = new StringBuffer("");
            for (int i = 0; i < tableList.size(); i++) {
                DbTable dbTable = tableList.get(i);
                //创建表名列
                this.createTileRow(i,dbTable.getTableName(), dbTable.getTableComments(), title);
                //显示表头
                List<String> zhCnColumnName = this.getZhCnColumnName(columnNames);
                this.createDataRow(i,dbTable, zhCnColumnName, columnNames, content);
            }
            title.append("</ol>");
            inputStream = new FileInputStream(SystemConstant.SYSTEM_FILE_FIR+File.separator+ TemplateFileConstants.HTML_TEMPLATE);
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String str = result.toString(StandardCharsets.UTF_8.name());
            str = str.replace("${data}", content.toString()).replace("${catalogue}", title.toString());
            FileUtils.write(targetFile, str, "utf-8");
        }catch (Exception e){
            throw e;
        }finally {
            if(inputStream != null){
                inputStream.close();
            }
        }

    }

    private void createDataRow(int index,DbTable dbTable, List<String> zhCnColumnName, List<String> columnNames, StringBuffer content) throws Exception {
        content.append(String.format("<h2 id='%s'>%s.%s(%s)</h2>", dbTable.getTableName(),index+1, dbTable.getTableName(), dbTable.getTableComments()));
        content.append("<p></p>");
        content.append("<table>");
        content.append("<tr>");
        for (int i = 0; i < zhCnColumnName.size(); i++) {
            content.append(String.format("<th>%s</th>", zhCnColumnName.get(i)));
        }
        content.append("</tr>");
        List<List<String>> tableColumnData = this.getTableColumnData(dbTable, columnNames);


        for (int i = 0; i < tableColumnData.size(); i++) {
            content.append("<tr>");
            for(int j = 0 ; j < tableColumnData.get(i).size() ; j++){
                content.append(String.format("<td>%s</th>", tableColumnData.get(i).get(j)));
            }
            content.append("</tr>");
        }
        content.append("</table>");
    }

    private void createTileRow(int index,String tableName, String tableComments, StringBuffer title) {
        title.append(String.format("<li><a href='#%s' title='%s(%s)'>%s.%s(%s)</a>", tableName, tableName, tableComments,index+1, tableName, tableComments));
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
