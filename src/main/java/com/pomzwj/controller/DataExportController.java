package com.pomzwj.controller;

import com.alibaba.fastjson2.JSON;
import com.deepoove.poi.XWPFTemplate;
import com.google.common.util.concurrent.RateLimiter;
import com.pomzwj.anno.DataColumnName;
import com.pomzwj.constant.DataBaseType;
import com.pomzwj.constant.ExportFileType;
import com.pomzwj.dbservice.DbService;
import com.pomzwj.dbservice.DbServiceFactory;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbColumnInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.domain.ResponseParams;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;
import com.pomzwj.filegeneration.FileGenerationFactory;
import com.pomzwj.utils.AssertUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    private RateLimiter rateLimiter = RateLimiter.create(20);
    static final Logger log = LoggerFactory.getLogger(DataExportController.class);
    @Autowired
    private DbServiceFactory dbServiceFactory;
    @Autowired
    private FileGenerationFactory fileGenerationFactory;

    @RequestMapping("/")
    public String getIndex2() {
        return "index";
    }

    @RequestMapping(value = "/makeFile")
    @ResponseBody
    public void makeFile(String base64Params, HttpServletResponse response) {
        String desc = "生成文档";
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        File file = null;
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            if (!rateLimiter.tryAcquire()) {
                throw new RuntimeException("目前请求的并发过多，请重试");
            }
            DbBaseInfo info = JSON.parseObject(new String(Base64.getDecoder().decode(base64Params)), DbBaseInfo.class);
            //参数校验
            AssertUtils.isNull(info.getDbKind(), MessageCode.DATABASE_KIND_IS_NULL_ERROR);
            AssertUtils.isNull(info.getIp(), MessageCode.DATABASE_IP_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPort(), MessageCode.DATABASE_PORT_IS_NULL_ERROR);
            AssertUtils.isNull(info.getUserName(), MessageCode.DATABASE_USER_IS_NULL_ERROR);
            ExportFileType exportFileTypeEnum = ExportFileType.matchType(info.getExportFileType());
            DataBaseType dataBaseType = DataBaseType.matchType(info.getDbKind());
            info.setExportFileTypeEnum(exportFileTypeEnum);
            info.setDbKindEnum(dataBaseType);
            DbService dbServiceBean = dbServiceFactory.getDbServiceBean(info.getDbKind());
            //查询表信息
            List<DbTable> tableDetailInfo = dbServiceBean.getTableDetailInfo(info);
            //生成word文档
            String filePath = fileGenerationFactory.getFileGenerationBean(exportFileTypeEnum).makeFile(info, tableDetailInfo);
            file = new File(filePath);
            response.setContentType("application/octet-stream");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            response.setHeader("Content-Disposition", "attachment;fileName=" + info.getDbName() + sdf.format(new Date()) + info.getExportFileTypeEnum().getFileSuffixName());
            fileInputStream = FileUtils.openInputStream(file);
            outputStream = response.getOutputStream();
            IOUtils.copy(fileInputStream, outputStream);
        } catch (Exception e) {
            try {
                response.getWriter().println(e.getMessage());
            } catch (IOException ioException) {
                log.error("desc={},输出错误信息出错e={}", desc, ioException);
            }
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

        String desc = "预览";
        ResponseParams<Map> responseParams = new ResponseParams();
        try {
            if (!rateLimiter.tryAcquire()) {
                throw new RuntimeException("目前请求的并发过多，请重试");
            }
            String s = new String(Base64.getDecoder().decode(base64Params));
            DbBaseInfo info = JSON.parseObject(s, DbBaseInfo.class);
            //参数校验
            AssertUtils.isNull(info.getDbKind(), MessageCode.DATABASE_KIND_IS_NULL_ERROR);
            AssertUtils.isNull(info.getIp(), MessageCode.DATABASE_IP_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPort(), MessageCode.DATABASE_PORT_IS_NULL_ERROR);
            AssertUtils.isNull(info.getUserName(), MessageCode.DATABASE_USER_IS_NULL_ERROR);
            //AssertUtils.isNull(info.getPassword(), MessageCode.DATABASE_PASSWORD_IS_NULL_ERROR);
            DataBaseType dataBaseType = DataBaseType.matchType(info.getDbKind());
            if (dataBaseType == null) {
                throw new DatabaseExportException(MessageCode.DATABASE_KIND_IS_NOT_MATCH_ERROR);
            }
            List<String> columnNames = dataBaseType.getColumnName();
            DbService dbServiceBean = dbServiceFactory.getDbServiceBean(info.getDbKind());
            //查询表信息
            List<DbTable> tableDetailInfo = dbServiceBean.getTableDetailInfo(info);
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
