package com.pomzwj.controller;

import com.alibaba.fastjson2.JSON;
import com.pomzwj.anno.DataColumnName;
import com.pomzwj.constant.DataBaseType;
import com.pomzwj.constant.SystemConstant;
import com.pomzwj.dbservice.DbServiceFactory;
import com.pomzwj.domain.*;
import com.pomzwj.filegeneration.FileGenerationFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * v2版本
 */
@Controller
@RequestMapping("/")
public class DataExportController {
    static final Logger log = LoggerFactory.getLogger(DataExportController.class);
    @Autowired
    private DbServiceFactory dbServiceFactory;
    @Autowired
    private FileGenerationFactory fileGenerationFactory;

    @RequestMapping("/")
    public String getIndex() {
        return "index";
    }

    @RequestMapping(value = "/makeFile")
    @ResponseBody
    public ResponseParams<Map> makeFile(String base64Params) {
        String desc = "生成文档";
        ResponseParams<Map> responseParams = new ResponseParams();
        try {
            DbBaseInfo info = JSON.parseObject(new String(Base64.getDecoder().decode(base64Params)), DbBaseInfo.class);
            info.fieldCheck();
            //查询表信息
            List<DbTable> tableDetailInfo = dbServiceFactory.getDbServiceBean(info.getDbKindEnum()).getTableDetailInfo(info);
            //生成word文档
            String fileName = fileGenerationFactory.getFileGenerationBean(info.getExportFileTypeEnum()).makeFile(info, tableDetailInfo);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("fileName", fileName);
            resultMap.put("dbName", info.getDbName());
            responseParams.setParams(resultMap);
        } catch (Exception e) {
            responseParams.setResultCode("500");
            responseParams.setParams(null);
            responseParams.setResultMsg("生成数据库文档失败,"+e.getMessage());
            log.error("desc={},获取失败, 原因:{}", desc, e);
        }
        return responseParams;
    }

    @PostMapping(value = "/getFile")
    @ResponseBody
    public void makeFile(@RequestBody DownloadFile downloadFile, HttpServletResponse response) {
        String desc = "获取文档";
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        File file = null;
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            String fileName = downloadFile.getFileName();
            file = new File(SystemConstant.GENERATION_FILE_TEMP_DIR+File.separator+fileName);
            response.setContentType("application/octet-stream");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            fileInputStream = FileUtils.openInputStream(file);
            outputStream = response.getOutputStream();
            IOUtils.copy(fileInputStream, outputStream);
        } catch (Exception e) {
            log.error("desc={},获取失败, 原因:{}", desc, e.getMessage(), e);
        } finally {
            //关闭流
            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(outputStream);
            FileUtils.deleteQuietly(file);
        }
    }
    @RequestMapping(value = "/getTableData")
    @ResponseBody
    public ResponseParams<Map> getDocData(String base64Params) {

        String desc = "预览数据获取";
        ResponseParams<Map> responseParams = new ResponseParams();
        try {
            String s = new String(Base64.getDecoder().decode(base64Params));
            DbBaseInfo info = JSON.parseObject(s, DbBaseInfo.class);
            info.fieldCheck();
            DataBaseType dataBaseType = info.getDbKindEnum();
            List<String> columnNames = dataBaseType.getColumnName();
            //查询表信息
            List<DbTable> tableDetailInfo = dbServiceFactory.getDbServiceBean(info.getDbKindEnum()).getTableDetailInfo(info);
            List<Map<String,String>>columnMap = new ArrayList<>();
            for (int i = 0; i < columnNames.size(); i++) {
                Map<String,String>col = new HashMap<>();

                String columnNameStr = null;
                String columnName = columnNames.get(i);
                Field declaredField = DbColumnInfo.class.getDeclaredField(columnName);
                DataColumnName annotation = declaredField.getAnnotation(DataColumnName.class);
                if (annotation == null) {
                    columnNameStr = "";
                }
                columnNameStr = annotation.name();
                col.put("prop",columnNames.get(i));
                col.put("label",columnNameStr);
                columnMap.add(col);
            }


            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("fieldList", columnMap);
            resultMap.put("tableDetailInfo", tableDetailInfo);
            responseParams.setParams(resultMap);
        } catch (Exception e) {
            responseParams.setResultCode("500");
            responseParams.setParams(null);
            responseParams.setResultMsg("数据库数据获取失败,"+e.getMessage());
            log.error("desc={},获取失败, 原因:{}", desc, e.getMessage(), e);
        }
        return responseParams;
    }
}
