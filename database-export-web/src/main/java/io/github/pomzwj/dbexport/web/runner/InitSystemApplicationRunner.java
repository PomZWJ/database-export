package io.github.pomzwj.dbexport.web.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * @author PomZWJ
 */
@Component
public class InitSystemApplicationRunner implements ApplicationRunner {
    static final Logger log = LoggerFactory.getLogger(InitSystemApplicationRunner.class);
    @Value("${server.port}")
    private Integer serverPort;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${databaseExport.startWeb}")
    private boolean startWebFlag;

    @Override
    public void run(ApplicationArguments args) {
        if(!startWebFlag){
            return;
        }
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String url = String.format("http://%s:%s%s", addr.getHostAddress(), serverPort, contextPath);
            String property = System.getProperty("os.name");
            if(property.startsWith("Windows")){
                Runtime.getRuntime().exec(String.format("rundll32 url.dll,FileProtocolHandler %s",url));
                log.info("检测到系统为windows，将自动打开主页");
            }
        } catch (Exception e) {
            log.error("获取主机IP错误,e={}", e);
        }

    }
}
