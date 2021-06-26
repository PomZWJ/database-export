package com.pomzwj.controller;

import com.deepoove.poi.XWPFTemplate;
import com.pomzwj.constant.DataBaseType;
import com.pomzwj.constant.ExportFileType;
import com.pomzwj.dbservice.DbService;
import com.pomzwj.dbservice.DbServiceFactory;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;
import com.pomzwj.officeframework.poitl.PoitlOperatorService;
import com.pomzwj.utils.AssertUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * v1版本
 */
@Controller
@RequestMapping("/v1")
public class DataExportV1Controller {
    static final Logger log = LoggerFactory.getLogger(DataExportV1Controller.class);
    @Autowired
    private PoitlOperatorService poitlOperatorService;
    @Autowired
    private DbServiceFactory dbServiceFactory;
    @RequestMapping()
    public String getIndex1(){
        return "v1/index";
    }

    /**
     * 导出WORD文档
     * @param info
     * @param response
     */
    @RequestMapping(value = "/makeWord")
    @ResponseBody
    public void makeWord(DbBaseInfo info, HttpServletResponse response) {
        String desc = "生成word文档[v1]";
        XWPFTemplate xwpfTemplate = null;
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            //参数校验
            AssertUtils.isNull(info.getDbKind(), MessageCode.DATABASE_KIND_IS_NULL_ERROR);
            AssertUtils.isNull(info.getIp(),MessageCode.DATABASE_IP_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPort(),MessageCode.DATABASE_PORT_IS_NULL_ERROR);
            AssertUtils.isNull(info.getUserName(),MessageCode.DATABASE_USER_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPassword(),MessageCode.DATABASE_PASSWORD_IS_NULL_ERROR);
            String exportFileType = info.getExportFileType();
            if(StringUtils.isEmpty(exportFileType)){
                exportFileType = "word";
            }
            ExportFileType exportFileTypeEnum = ExportFileType.matchType(exportFileType);
            if(exportFileTypeEnum == null){
                throw new DatabaseExportException(MessageCode.EXPORT_FILE_TYPE_IS_NOT_MATCH_ERROR);
            }
            DataBaseType dataBaseType = DataBaseType.matchType(info.getDbKind());
            if(dataBaseType==null){
                throw new DatabaseExportException(MessageCode.DATABASE_KIND_IS_NOT_MATCH_ERROR);
            }
            //查询表信息
            DbService dbServiceBean = dbServiceFactory.getDbServiceBean(info.getDbKind());
            List<DbTable> tableDetailInfo = dbServiceBean.getTableDetailInfo(info);
            //生成word文档
            xwpfTemplate = poitlOperatorService.makeDoc(info.getDbKind(),tableDetailInfo);
            response.setContentType("application/octet-stream");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            response.setHeader("Content-Disposition", "attachment;fileName="+info.getDbName()+sdf.format(new Date())+".docx");
            response.flushBuffer();
            xwpfTemplate.write(response.getOutputStream());
        } catch (Exception e) {
            try {
                response.getWriter().println(e.getMessage());
            } catch (IOException ioException) {
                log.error("desc={},输出错误信息出错e={}",desc,ioException);
            }
            log.error("desc={},获取失败, 原因:{}", desc, e.getMessage(), e);
        }finally {
            if(xwpfTemplate!=null){
                try {
                    xwpfTemplate.close();
                } catch (IOException e) {
                    log.error("desc={},关闭xwpfTemplate出错e={}",desc,e);
                }
            }
        }
    }
}
