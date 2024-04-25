package io.github.pomzwj.dbexport.web.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import io.github.pomzwj.dbexport.core.DataBaseExportExecute;
import io.github.pomzwj.dbexport.core.anno.DataColumnName;
import io.github.pomzwj.dbexport.core.anno.DbIndexName;
import io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant;
import io.github.pomzwj.dbexport.core.domain.DbExportConfig;
import io.github.pomzwj.dbexport.core.exception.MessageCode;
import io.github.pomzwj.dbexport.core.type.DataBaseType;
import io.github.pomzwj.dbexport.core.type.ExportFileType;
import io.github.pomzwj.dbexport.core.utils.ClassUtils;
import io.github.pomzwj.dbexport.core.domain.DbBaseInfo;
import io.github.pomzwj.dbexport.core.domain.DbColumnInfo;
import io.github.pomzwj.dbexport.core.domain.DbTable;
import io.github.pomzwj.dbexport.core.utils.StringUtils;
import io.github.pomzwj.dbexport.web.domain.DownloadFile;
import io.github.pomzwj.dbexport.web.domain.ResponseParams;
import io.github.pomzwj.dbexport.web.params.DbBaseConfig;
import io.github.pomzwj.dbexport.web.params.DbBaseConfigResult;
import io.github.pomzwj.dbexport.web.utils.DruidPoolUtils;
import io.github.pomzwj.dbexport.web.vo.DbBaseInfoVo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant.SYSTEM_FILE_DIR;

/**
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * v2版本
 */
@Controller
@RequestMapping("/")
public class DataExportController {
    public final static String GENERATION_FILE_TEMP_DIR = SYSTEM_FILE_DIR + File.separator + "fileTemp";
    static final Logger log = LoggerFactory.getLogger(DataExportController.class);
    @Autowired
    DataBaseExportExecute dataBaseExportExecute;
    @Autowired
    DruidPoolUtils druidPoolUtils;

    @RequestMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/getConfig/{dbKind}")
    @ResponseBody
    public ResponseParams<DbBaseConfigResult> getConfig(@PathVariable("dbKind") String dbKind) {
        DataBaseType dataBaseType = DataBaseType.matchType(dbKind);
        List<Field> columFields = ClassUtils.sortColumnField(dataBaseType.getColumnInfoClazz());
        List<Field> indexfields = ClassUtils.sortIndexField(dataBaseType.getIndexInfoClazz());
        DbBaseConfigResult dbBaseConfigResult = new DbBaseConfigResult();
        List<DbBaseConfig> columnConfig = new ArrayList<>();
        List<DbBaseConfig> indexConfig = new ArrayList<>();
        List<DbBaseConfig> exportTypeConfig = new ArrayList<>();
        exportTypeConfig.add(new DbBaseConfig("VIEW","VIEW"));
        for (ExportFileType exportFileType : ExportFileType.values()) {
            exportTypeConfig.add(new DbBaseConfig(exportFileType.name(),exportFileType.name()));
        }
        for (Field item : columFields) {
            String value = item.getName();
            DataColumnName annotation = item.getAnnotation(DataColumnName.class);
            String text = annotation.name();
            columnConfig.add(new DbBaseConfig(value,text));
        }
        for (Field item : indexfields) {
            String value = item.getName();
            DbIndexName annotation = item.getAnnotation(DbIndexName.class);
            String text = annotation.name();
            indexConfig.add(new DbBaseConfig(value,text));
        }
        dbBaseConfigResult.setColumnConfig(columnConfig);
        dbBaseConfigResult.setIndexConfig(indexConfig);
        dbBaseConfigResult.setExportTypeConfig(exportTypeConfig);
        return new ResponseParams<DbBaseConfigResult>().success(MessageCode.SUCCESS,dbBaseConfigResult);
    }

    @PostMapping(value = "/makeFile")
    @ResponseBody
    public ResponseParams<Map<String, Object>> makeFile(@RequestBody DbBaseInfoVo dbBaseInfoVo) throws Exception {
        DataSource dbPool = druidPoolUtils.createDbPool(dbBaseInfoVo);
        //查询表信息
        DbExportConfig dbExportConfig = new DbExportConfig().setSearchIndex(dbBaseInfoVo.getShowIndex() == 1 ? true:false);
        dbExportConfig.setShowColumnList(Stream.of(dbBaseInfoVo.getColumnSetList().split(","))
                .collect(Collectors.toList()));
        dbExportConfig.setShowIndexList(Stream.of(dbBaseInfoVo.getIndexSetList().split(","))
                .collect(Collectors.toList()));
        String selectTableStr = dbBaseInfoVo.getSelectTableStr();
        dbExportConfig.setGenerationFileTempDir(GENERATION_FILE_TEMP_DIR);
        dbExportConfig.setExportFileTypeEnum(ExportFileType.matchType(dbBaseInfoVo.getExportFileType()));
        if(StringUtils.isNotEmpty(selectTableStr)){
            dbExportConfig.setSelectTableList(Splitter.on(",").splitToList(selectTableStr));
        }
        try {
            String executeFile = dataBaseExportExecute.executeFile(dbPool, dbExportConfig);
            String fileName = new File(executeFile).getName();
            return new ResponseParams<Map<String, Object>>().success(MessageCode.SUCCESS, ImmutableMap.of("fileName", fileName, "dbName", dbBaseInfoVo.getDbName()));
        } finally {
            druidPoolUtils.closeDbPool(dbPool);
        }
    }

