package io.github.pomzwj.dbexport.core.filegeneration.html;

import io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant;
import io.github.pomzwj.dbexport.core.filegeneration.AbstractFileGenerationService;
import io.github.pomzwj.dbexport.core.domain.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HtmlOperatorService extends AbstractFileGenerationService {

    private HtmlOperatorService(){

    }

    private static volatile HtmlOperatorService siglegle;

    public static HtmlOperatorService getInstance() {
        if(siglegle == null)
            synchronized(HtmlOperatorService.class) {
                if (siglegle == null)
                    siglegle = new HtmlOperatorService();
            }
        return siglegle;
    }
    @Override
    protected void makeFileStream(List<DbTable> tableList, File targetFile) throws Exception {
        FileInputStream inputStream = null;
        try{
            StringBuffer title = new StringBuffer("<ol>");
            StringBuffer content = new StringBuffer("");
            for (int i = 0; i < tableList.size(); i++) {
                DbTable dbTable = tableList.get(i);
                //创建表名列
                this.createTileRow(i,dbTable.getTableName(), dbTable.getTableComments(), title);
                this.createDataRow(i,dbTable, content);
            }
            title.append("</ol>");
            inputStream = new FileInputStream(DataBaseConfigConstant.SYSTEM_FILE_DIR +File.separator+ DataBaseConfigConstant.HTML_TEMPLATE);
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String str = result.toString(StandardCharsets.UTF_8.name());
            str = str.replace("${data}", content.toString()).replace("${catalogue}", title.toString());
            FileUtils.write(targetFile, str, DataBaseConfigConstant.DEFAULT_ENCODE);
        } finally {
            if(inputStream != null){
                inputStream.close();
            }
        }

    }

    private void createDataRow(int index,DbTable dbTable,StringBuffer content) throws Exception {
        this.createTableRow(index,dbTable,content);
        if(CollectionUtils.isNotEmpty(dbTable.getTabsIndex())){
            this.createTableIndexRow(index,dbTable,content);
        }
    }

    private void createTableRow(int index,DbTable dbTable, StringBuffer content)throws Exception{
        Class<? extends DbColumnInfo> columnInfoClazz = dbExportConfigThreadLocal.get().getDbColumnInfoDynamicClazz();
        List<String> columnHeaderName = this.getColumnHeaderName(columnInfoClazz);
        content.append(String.format("<h2 id='%s'>%s.%s(%s)</h2>", dbTable.getTableName(),index+1, dbTable.getTableName(), dbTable.getTableComments()));
        content.append("<p></p>");
        content.append("<table>");
        content.append("<tr>");
        for (int i = 0; i < columnHeaderName.size(); i++) {
            content.append(String.format("<th>%s</th>", columnHeaderName.get(i)));
        }
        content.append("</tr>");
        List<List<String>> tableColumnData = this.getTableColumnData(dbTable, columnInfoClazz);


        for (int i = 0; i < tableColumnData.size(); i++) {
            content.append("<tr>");
            for(int j = 0 ; j < tableColumnData.get(i).size() ; j++){
                content.append(String.format("<td>%s</th>", tableColumnData.get(i).get(j)));
            }
            content.append("</tr>");
        }
        content.append("</table>");
    }

    private void createTableIndexRow(int index, DbTable dbTable, StringBuffer content)throws Exception {
        if(CollectionUtils.isEmpty(dbTable.getTabsIndex())){
            return;
        }
        Class<? extends DbIndexInfo> indexInfoClazz = dbExportConfigThreadLocal.get().getDbIndexInfoDynamicClazz();
        List<String> indexHeaderName = this.getIndexHeaderName(indexInfoClazz);
        content.append("<h3>索引信息</h3>");
        content.append("<table>");
        content.append("<tr>");
        for (int i = 0; i < indexHeaderName.size(); i++) {
            content.append(String.format("<th>%s</th>", indexHeaderName.get(i)));
        }
        content.append("</tr>");
        List<List<String>> tableColumnData = this.getTableIndexData(dbTable, indexInfoClazz);


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


}
