package com.pomzwj.filegeneration.pdf;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.pomzwj.constant.SystemConstant;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.filegeneration.AbstractFileGenerationService;
import com.pomzwj.filegeneration.word.WordOperatorService;
import com.pomzwj.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Service
public class PdfOperatorService extends AbstractFileGenerationService {
    @Autowired
    private WordOperatorService wordOperatorService;
    static final Logger log = LoggerFactory.getLogger(PdfOperatorService.class);

    @Override
    protected void makeFileStream(DbBaseInfo dbBaseInfo, List<DbTable> tableList, File targetFile) throws Exception {
        String wordPath = "";
        FileOutputStream os = null;
        try {
            dbBaseInfo.setExportFileType("word");
            String docFilePath = wordOperatorService.makeFile(dbBaseInfo, tableList);
            wordPath = SystemConstant.GENERATION_FILE_TEMP_DIR + File.separator + docFilePath;
            os = new FileOutputStream(targetFile);
            doc2pdf(wordPath, os);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dbBaseInfo.setExportFileType("pdf");
            IOUtils.closeQuietly(os);
            if (StringUtils.isNotEmpty(wordPath)) {
                FileUtils.deleteQuietly(new File(wordPath));
            }
        }

    }

    /**
     * 将word转换为pdf
     *
     * @param inPath word存储路径
     * @param outOs  pdf保存文件流
     */
    public static void doc2pdf(String inPath, FileOutputStream outOs) throws Exception {
        long old = System.currentTimeMillis();
        Document doc = new Document(inPath); // Address是将要被转化的word文档
        doc.save(outOs, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,
        // EPUB, XPS, SWF 相互转换
        long now = System.currentTimeMillis();
        log.info("Word转换为pdf成功，共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
    }
}
