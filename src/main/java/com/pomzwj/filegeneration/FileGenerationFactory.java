package com.pomzwj.filegeneration;

import com.pomzwj.constant.ExportFileType;
import com.pomzwj.filegeneration.excel.ExcelOperatorService;
import com.pomzwj.filegeneration.md.MarkdownOperatorService;
import com.pomzwj.filegeneration.word.WordOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileGenerationFactory {
    @Autowired
    private MarkdownOperatorService markdownOperatorService;
    @Autowired
    private ExcelOperatorService excelOperatorService;
    @Autowired
    private WordOperatorService wordOperatorService;

    public FileGenerationService getFileGenerationBean(ExportFileType exportFileType) {
        if (ExportFileType.MARKDOWN.equals(exportFileType)) {
            return markdownOperatorService;
        }else if (ExportFileType.EXCEL.equals(exportFileType)) {
            return excelOperatorService;
        }else if (ExportFileType.WORD.equals(exportFileType)) {
            return wordOperatorService;
        }
        return null;
    }
}
