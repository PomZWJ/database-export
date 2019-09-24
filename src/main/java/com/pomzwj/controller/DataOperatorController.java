package com.pomzwj.controller;

import com.alibaba.fastjson.JSON;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.domain.ResponseParams;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;
import com.pomzwj.officeframework.poitl.PoitlOperatorService;
import com.pomzwj.service.IDataOperatorService;
import com.pomzwj.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public @ResponseBody ResponseParams getData(String dbKind, DbBaseInfo info) {
        String desc = "生成word文档";
        ResponseParams responseParams = new ResponseParams();
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
            poitlOperatorService.makeDoc(tableMessage);
            responseParams.setParams(true);
        } catch (Exception e) {
            log.error("desc={},获取失败, 原因:{}", desc, e.getMessage(), e);
            responseParams.setParams(null);
            if (e instanceof DatabaseExportException) {
                DatabaseExportException ce = (DatabaseExportException) e;
                responseParams.setResultCode(ce.getErrorCode());
                responseParams.setResultMsg(ce.getErrorMessage());
            } else {
                responseParams.setResultCode(MessageCode.UNKNOWN_ERROR.getCode());
                responseParams.setResultMsg(MessageCode.UNKNOWN_ERROR.getMsg() + "," + e.getMessage());
            }
        }
        log.info("desc = {},返回的数据={}",desc,JSON.toJSONString(responseParams));
        return responseParams;
    }

    @RequestMapping(value = "/v2")
    public @ResponseBody ResponseParams makeWordV2(@RequestBody Map map) {
        String desc = "生成word文档";
        DbBaseInfo info = new DbBaseInfo();
        ResponseParams responseParams = new ResponseParams();
        try {
            //参数接收
            String dbKind = StringUtils.getValue(map.get("dbKind"));
            String filePath = StringUtils.getValue(map.get("filePath"));
            info.setIp(StringUtils.getValue(map.get("ip")));
            info.setDbName(StringUtils.getValue(map.get("dbName")));
            info.setUserName(StringUtils.getValue(map.get("userName")));
            info.setPassword(StringUtils.getValue(map.get("password")));
            info.setPort(StringUtils.getValue(map.get("port")));
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
            if(StringUtils.isEmpty(filePath)){
                throw new DatabaseExportException(MessageCode.FILE_PATH_IS_NULL_ERROR.getCode(),MessageCode.FILE_PATH_IS_NULL_ERROR.getMsg());
            }
            //查询表信息
            List<DbTable> tableMessage = dataOperatorService.getTableName(dbKind,info);
            //生成word文档
            poitlOperatorService.makeDoc(tableMessage,filePath);
            responseParams.setParams(true);
        } catch (Exception e) {
            log.error("desc={},获取失败, 原因:{}", desc, e.getMessage(), e);
            responseParams.setParams(null);
            if (e instanceof DatabaseExportException) {
                DatabaseExportException ce = (DatabaseExportException) e;
                responseParams.setResultCode(ce.getErrorCode());
                responseParams.setResultMsg(ce.getErrorMessage());
            } else {
                responseParams.setResultCode(MessageCode.UNKNOWN_ERROR.getCode());
                responseParams.setResultMsg(MessageCode.UNKNOWN_ERROR.getMsg() + "," + e.getMessage());
            }
        }
        log.info("desc = {},返回的数据={}",desc,JSON.toJSONString(responseParams));
        return responseParams;
    }







}
