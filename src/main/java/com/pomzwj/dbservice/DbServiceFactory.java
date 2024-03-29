package com.pomzwj.dbservice;

import com.pomzwj.constant.DataBaseType;
import com.pomzwj.dbservice.clickhouse.ClickhouseDbService;
import com.pomzwj.dbservice.db2.Db2DbService;
import com.pomzwj.dbservice.dm.DmDbService;
import com.pomzwj.dbservice.mysql.MySqlDbService;
import com.pomzwj.dbservice.oracle.OracleDbService;
import com.pomzwj.dbservice.postgresql.PostgresqlDbService;
import com.pomzwj.dbservice.sqlite.SqliteDbService;
import com.pomzwj.dbservice.sqlserver.SqlServerDbService;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;
import com.pomzwj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
@Component
public class DbServiceFactory {
    @Autowired
    private MySqlDbService mySqlDbService;
    @Autowired
    private OracleDbService oracleDbService;
    @Autowired
    private SqlServerDbService sqlServerDbService;
    @Autowired
    private PostgresqlDbService postgresqlDbService;
    @Autowired
    private ClickhouseDbService clickhouseDbService;
    @Autowired
    private SqliteDbService sqliteDbService;
    @Autowired
    private DmDbService dmDbService;
    @Autowired
    private Db2DbService db2DbService;

    public DbService getDbServiceBean(DataBaseType dataBaseType) {
        if (dataBaseType == null) {
            throw new DatabaseExportException(MessageCode.DATABASE_KIND_IS_NULL_ERROR);
        }else {
            if (DataBaseType.MYSQL.equals(dataBaseType)) {
                return mySqlDbService;
            } else if (DataBaseType.ORACLE.equals(dataBaseType)) {
                return oracleDbService;
            } else if (DataBaseType.SQLSERVER.equals(dataBaseType)) {
                return sqlServerDbService;
            } else if (DataBaseType.POSTGRESQL.equals(dataBaseType)) {
                return postgresqlDbService;
            } else if(DataBaseType.CLICKHOUSE.equals(dataBaseType)){
                return clickhouseDbService;
            } else if(DataBaseType.SQLITE.equals(dataBaseType)){
                return sqliteDbService;
            }else if(DataBaseType.DM.equals(dataBaseType)){
                return dmDbService;
            }else if(DataBaseType.DB2.equals(dataBaseType)){
                return db2DbService;
            }
        }
        return null;
    }
}
