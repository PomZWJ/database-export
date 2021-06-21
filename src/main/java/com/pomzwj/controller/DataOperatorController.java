package com.pomzwj.controller;

import com.deepoove.poi.XWPFTemplate;
import com.pomzwj.constant.DataBaseType;
import com.pomzwj.dbservice.DbService;
import com.pomzwj.dbservice.DbServiceFactory;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;
import com.pomzwj.officeframework.poitl.PoitlOperatorService;
import com.pomzwj.utils.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 类说明:
 *
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2018/10/29/0029.
 */
@Controller
@RequestMapping(value = "/makeWord")
public class DataOperatorController {
    static final Logger log = LoggerFactory.getLogger(DataOperatorController.class);
    @Autowired
    private PoitlOperatorService poitlOperatorService;
    @Autowired
    private DbServiceFactory dbServiceFactory;

    @RequestMapping(value = "/v1")
    @ResponseBody
    public void getData(DbBaseInfo info, HttpServletResponse response) {
        String desc = "生成word文档[v1]";
        XWPFTemplate xwpfTemplate = null;
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            //参数校验
            AssertUtils.isNull(info.getDbKind(),MessageCode.DATABASE_KIND_IS_NULL_ERROR);
            AssertUtils.isNull(info.getIp(),MessageCode.DATABASE_IP_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPort(),MessageCode.DATABASE_PORT_IS_NULL_ERROR);
            AssertUtils.isNull(info.getUserName(),MessageCode.DATABASE_USER_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPassword(),MessageCode.DATABASE_PASSWORD_IS_NULL_ERROR);
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

    @RequestMapping(value = "/v2")
    @ResponseBody
    public void makeWordV2(DbBaseInfo info, HttpServletResponse response) {
        String desc = "生成word文档[v2]";
        XWPFTemplate xwpfTemplate = null;
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            //参数校验
            AssertUtils.isNull(info.getDbKind(),MessageCode.DATABASE_KIND_IS_NULL_ERROR);
            AssertUtils.isNull(info.getIp(),MessageCode.DATABASE_IP_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPort(),MessageCode.DATABASE_PORT_IS_NULL_ERROR);
            AssertUtils.isNull(info.getUserName(),MessageCode.DATABASE_USER_IS_NULL_ERROR);
            AssertUtils.isNull(info.getPassword(),MessageCode.DATABASE_PASSWORD_IS_NULL_ERROR);
            DataBaseType dataBaseType = DataBaseType.matchType(info.getDbKind());
            if(dataBaseType==null){
                throw new DatabaseExportException(MessageCode.DATABASE_KIND_IS_NOT_MATCH_ERROR);
            }
            DbService dbServiceBean = dbServiceFactory.getDbServiceBean(info.getDbKind());
            //查询表信息
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
