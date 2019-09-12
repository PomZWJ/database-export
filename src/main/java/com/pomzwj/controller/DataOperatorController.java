package com.pomzwj.controller;

import com.alibaba.fastjson.JSON;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.domain.ResponseParams;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;
import com.pomzwj.officeframework.poitl.PoitlOperatorService;
import com.pomzwj.service.IDataOperatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public class DataOperatorController {

    @Autowired
    private IDataOperatorService dataOperatorService;
    @Autowired
    private PoitlOperatorService poitlOperatorService;

    @RequestMapping(value = "/makeWord")
    public @ResponseBody ResponseParams getData(String dbKind, DbBaseInfo info) {
        String desc = "生成word文档";
        ResponseParams responseParams = new ResponseParams();
        try {
            List<DbTable> tableMessage = dataOperatorService.getTableName(dbKind,info);
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
        return responseParams;
    }







}
