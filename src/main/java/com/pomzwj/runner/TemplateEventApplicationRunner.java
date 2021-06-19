package com.pomzwj.runner;

import com.pomzwj.constant.TemplateFileConstants;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.net.InetAddress;
import java.net.URI;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2020-07-15
 */
@Component
public class TemplateEventApplicationRunner implements ApplicationRunner {
    static final Logger log = LoggerFactory.getLogger(TemplateEventApplicationRunner.class);
    @Value("${export.template-copy-path}")
    private String templateCopyPath;
    @Value("${server.port}")
    private Integer serverPort;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public void run(ApplicationArguments args) {
        String[] filePath = new String[]{TemplateFileConstants.IMPORT_TEMPLATE, TemplateFileConstants.SUB_MODEL_TEMPLATE};
        for (String s : filePath) {
            try {
                ClassPathResource classPathResource = new ClassPathResource("docx" + File.separator + s);
                File file = new File(templateCopyPath + File.separator + s);

                log.info("生成文件的路径是={}", file.getAbsolutePath());
                FileUtils.copyInputStreamToFile(classPathResource.getInputStream(), file);

            } catch (Exception e) {
                log.error("创建初始文件失败,系统自动退出,e={}", e);
                System.exit(0);
            }
        }
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String url = String.format("http://%s:%s%s", addr.getHostAddress(), serverPort, contextPath);
            String property = System.getProperty("os.name");
            if(property.startsWith("Windows")){
                Runtime.getRuntime().exec(String.format("rundll32 url.dll,FileProtocolHandler %s",url));
                log.info("检测到系统为windows，将自动打开主页");
            }else{
                log.info("检测到系统不是windows，请手动打开主页,{}",url);
            }
        } catch (Exception e) {
            log.error("获取主机IP错误,e={}", e);
        }

    }
}
