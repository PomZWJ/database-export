package io.github.pomzwj.dbexport.core.utils;

import io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件读取工具类，提供classpath资源文件读取功能
 *
 * @author PomZWJ
 */
public class FileReaderUtils {
    static final Logger log = LoggerFactory.getLogger(FileReaderUtils.class);
    /** SQL 文件内容缓存，避免每次查询都重新读取 classpath 文件 */
    private static final ConcurrentHashMap<String, String> SQL_CACHE = new ConcurrentHashMap<>();

    /**
     * 读取classpath下的SQL文件内容，优先从缓存读取
     *
     * @param path classpath下的文件路径
     * @return 文件内容字符串，读取失败返回空字符串
     */
    public static String getSqlFile(String path) {
        //优先从缓存读取，避免大表导出时重复读文件
        String cached = SQL_CACHE.get(path);
        if (cached != null) {
            return cached;
        }
        InputStream inputStream = null;
        try{
            inputStream = getDefaultClassLoader().getResource(path).openStream();
            String content = IOUtils.toString(inputStream, DataBaseConfigConstant.DEFAULT_ENCODE);
            //缓存空字符串没有意义，只缓存非空内容
            if (!content.isEmpty()) {
                SQL_CACHE.putIfAbsent(path, content);
            }
            return content;
        }catch (IOException e){
            log.error("path={},read file error",path,e);
        }finally {
            IOUtils.closeQuietly(inputStream);
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
