package com.pomzwj.controller;

import com.alibaba.fastjson.JSON;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.domain.ResponseMessage;
import com.pomzwj.officeframework.poitl.PoitlOperatorService;
import com.pomzwj.service.IDataOperatorService;
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
@Controller
public class DataOperatorController {

    @Autowired
    private IDataOperatorService dataOperatorService;
    @Autowired
    private PoitlOperatorService poitlOperatorService;

    @RequestMapping(value = "/makeWord")
    public @ResponseBody
    ResponseMessage getData(String dbKind, DbBaseInfo info) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List<DbTable> tableMessage = dataOperatorService.getTablename(dbKind,info);
            poitlOperatorService.makeDoc(tableMessage);
            responseMessage.setMessage("生成文档成功!!!");
        }catch (Exception e){
            e.printStackTrace();
            responseMessage.setMessage("诶，遇到错误了,"+e.getMessage());
        }
        System.out.println(JSON.toJSONString(responseMessage));
        return responseMessage;
    }







}
