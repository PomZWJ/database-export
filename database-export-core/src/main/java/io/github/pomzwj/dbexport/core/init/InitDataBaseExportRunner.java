package io.github.pomzwj.dbexport.core.init;

import io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 数据库导出初始化运行器，负责初始化所需的模版文件及资源
 *
 * @author PomZWJ
 */
public class InitDataBaseExportRunner {
    static final Logger log = LoggerFactory.getLogger(InitDataBaseExportRunner.class);

    /**
     * 初始化数据库导出所需的模版文件，将资源文件拷贝到本地缓存目录
     *
     * @throws IOException 如果读取或写入资源文件失败
     */
    public synchronized static void init() throws IOException {
        String templateCopyPath = DataBaseConfigConstant.SYSTEM_FILE_DIR;
        String[] filePath = new String[]{
                DataBaseConfigConstant.IMPORT_TEMPLATE,
                DataBaseConfigConstant.SUB_MODEL_TEMPLATE,
                DataBaseConfigConstant.HTML_TEMPLATE,
                DataBaseConfigConstant.CALLIGRAPHIC_TEMPLATE
        };

        for (String s : filePath) {
            InputStream inputStream = null;
            try {
                inputStream = getDefaultClassLoader().getResource(s).openStream();
                File file = new File(templateCopyPath + File.separator + s);
                log.info("create database-export-core file temp dir is {}", file.getAbsolutePath());
                FileUtils.copyInputStreamToFile(inputStream, file);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
    }

    /**
     * 获取默认的类加载器，依次尝试从当前线程上下文、当前类及系统类加载器中获取
     *
     * @return 类加载器，获取不到时返回null
     */
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