    @PostMapping(value = "/getFile")
    @ResponseBody
    public void makeFile(@RequestBody DownloadFile downloadFile, HttpServletResponse response) throws Exception {
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        File file = null;
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            String fileName = downloadFile.getFileName();
            file = new File(GENERATION_FILE_TEMP_DIR + File.separator + fileName);
            response.setContentType("application/octet-stream");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            fileInputStream = FileUtils.openInputStream(file);
            outputStream = response.getOutputStream();
            IOUtils.copy(fileInputStream, outputStream);
        } finally {
            //关闭流
            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(outputStream);
            FileUtils.deleteQuietly(file);
        }
    }

    @PostMapping(value = "/getTableData")
    @ResponseBody
    public ResponseParams<Map<String, Object>> getDocData(@RequestBody DbBaseInfoVo dbBaseInfoVo) throws Exception {
        DataSource dbPool = druidPoolUtils.createDbPool(dbBaseInfoVo);
        //查询表信息
        try {
            DbExportConfig dbExportConfig = new DbExportConfig().setSearchIndex(dbBaseInfoVo.getShowIndex() == 1 ? true:false);
            dbExportConfig.setShowColumnList(Stream.of(dbBaseInfoVo.getColumnSetList().split(","))
                    .collect(Collectors.toList()));
            dbExportConfig.setShowIndexList(Stream.of(dbBaseInfoVo.getIndexSetList().split(","))
                    .collect(Collectors.toList()));
            String selectTableStr = dbBaseInfoVo.getSelectTableStr();
            if(StringUtils.isNotEmpty(selectTableStr)){
                dbExportConfig.setSelectTableList(Splitter.on(",").splitToList(selectTableStr));
            }
            List<DbTable> tableDetailInfo = dataBaseExportExecute.executeGetTableDataAll(dbPool, dbExportConfig);
            List<Map<String, String>> columnMap = new ArrayList<>();
            for (Field declaredField : ClassUtils.sortColumnField(dbExportConfig.getDbColumnInfoDynamicClazz())) {
                declaredField.setAccessible(true);
                Map<String, String> col = new HashMap<>();
                String columnName = declaredField.getName();
                DataColumnName dataColumnName = declaredField.getAnnotation(DataColumnName.class);
                col.put("prop", columnName);
                col.put("label", dataColumnName.name());
                columnMap.add(col);
            }
            List<Map<String, String>> indexMap = new ArrayList<>();
            if (dbExportConfig.isSearchIndex()) {
                for (Field declaredField : ClassUtils.sortIndexField(dbExportConfig.getDbIndexInfoDynamicClazz())) {
                    declaredField.setAccessible(true);
                    Map<String, String> col = new HashMap<>();
                    String columnName = declaredField.getName();
                    DbIndexName dataColumnName = declaredField.getAnnotation(DbIndexName.class);
                    col.put("prop", columnName);
                    col.put("label", dataColumnName.name());
                    indexMap.add(col);
                }
            }
            return new ResponseParams<Map<String, Object>>().success(MessageCode.SUCCESS, ImmutableMap.of("fieldList", columnMap, "indexFieldList", indexMap, "tableDetailInfo", tableDetailInfo));
        } finally {
            druidPoolUtils.closeDbPool(dbPool);
        }
    }

    @RequestMapping(value = "getAllTableInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResponseParams<List<DbTable>> getAllTableInfo(@RequestBody DbBaseInfoVo infoVo) {
        //查询表信息
        DataSource dbPool = druidPoolUtils.createDbPool(infoVo);
        DbExportConfig dbExportConfig = new DbExportConfig().setSearchIndex(true);
        try {
            List<DbTable> tableDetailInfo = dataBaseExportExecute.executeGetTableAndComments(dbPool, dbExportConfig);
            return new ResponseParams<List<DbTable>>().success(MessageCode.SUCCESS, tableDetailInfo);
        } finally {
            druidPoolUtils.closeDbPool(dbPool);
        }
    }

}
