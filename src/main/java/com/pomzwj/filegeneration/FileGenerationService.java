package com.pomzwj.filegeneration;

import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;

import java.util.List;

public interface FileGenerationService {

    /**
     * 生成文件
     * @param dbBaseInfo
     * @param tableList
     * @return 文件路径
     * @throws Exception
     */
    String makeFile(DbBaseInfo dbBaseInfo, List<DbTable> tableList)throws Exception;

}
