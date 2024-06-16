package io.github.pomzwj.dbexport.core.utils;

import io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class FileReaderUtils {
    static final Logger log = LoggerFactory.getLogger(FileReaderUtils.class);
    public static String getSqlFile(String path) {
        try{
            InputStream inputStream = getDefaultClassLoader().getResource(path).openStream();
            return IOUtils.toString(inputStream, DataBaseConfigConstant.DEFAULT_ENCODE);
        }catch (IOException e){
            log.error("path={},read file error",path,e);
        }
        return "";
    }

    static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;

        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable var3) {
        }

        if (cl == null) {
            cl = FileReaderUtils.class.getClassLoader();
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
