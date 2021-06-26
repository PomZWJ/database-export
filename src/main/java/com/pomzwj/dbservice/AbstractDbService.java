package com.pomzwj.dbservice;

import com.pomzwj.domain.DbTable;
import com.pomzwj.domain.FiledDefaultValue;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * dbService中间类
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
public abstract class AbstractDbService {

    /**
     * 把List进行分页
     * @param dbTableList
     * @return
     */
    public List[] listToPageArray(List<DbTable> dbTableList){
        if(CollectionUtils.isEmpty(dbTableList)){
            throw new DatabaseExportException("该数据库中没有找到任何表");
        }
        int totalNum = dbTableList.size();
        List[] listArray = null;
        //如果表数量大于100则启用多线程
        if (totalNum < 100) {
            listArray = new ArrayList[1];
            listArray[0] = dbTableList;
        } else {
            int pageSize = 10;
            int totalPage = (totalNum + pageSize - 1) / pageSize;
            boolean finishFlag = false;
            listArray = new ArrayList[totalPage];
            for (int i = 0; i < totalPage; i++) {
                List<DbTable> temp = new ArrayList<>();
                for (int j = 0; j < pageSize; j++) {
                    int index = i * pageSize + j;
                    if (index >= totalNum) {
                        finishFlag = true;
                        break;
                    }
                    temp.add(dbTableList.get(index));
                }
                listArray[i] = temp;
                if (finishFlag) {
                    break;
                }
            }
        }
        return listArray;
    }

    public List<DbTable> getTableNameAndComments(List<Map<String, Object>> resultList){
        List<DbTable> tableList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (int i = 0; i < resultList.size(); i++) {
                Map<String, Object> resultMap = resultList.get(i);
                DbTable dbTable = new DbTable();
                String tableName = MapUtils.getString(resultMap, "TABLE_NAME");
                String tableComments = MapUtils.getString(resultMap, "COMMENTS");
                if (StringUtils.isEmpty(tableComments)) {
                    dbTable.setTableComments(FiledDefaultValue.TABLE_COMMENTS_DEFAULT);
                } else {
                    dbTable.setTableComments(tableComments);
                }
                dbTable.setTableName(tableName);
                tableList.add(dbTable);
            }
        }
        return tableList;
    }
}
