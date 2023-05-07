package com.pomzwj.filegeneration;

import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;

import java.util.List;

public interface FileGenerationService<T> {

    public T makeFile(DbBaseInfo dbBaseInfo, List<DbTable> tableList)throws Exception ;
}
