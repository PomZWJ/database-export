package io.github.pomzwj.dbexport.core.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class FileReaderUtils {

    public static String getSqlFile(String path){
        try{
            InputStream inputStream = getDefaultClassLoader().getResource(path).openStream();
            String executeSql = IOUtils.toString(inputStream, "utf-8");
            return executeSql;
        }catch (NullPointerException | IOException e){
            throw new RuntimeException(e);
        }
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
