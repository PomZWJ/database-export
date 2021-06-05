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

import java.io.File;

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
    public void run(ApplicationArguments args){
        String[] filePath = new String[]{TemplateFileConstants.IMPORT_TEMPLATE,TemplateFileConstants.SUB_MODEL_TEMPLATE};
        for(String s:filePath){
            try{
                ClassPathResource classPathResource = new ClassPathResource("docx"+File.separator+s);
                File file = new File(templateCopyPath+File.separator+s);
                if(!file.exists()){
                    log.info("生成文件的路径是={}",file.getAbsolutePath());
                    FileUtils.copyInputStreamToFile(classPathResource.getInputStream(),file);
                }
            }catch (Exception e){
                log.error("创建初始文件失败,系统自动退出,e={}",e);
                System.exit(0);
            }
        }
        log.info("http://localhost:{}{}",serverPort,contextPath);
    }
}
