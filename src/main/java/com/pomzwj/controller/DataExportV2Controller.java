package com.pomzwj.controller;

import com.alibaba.fastjson2.JSON;
import com.deepoove.poi.XWPFTemplate;
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
import com.pomzwj.officeframework.poi.PoiExcelOperatorService;
import com.pomzwj.officeframework.poitl.PoitlOperatorService;
import com.pomzwj.utils.AssertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
@RequestMapping("/v2")
public class DataExportV2Controller {
    static final Logger log = LoggerFactory.getLogger(DataExportV2Controller.class);
    @Autowired
    private PoitlOperatorService poitlOperatorService;
    @Autowired
    private PoiExcelOperatorService poiExcelOperatorService;
    @Autowired
    private DbServiceFactory dbServiceFactory;

    @RequestMapping("/viewDocVue")
    public String viewDocVue(DbBaseInfo info, ModelMap modelMap){
        modelMap.put("dbBaseInfo",info);
        return "v2/docView";
    }

    @RequestMapping("/viewDocHtml")
    public String docHtml(String base64Params, ModelMap modelMap){
        modelMap.put("base64Params",base64Params);
        return "v2/docHtml";
    }

    @RequestMapping(value = "/makeWord")
    @ResponseBody
    public void makeWord(String base64Params, HttpServletResponse response) {
        String desc = "生成word文档[v2]";
        XWPFTemplate xwpfTemplate = null;
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            DbBaseInfo info = JSON.parseObject(new String(Base64.getDecoder().decode(base64Params)),DbBaseInfo.class);
            //参数校验
            AssertUtils.isNull(info.getDbKind(), MessageCode.DATABASE_KIND_IS_NULL_ERROR);
            AssertUtils.isNull(info.getIp(), MessageCode.DATABASE_IP_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPort(), MessageCode.DATABASE_PORT_IS_NULL_ERROR);
            AssertUtils.isNull(info.getUserName(), MessageCode.DATABASE_USER_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPassword(), MessageCode.DATABASE_PASSWORD_IS_NULL_ERROR);
            String exportFileType = info.getExportFileType();
            if (StringUtils.isEmpty(exportFileType)) {
                exportFileType = "word";
            }
            ExportFileType exportFileTypeEnum = ExportFileType.matchType(exportFileType);
            if (exportFileTypeEnum == null) {
                throw new DatabaseExportException(MessageCode.EXPORT_FILE_TYPE_IS_NOT_MATCH_ERROR);
            }else if(exportFileTypeEnum.isEnable() == false){
                throw new DatabaseExportException(MessageCode.EXPORT_FILE_TYPE_IS_NOT_DEVELOP_ERROR);
            }
            DataBaseType dataBaseType = DataBaseType.matchType(info.getDbKind());
            if (dataBaseType == null) {
                throw new DatabaseExportException(MessageCode.DATABASE_KIND_IS_NOT_MATCH_ERROR);
            }
            DbService dbServiceBean = dbServiceFactory.getDbServiceBean(info.getDbKind());
            //查询表信息
            List<DbTable> tableDetailInfo = dbServiceBean.getTableDetailInfo(info);
            //生成word文档
            xwpfTemplate = poitlOperatorService.makeDoc(info.getDbKind(), tableDetailInfo);
            response.setContentType("application/octet-stream");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            response.setHeader("Content-Disposition", "attachment;fileName=" + info.getDbName() + sdf.format(new Date()) + ".docx");
            response.flushBuffer();
            xwpfTemplate.write(response.getOutputStream());
        } catch (Exception e) {
            try {
                response.getWriter().println(e.getMessage());
            } catch (IOException ioException) {
                log.error("desc={},输出错误信息出错e={}", desc, ioException);
            }
            log.error("desc={},获取失败, 原因:{}", desc, e.getMessage(), e);
        } finally {
            if (xwpfTemplate != null) {
                try {
                    xwpfTemplate.close();
                } catch (IOException e) {
                    log.error("desc={},关闭xwpfTemplate出错e={}", desc, e);
                }
            }
        }
    }

    @RequestMapping(value = "/makeExcel")
    @ResponseBody
    public void makeExcel(String base64Params, HttpServletResponse response) {
        String desc = "生成excel文档[v2]";
        XSSFWorkbook workbook = null;
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            DbBaseInfo info = JSON.parseObject(new String(Base64.getDecoder().decode(base64Params)),DbBaseInfo.class);
            //参数校验
            AssertUtils.isNull(info.getDbKind(), MessageCode.DATABASE_KIND_IS_NULL_ERROR);
            AssertUtils.isNull(info.getIp(), MessageCode.DATABASE_IP_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPort(), MessageCode.DATABASE_PORT_IS_NULL_ERROR);
            AssertUtils.isNull(info.getUserName(), MessageCode.DATABASE_USER_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPassword(), MessageCode.DATABASE_PASSWORD_IS_NULL_ERROR);
            String exportFileType = info.getExportFileType();
            if (StringUtils.isEmpty(exportFileType)) {
                exportFileType = "excel";
            }
            ExportFileType exportFileTypeEnum = ExportFileType.matchType(exportFileType);
            if (exportFileTypeEnum == null) {
                throw new DatabaseExportException(MessageCode.EXPORT_FILE_TYPE_IS_NOT_MATCH_ERROR);
            }else if(exportFileTypeEnum.isEnable() == false){
                throw new DatabaseExportException(MessageCode.EXPORT_FILE_TYPE_IS_NOT_DEVELOP_ERROR);
            }
            DataBaseType dataBaseType = DataBaseType.matchType(info.getDbKind());
            if (dataBaseType == null) {
                throw new DatabaseExportException(MessageCode.DATABASE_KIND_IS_NOT_MATCH_ERROR);
            }
            DbService dbServiceBean = dbServiceFactory.getDbServiceBean(info.getDbKind());
            //查询表信息
            List<DbTable> tableDetailInfo = dbServiceBean.getTableDetailInfo(info);
            //生成word文档
            workbook = poiExcelOperatorService.makeExcel(info.getDbKind(),info.getDbName(), tableDetailInfo);
            response.setContentType("application/octet-stream");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            response.setHeader("Content-Disposition", "attachment;fileName=" + info.getDbName() + sdf.format(new Date()) + ".xlsx");
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            try {
                response.getWriter().println(e.getMessage());
            } catch (IOException ioException) {
                log.error("desc={},输出错误信息出错e={}", desc, ioException);
            }
            log.error("desc={},获取失败, 原因:{}", desc, e.getMessage(), e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    log.error("desc={},关闭workbook出错e={}", desc, e);
                }
            }
        }
    }
    @RequestMapping(value = "/getTableData")
    @ResponseBody
    public ResponseParams<Map> getDocData(String base64Params) {
        String desc = "生成word文档[v2]";
        ResponseParams<Map> responseParams = new ResponseParams();
        try {
            String s = new String(Base64.getDecoder().decode(base64Params));
            DbBaseInfo info = JSON.parseObject(s,DbBaseInfo.class);
            //参数校验
            AssertUtils.isNull(info.getDbKind(), MessageCode.DATABASE_KIND_IS_NULL_ERROR);
            AssertUtils.isNull(info.getIp(), MessageCode.DATABASE_IP_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPort(), MessageCode.DATABASE_PORT_IS_NULL_ERROR);
            AssertUtils.isNull(info.getUserName(), MessageCode.DATABASE_USER_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPassword(), MessageCode.DATABASE_PASSWORD_IS_NULL_ERROR);
            DataBaseType dataBaseType = DataBaseType.matchType(info.getDbKind());
            if (dataBaseType == null) {
                throw new DatabaseExportException(MessageCode.DATABASE_KIND_IS_NOT_MATCH_ERROR);
            }
            List<String> columnNames = dataBaseType.getColumnName();
            List<String> columnNameStrLists = new ArrayList<>();
            DbService dbServiceBean = dbServiceFactory.getDbServiceBean(info.getDbKind());
            //查询表信息
            List<DbTable> tableDetailInfo = dbServiceBean.getTableDetailInfo(info);

            for(int i=0;i<columnNames.size();i++){
                String columnNameStr = null;
                String columnName = columnNames.get(i);
                Field declaredField = DbColumnInfo.class.getDeclaredField(columnName);
                DataColumnName annotation = declaredField.getAnnotation(DataColumnName.class);
                if(annotation == null){
                    columnNameStr = "";
                }
                columnNameStr = annotation.name();
                columnNameStrLists.add(columnNameStr);
            }


            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("headerList", columnNameStrLists);
            resultMap.put("fieldList", columnNames);
            resultMap.put("tableDetailInfo", tableDetailInfo);
            responseParams.setParams(resultMap);
        } catch (Exception e) {
            responseParams.setResultCode("500");
            responseParams.setParams(null);
            responseParams.setResultMsg(e.getMessage());
            log.error("desc={},获取失败, 原因:{}", desc, e.getMessage(), e);
        }
        return responseParams;
    }
}
