package com.pomzwj.controller;

import org.springframework.stereotype.Controller;
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
    public String getIndex(){
        return "index";
    }
}
