package io.github.pomzwj.dbexport.core.filegeneration;

import io.github.pomzwj.dbexport.core.type.ExportFileType;
import io.github.pomzwj.dbexport.core.filegeneration.excel.ExcelOperatorService;
import io.github.pomzwj.dbexport.core.filegeneration.html.HtmlOperatorService;
import io.github.pomzwj.dbexport.core.filegeneration.md.MarkdownOperatorService;
import io.github.pomzwj.dbexport.core.filegeneration.pdf.PdfOperatorService;
import io.github.pomzwj.dbexport.core.filegeneration.word.WordOperatorService;

/**
 * 文件生成服务工厂，根据导出文件类型返回对应的文件生成服务实例。
 *
 * @author PomZWJ
 */
public class FileGenerationFactory {
    /**
     * 根据导出文件类型获取对应的文件生成服务实例。
     *
     * @param exportFileType 导出文件类型
     * @return 文件生成服务实例，类型不支持时返回 {@code null}
     */
    public FileGenerationService getFileGenerationBean(ExportFileType exportFileType) {
        if (ExportFileType.MARKDOWN.equals(exportFileType)) {
            return MarkdownOperatorService.getInstance();
        }else if (ExportFileType.WORD.equals(exportFileType)) {
            return WordOperatorService.getInstance();
        }else if(ExportFileType.PDF.equals(exportFileType)){
            return PdfOperatorService.getInstance();
        }else if(ExportFileType.HTML.equals(exportFileType)){
            return HtmlOperatorService.getInstance();
        }else if(ExportFileType.EXCEL.equals(exportFileType)){
            return ExcelOperatorService.getInstance();
        }
        return null;
    }
}
