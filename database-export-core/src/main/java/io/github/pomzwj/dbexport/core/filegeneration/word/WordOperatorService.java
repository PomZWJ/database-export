package io.github.pomzwj.dbexport.core.filegeneration.word;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.Includes;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.data.Tables;
import io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant;
import io.github.pomzwj.dbexport.core.filegeneration.AbstractFileGenerationService;
import io.github.pomzwj.dbexport.core.domain.*;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 类说明:POI-TL操作服务
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2018/10/29/0029.
 */
public class WordOperatorService extends AbstractFileGenerationService {

	private WordOperatorService(){

	}

	private static volatile WordOperatorService siglegle;

	public static WordOperatorService getInstance() {
		if(siglegle == null)
			synchronized(WordOperatorService.class) {
				if (siglegle == null)
					siglegle = new WordOperatorService();
			}
		return siglegle;
	}

	@Override
	protected void makeFileStream(List<DbTable> tableList, File targetFile)throws Exception {
		String templateCopyPath = DataBaseConfigConstant.SYSTEM_FILE_DIR;
		Class<? extends DbColumnInfo> columnInfoClazz = dbExportConfigThreadLocal.get().getDbColumnInfoDynamicClazz();
		Class<? extends DbIndexInfo> indexInfoClazz = dbExportConfigThreadLocal.get().getDbIndexInfoDynamicClazz();
		List segmentDataList = new ArrayList();

		//获取表头
		List<String> headerList = this.getColumnHeaderName(columnInfoClazz);
		List<String> tableIndexHeaderList = this.getIndexHeaderName(indexInfoClazz);
		RowRenderData headerRow = Rows.of(headerList.toArray(new String[headerList.size()])).textColor("FFFFFF").bgColor("4472C4").create();
		RowRenderData headerIndexRow = Rows.of(tableIndexHeaderList.toArray(new String[tableIndexHeaderList.size()])).textColor("FFFFFF").bgColor("4472C4").create();

		for (int i = 0; i < tableList.size(); i++) {
			DbTable dbTable = tableList.get(i);
			List<RowRenderData> rowList = this.getRow(dbTable, columnInfoClazz, headerRow);
			List<RowRenderData> rowIndexList = this.getRowIndex(dbTable, indexInfoClazz, headerIndexRow);
			SegmentData segmentData = new SegmentData();
			if(CollectionUtils.isNotEmpty(dbTable.getTabsIndex())){
				segmentData.setIndexTitle("索引信息");
				segmentData.setIndexTable(Tables.create(rowIndexList.toArray(new RowRenderData[rowIndexList.size()])));
			}
			segmentData.setTable(Tables.create(rowList.toArray(new RowRenderData[rowList.size()])));
			segmentData.setTableName((i+1)+"."+dbTable.getTableName());
			segmentData.setTableComments("(" + dbTable.getTableComments() + ")");
			segmentDataList.add(segmentData);
		}

		Map<String, Object> tempMap = new HashMap<>();
		tempMap.put("seg", Includes.ofLocal(templateCopyPath + "/" + DataBaseConfigConstant.SUB_MODEL_TEMPLATE).setRenderModel(segmentDataList).create());

		File importWordFile = new File(templateCopyPath + "/" + DataBaseConfigConstant.IMPORT_TEMPLATE);
		/*1.根据模板生成文档*/
		XWPFTemplate template = XWPFTemplate.compile(importWordFile).render(tempMap);
		template.writeToFile(targetFile.getAbsolutePath());
	}

	public List<RowRenderData> getRow(DbTable dbTable, Class<? extends DbColumnInfo> columnInfoClazz, RowRenderData headerRow) throws Exception {
		List<DbColumnInfo> tabsColumn = dbTable.getTabsColumn();
		List<RowRenderData> rows = new ArrayList<>();
		rows.add(headerRow);
		if(tabsColumn == null){
			tabsColumn = new ArrayList<>();
		}
		for (int k = 0; k < tabsColumn.size(); k++) {
			DbColumnInfo dbColumnInfo = tabsColumn.get(k);
			List<String> dataBody = this.getTableData(columnInfoClazz,dbColumnInfo);
			rows.add(Rows.create(dataBody.toArray(new String[dataBody.size()])));
		}
		return rows;
	}

	public List<RowRenderData> getRowIndex(DbTable dbTable, Class<? extends DbIndexInfo> indexInfoClazz, RowRenderData headerRow) throws Exception {
		List<DbIndexInfo> tabsIndexInfos = dbTable.getTabsIndex();
		List<RowRenderData> rows = new ArrayList<>();
		rows.add(headerRow);
		if(tabsIndexInfos == null){
			tabsIndexInfos = new ArrayList<>();
		}
		for (int k = 0; k < tabsIndexInfos.size(); k++) {
			DbIndexInfo dbIndexInfo = tabsIndexInfos.get(k);
			List<String> dataBody = this.getTableIndexData(indexInfoClazz,dbIndexInfo);
			rows.add(Rows.create(dataBody.toArray(new String[dataBody.size()])));
		}
		return rows;
	}
}
