package com.pomzwj.officeframework.poitl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.DocxRenderData;
import com.deepoove.poi.data.MiniTableRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.pomzwj.constant.TemplateFileConstants;
import com.pomzwj.domain.SegmentData;
import com.pomzwj.domain.TempData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;


/**
 * 类说明:POI-TL操作服务
 *
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2018/10/29/0029.
 */
@Service
public class PoitlOperatorService {
    @Value("${export.template-copy-path}")
    private String templateCopyPath;

    /**
     * 生成word，带自定义路径
     * @param rowNames
     * @param tempDataList
     * @return
     */
    public XWPFTemplate makeDoc(List<String>rowNames,List<TempData>tempDataList) {

        Map<String,Object>tempMap = new HashMap<>();
        List segmentDataList = new ArrayList();
        for(int i=0;i<tempDataList.size();i++){
            TempData tempData = tempDataList.get(i);
            RowRenderData header = RowRenderData.build(rowNames.toArray(new String[rowNames.size()]));
            SegmentData segmentData = new SegmentData();
            segmentData.setTable(new MiniTableRenderData(header,tempData.getData()));
            segmentData.setTableName(tempData.getTableName());
            segmentData.setTableComments("("+tempData.getTableComment()+")");
            segmentDataList.add(segmentData);
        }
        File subModelWordFile = new File(templateCopyPath+"/"+ TemplateFileConstants.SUB_MODEL_TEMPLATE);
        tempMap.put("seg",new DocxRenderData(subModelWordFile, segmentDataList));

        File importWordFile = new File(templateCopyPath+"/"+ TemplateFileConstants.IMPORT_TEMPLATE);
        /*1.根据模板生成文档*/
        XWPFTemplate template = XWPFTemplate.compile(importWordFile).render(tempMap);
        return template;
    }
}
