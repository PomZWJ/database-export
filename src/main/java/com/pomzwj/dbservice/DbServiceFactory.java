package com.pomzwj.dbservice;

import com.pomzwj.constant.DataBaseType;
import com.pomzwj.dbservice.mysql.MySqlDbService;
import com.pomzwj.dbservice.oracle.OracleDbService;
import com.pomzwj.dbservice.sqlserver.SqlServerDbService;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;
import com.pomzwj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbServiceFactory {
    @Autowired
    private MySqlDbService mySqlDbService;
    @Autowired
    private OracleDbService oracleDbService;
    @Autowired
    private SqlServerDbService sqlServerDbService;

    public DbService getDbServiceBean(String dbKind) {
        if (StringUtils.isEmpty(dbKind)) {
            throw new DatabaseExportException(MessageCode.DATABASE_KIND_IS_NULL_ERROR);
        } else if (DataBaseType.matchType(dbKind) == null) {
            throw new DatabaseExportException(MessageCode.DATABASE_KIND_IS_NOT_MATCH_ERROR);
        } else {
            if (DataBaseType.MYSQL.name().equals(dbKind.toUpperCase())) {
                return mySqlDbService;
            } else if (DataBaseType.ORACLE.name().equals(dbKind.toUpperCase())) {
                return oracleDbService;
            } else if (DataBaseType.SQLSERVER.name().equals(dbKind.toUpperCase())) {
                return sqlServerDbService;
            }
        }
        return null;
    }
}
