package com.pomzwj.runner;

import com.pomzwj.constant.TemplateFileConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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
@Slf4j
@Component
public class TemplateEventApplicationRunner implements ApplicationRunner {
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
                ClassPathResource classPathResource = new ClassPathResource("docx/"+s);
                File file = new File(templateCopyPath+"/"+s);
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
