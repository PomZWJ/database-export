package io.github.pomzwj.dbexport.core.filegeneration;

import io.github.pomzwj.dbexport.core.type.ExportFileType;
import io.github.pomzwj.dbexport.core.filegeneration.html.HtmlOperatorService;
import io.github.pomzwj.dbexport.core.filegeneration.md.MarkdownOperatorService;
import io.github.pomzwj.dbexport.core.filegeneration.pdf.PdfOperatorService;
import io.github.pomzwj.dbexport.core.filegeneration.word.WordOperatorService;

public class FileGenerationFactory {
    public FileGenerationService getFileGenerationBean(ExportFileType exportFileType) {
        if (ExportFileType.MARKDOWN.equals(exportFileType)) {
            return MarkdownOperatorService.getInstance();
        }else if (ExportFileType.WORD.equals(exportFileType)) {
            return WordOperatorService.getInstance();
        }else if(ExportFileType.PDF.equals(exportFileType)){
            return PdfOperatorService.getInstance();
        }else if(ExportFileType.HTML.equals(exportFileType)){
            return HtmlOperatorService.getInstance();
        }
        return null;
    }
}
