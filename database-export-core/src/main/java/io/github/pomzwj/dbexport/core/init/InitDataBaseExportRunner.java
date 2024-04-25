package io.github.pomzwj.dbexport.core.init;

import io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class InitDataBaseExportRunner {
    static final Logger log = LoggerFactory.getLogger(InitDataBaseExportRunner.class);

    public synchronized static void init() throws IOException {
        String templateCopyPath = DataBaseConfigConstant.SYSTEM_FILE_DIR;
        String[] filePath = new String[]{
                DataBaseConfigConstant.IMPORT_TEMPLATE,
                DataBaseConfigConstant.SUB_MODEL_TEMPLATE,
                DataBaseConfigConstant.HTML_TEMPLATE,
                DataBaseConfigConstant.CALLIGRAPHIC_TEMPLATE
        };

        for (String s : filePath) {
            InputStream inputStream = getDefaultClassLoader().getResource(s).openStream();
            File file = new File(templateCopyPath + File.separator + s);
            log.info("create database-export-core file temp dir is {}", file.getAbsolutePath());
            FileUtils.copyInputStreamToFile(inputStream, file);
        }
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;

        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable var3) {
        }

        if (cl == null) {
            cl = InitDataBaseExportRunner.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable var2) {
                }
            }
        }

        return cl;
    }
}
