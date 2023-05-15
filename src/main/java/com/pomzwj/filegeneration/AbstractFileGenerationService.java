package com.pomzwj.filegeneration;

import com.pomzwj.constant.SystemConstant;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

public abstract class AbstractFileGenerationService implements FileGenerationService{
    static final Logger log = LoggerFactory.getLogger(AbstractFileGenerationService.class);
    @Override
    public String makeFile(DbBaseInfo dbBaseInfo, List<DbTable> tableList) {
        try{
            String generationFileTempDir = SystemConstant.GENERATION_FILE_TEMP_DIR;
            String fileName = UUID.randomUUID().toString().replace("-","")+dbBaseInfo.getExportFileTypeEnum().getFileSuffixName();
            File file = new File(generationFileTempDir + File.separator+fileName);
            if(file.exists() && file.isFile()){
                FileUtils.deleteQuietly(file);
            }else{
                file.createNewFile();
            }
            this.makeFileStream(dbBaseInfo,tableList, file);
            return file.getAbsolutePath();
        }catch (Exception e){
            log.error("makeFile error",e);
        }
        return null;
    }
    protected abstract void makeFileStream(DbBaseInfo dbBaseInfo, List<DbTable> tableList,File targetFile)throws Exception;
}
