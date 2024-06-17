package io.github.pomzwj.dbexport.core.filegeneration.md;

import com.google.common.base.Joiner;
import io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant;
import io.github.pomzwj.dbexport.core.filegeneration.AbstractFileGenerationService;
import io.github.pomzwj.dbexport.core.utils.ClassUtils;
import io.github.pomzwj.dbexport.core.utils.StringUtils;
import io.github.pomzwj.dbexport.core.domain.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出MD
 *
 * @author PomZWJ
 */
public class MarkdownOperatorService extends AbstractFileGenerationService {
    static final Logger log = LoggerFactory.getLogger(MarkdownOperatorService.class);
    static final String lineSeparator = System.getProperty("line.separator");

    private MarkdownOperatorService() {

    }

    private static volatile MarkdownOperatorService siglegle;

    public static MarkdownOperatorService getInstance() {
        if (siglegle == null)
            synchronized (MarkdownOperatorService.class) {
                if (siglegle == null)
                    siglegle = new MarkdownOperatorService();
            }
        return siglegle;
    }

    @Override
    protected void makeFileStream(List<DbTable> tableList, File targetFile) throws Exception {
        DbExportConfig dbExportConfig = dbExportConfigThreadLocal.get();
        DbBaseInfo dbBaseInfo = dbBaseInfoThreadLocal.get();
        Class<? extends DbColumnInfo> columnInfoClazz = dbExportConfig.getDbColumnInfoDynamicClazz();
        Class<? extends DbIndexInfo> indexInfoClazz = dbExportConfig.getDbIndexInfoDynamicClazz();
        StringBuffer content = new StringBuffer("");
        List<String> tableSpliter = new ArrayList<>();
        List<String> tableIndexSpliter = new ArrayList<>();
        for (int i = 0; i < ClassUtils.sortColumnField(columnInfoClazz).size(); i++) {
            tableSpliter.add(":-----:");
        }
        for (int i = 0; i < ClassUtils.sortIndexField(indexInfoClazz).size(); i++) {
            tableIndexSpliter.add(":-----:");
        }
        for (int i = 0; i < tableList.size(); i++) {
            DbTable dbTable = tableList.get(i);
            //创建表名列
            this.createTitleRow(i, dbTable.getTableName(), dbTable.getTableComments(), content);

            this.createDataRow(dbTable, columnInfoClazz,indexInfoClazz, tableSpliter,tableIndexSpliter, content);
        }
        FileUtils.write(targetFile, content.toString(), DataBaseConfigConstant.DEFAULT_ENCODE);
    }

    private void createTableRow(DbTable dbTable, Class<? extends DbColumnInfo> columnInfoClazz,
                                List<String> tableSpliter, StringBuffer content) throws Exception {
        List<String> columnHeaderName = this.getColumnHeaderName(columnInfoClazz);
        content.append("|").append(Joiner.on("|").join(columnHeaderName)).append("|").append(lineSeparator);
        content.append("|").append(Joiner.on("|").join(tableSpliter)).append("|").append(lineSeparator);
        List<List<String>> tableColumnData = this.getTableColumnData(dbTable, columnInfoClazz);
        for (int i = 0; i < tableColumnData.size(); i++) {
            List<String> rowValue = tableColumnData.get(i);
            content.append("|").append(Joiner.on("| ").join(rowValue)).append("|").append(lineSeparator);
        }
    }

    private void createTableIndexRow(DbTable dbTable, Class<? extends DbIndexInfo> indexInfoClazz,
                                     List<String> tableSpliter, StringBuffer content) throws Exception {
        List<String> indexHeaderName = this.getIndexHeaderName(indexInfoClazz);
        content.append("|").append(Joiner.on("|").join(indexHeaderName)).append("|").append(lineSeparator);
        content.append("|").append(Joiner.on("|").join(tableSpliter)).append("|").append(lineSeparator);
        List<List<String>> tableColumnData = this.getTableIndexData(dbTable, indexInfoClazz);
        for (int i = 0; i < tableColumnData.size(); i++) {
            List<String> rowValue = tableColumnData.get(i);
            content.append("|").append(Joiner.on("| ").join(rowValue)).append("|").append(lineSeparator);
        }

    }

    private void createDataRow(DbTable dbTable, Class<? extends DbColumnInfo> columnInfoClazz,Class<? extends DbIndexInfo> indexInfoClazz, List<String> tableSpliter,List<String> tableIndexSpliter, StringBuffer content) throws Exception {
        this.createTableRow(dbTable, columnInfoClazz, tableSpliter, content);
        content.append(lineSeparator);
        if(CollectionUtils.isNotEmpty(dbTable.getTabsIndex())){
            content.append("#### 索引信息").append(lineSeparator);
            content.append(lineSeparator);
            this.createTableIndexRow(dbTable, indexInfoClazz, tableIndexSpliter, content);
        }
    }

    private void createTitleRow(int index, String tableName, String tableComments, StringBuffer content) {
        content.append(lineSeparator).append("### ").append(index + 1).append(".").append(tableName);
        if (StringUtils.isNotEmpty(tableComments)) {
            content.append("(").append(tableComments).append(")").append(lineSeparator).append(lineSeparator);
        }
    }
}
