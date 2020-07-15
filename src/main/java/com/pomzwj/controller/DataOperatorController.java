package com.pomzwj.controller;

import com.deepoove.poi.XWPFTemplate;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;
import com.pomzwj.officeframework.poitl.PoitlOperatorService;
import com.pomzwj.service.IDataOperatorService;
import com.pomzwj.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Controller
@RequestMapping(value = "/makeWord")
public class DataOperatorController {

    @Autowired
    private IDataOperatorService dataOperatorService;
    @Autowired
    private PoitlOperatorService poitlOperatorService;

    @RequestMapping(value = "/v1")
    @ResponseBody
    public void getData(String dbKind, DbBaseInfo info, HttpServletResponse response) {
        String desc = "生成word文档[v1]";
        XWPFTemplate xwpfTemplate = null;
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            //参数校验
            if(StringUtils.isEmpty(info.getIp())){
                throw new DatabaseExportException(MessageCode.DATABASE_IP_IS_NULL_ERROR.getCode(),MessageCode.DATABASE_IP_IS_NULL_ERROR.getMsg());
            }
            if(StringUtils.isEmpty(info.getPort())){
                throw new DatabaseExportException(MessageCode.DATABASE_PORT_IS_NULL_ERROR.getCode(),MessageCode.DATABASE_PORT_IS_NULL_ERROR.getMsg());
            }
            if(StringUtils.isEmpty(info.getUserName())){
                throw new DatabaseExportException(MessageCode.DATABASE_USER_IS_NULL_ERROR.getCode(),MessageCode.DATABASE_USER_IS_NULL_ERROR.getMsg());
            }
            if(StringUtils.isEmpty(info.getPassword())){
                throw new DatabaseExportException(MessageCode.DATABASE_PASSWORD_IS_NULL_ERROR.getCode(),MessageCode.DATABASE_PASSWORD_IS_NULL_ERROR.getMsg());
            }
            //查询表信息
            List<DbTable> tableMessage = dataOperatorService.getTableName(dbKind,info);
            //生成word文档
            xwpfTemplate = poitlOperatorService.makeDoc(tableMessage);
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
    public void makeWordV2(String dbKind, DbBaseInfo info, HttpServletResponse response) {
        String desc = "生成word文档[v2]";
        XWPFTemplate xwpfTemplate = null;
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            //参数校验
            if(StringUtils.isEmpty(info.getIp())){
                throw new DatabaseExportException(MessageCode.DATABASE_IP_IS_NULL_ERROR.getCode(),MessageCode.DATABASE_IP_IS_NULL_ERROR.getMsg());
            }
            if(StringUtils.isEmpty(info.getPort())){
                throw new DatabaseExportException(MessageCode.DATABASE_PORT_IS_NULL_ERROR.getCode(),MessageCode.DATABASE_PORT_IS_NULL_ERROR.getMsg());
            }
            if(StringUtils.isEmpty(info.getUserName())){
                throw new DatabaseExportException(MessageCode.DATABASE_USER_IS_NULL_ERROR.getCode(),MessageCode.DATABASE_USER_IS_NULL_ERROR.getMsg());
            }
            if(StringUtils.isEmpty(info.getPassword())){
                throw new DatabaseExportException(MessageCode.DATABASE_PASSWORD_IS_NULL_ERROR.getCode(),MessageCode.DATABASE_PASSWORD_IS_NULL_ERROR.getMsg());
            }
            if(StringUtils.isEmpty(info.getDbName())){
                throw new DatabaseExportException(MessageCode.DATABASE_NAME_IS_NULL_ERROR.getCode(),MessageCode.DATABASE_NAME_IS_NULL_ERROR.getMsg());
            }
            //查询表信息
            List<DbTable> tableMessage = dataOperatorService.getTableName(dbKind,info);
            //生成word文档
            xwpfTemplate = poitlOperatorService.makeDoc(tableMessage);
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
