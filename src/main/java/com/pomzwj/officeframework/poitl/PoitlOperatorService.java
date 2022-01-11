package com.pomzwj.officeframework.poitl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import com.pomzwj.anno.DataColumnName;
import com.pomzwj.constant.DataBaseType;
import com.pomzwj.constant.TemplateFileConstants;
import com.pomzwj.domain.DbColumnInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.domain.SegmentData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


/**
 * 类说明:POI-TL操作服务
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2018/10/29/0029.
 */
@Service
public class PoitlOperatorService {
	@Value("${export.template-copy-path}")
	private String templateCopyPath;

	/**
	 * 生成word，带自定义路径
	 *
	 * @param dbKind
	 * @param tableList
	 * @return
	 */
	public XWPFTemplate makeDoc(String dbKind, List<DbTable> tableList) throws Exception {
		DataBaseType dataBaseKind = DataBaseType.matchType(dbKind);
		List<String> columnNames = dataBaseKind.getColumnName();

		Class<DbColumnInfo> dbColumnInfoClass = DbColumnInfo.class;
		List segmentDataList = new ArrayList();


		//获取表头
		List<String> headerList = new ArrayList<>();
		for (int i = 0; i < columnNames.size(); i++) {
			String filed = columnNames.get(i);
			Field declaredField = dbColumnInfoClass.getDeclaredField(filed);
			DataColumnName annotation = declaredField.getAnnotation(DataColumnName.class);
			String annoName = annotation.name();
			headerList.add(annoName);
		}

		RowRenderData headerRow = Rows.of(headerList.toArray(new String[headerList.size()])).textColor("FFFFFF").bgColor("4472C4").create();

		for (int i = 0; i < tableList.size(); i++) {
			DbTable dbTable = tableList.get(i);
			List<RowRenderData> rowList = this.getRow(dbTable, columnNames, headerRow);
			SegmentData segmentData = new SegmentData();
			segmentData.setTable(Tables.create(rowList.toArray(new RowRenderData[rowList.size()])));
			segmentData.setTableName(dbTable.getTableName());
			segmentData.setTableComments("(" + dbTable.getTableComments() + ")");
			segmentDataList.add(segmentData);
		}

		Map<String, Object> tempMap = new HashMap<>();
		tempMap.put("seg", Includes.ofLocal(templateCopyPath + "/" + TemplateFileConstants.SUB_MODEL_TEMPLATE).setRenderModel(segmentDataList).create());

		File importWordFile = new File(templateCopyPath + "/" + TemplateFileConstants.IMPORT_TEMPLATE);
		/*1.根据模板生成文档*/
		XWPFTemplate template = XWPFTemplate.compile(importWordFile).render(tempMap);
		return template;
	}

	public List<RowRenderData> getRow(DbTable dbTable, List<String> columnNames, RowRenderData headerRow) throws Exception {
		List<DbColumnInfo> tabsColumn = dbTable.getTabsColumn();
		Class<DbColumnInfo> dbColumnInfoClass = DbColumnInfo.class;
		List<RowRenderData> rows = new ArrayList<>();
		rows.add(headerRow);
		if(tabsColumn == null){
			tabsColumn = new ArrayList<>();
		}
		for (int k = 0; k < tabsColumn.size(); k++) {
			DbColumnInfo dbColumnInfo = tabsColumn.get(k);
			List<String> dataBody = new ArrayList<>();
			for (int j = 0; j < columnNames.size(); j++) {
				String fieldName = columnNames.get(j);
				fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method method = dbColumnInfoClass.getMethod("get" + fieldName, new Class[0]);
				dataBody.add(method.invoke(dbColumnInfo, new Object[0]).toString());
			}
			rows.add(Rows.create(dataBody.toArray(new String[dataBody.size()])));
		}
		return rows;
	}
}
