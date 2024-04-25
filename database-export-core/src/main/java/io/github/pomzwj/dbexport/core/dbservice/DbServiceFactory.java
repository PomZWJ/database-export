package io.github.pomzwj.dbexport.core.dbservice;

import io.github.pomzwj.dbexport.core.domain.DbBaseInfo;
import io.github.pomzwj.dbexport.core.type.DataBaseType;
import io.github.pomzwj.dbexport.core.dbservice.clickhouse.ClickhouseDbService;
import io.github.pomzwj.dbexport.core.dbservice.db2.Db2DbService;
import io.github.pomzwj.dbexport.core.dbservice.dm.DmDbService;
import io.github.pomzwj.dbexport.core.dbservice.mysql.MySqlDbService;
import io.github.pomzwj.dbexport.core.dbservice.oracle.OracleDbService;
import io.github.pomzwj.dbexport.core.dbservice.postgresql.PostgresqlDbService;
import io.github.pomzwj.dbexport.core.dbservice.sqlite.SqliteDbService;
import io.github.pomzwj.dbexport.core.dbservice.sqlserver.SqlServerDbService;
import io.github.pomzwj.dbexport.core.exception.DatabaseExportException;
import io.github.pomzwj.dbexport.core.exception.MessageCode;
import io.github.pomzwj.dbexport.core.utils.JdbcUrlParseUtils;

import javax.sql.DataSource;

/**
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
public class DbServiceFactory {
    public DbService getDbServiceBean(DataSource dataSource){
        if (dataSource == null) {
            throw new DatabaseExportException(MessageCode.DATABASE_SOURCE_IS_NULL_ERROR);
        }
        DbBaseInfo dbBaseInfo = JdbcUrlParseUtils.parseConnectionUrlToBaseInfo(dataSource);
        return this.getDbServiceBean(dbBaseInfo.getDbKindEnum());
    }
    public DbService getDbServiceBean(DataBaseType dataBaseType) {
        if (dataBaseType == null) {
            throw new DatabaseExportException(MessageCode.DATABASE_KIND_IS_NULL_ERROR);
        }else {
            if (DataBaseType.MYSQL.equals(dataBaseType)) {
                return MySqlDbService.getInstance();
            } else if (DataBaseType.ORACLE.equals(dataBaseType)) {
                return OracleDbService.getInstance();
            } else if (DataBaseType.SQLSERVER.equals(dataBaseType)) {
                return SqlServerDbService.getInstance();
            } else if (DataBaseType.POSTGRESQL.equals(dataBaseType)) {
                return PostgresqlDbService.getInstance();
            } else if(DataBaseType.CLICKHOUSE.equals(dataBaseType)){
                return ClickhouseDbService.getInstance();
            } else if(DataBaseType.SQLITE.equals(dataBaseType)){
                return SqliteDbService.getInstance();
            }else if(DataBaseType.DM.equals(dataBaseType)){
                return DmDbService.getInstance();
            }else if(DataBaseType.DB2.equals(dataBaseType)){
                return Db2DbService.getInstance();
            }else{
                throw new DatabaseExportException(MessageCode.DATABASE_KIND_IS_NULL_ERROR);
            }
        }
    }
}
