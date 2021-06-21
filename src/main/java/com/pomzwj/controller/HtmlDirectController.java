package com.pomzwj.controller;

import com.pomzwj.domain.DbBaseInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类说明:页面跳转控制类
 *
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2019/09/12/0029.
 */
@Controller
public class HtmlDirectController {

    @RequestMapping("/")
    public String getIndex2(){
        return "index2";
    }

    @RequestMapping("/indexV1")
    public String getIndex1(){
        return "index";
    }

    @RequestMapping("/docHtml")
    public String docHtml(DbBaseInfo info, ModelMap modelMap){
        modelMap.put("ip",info.getIp());
        modelMap.put("port",info.getPort());
        modelMap.put("dbName",info.getDbName());
        modelMap.put("userName",info.getUserName());
        modelMap.put("password",info.getPassword());
        modelMap.put("dbKind",info.getDbKind());
        return "docHtml";
    }
}
