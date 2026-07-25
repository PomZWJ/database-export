package io.github.pomzwj.dbexport.core.type;

import io.github.pomzwj.dbexport.core.dbservice.clickhouse.ClickHouseIndexInfo;
import io.github.pomzwj.dbexport.core.dbservice.clickhouse.ClickhouseColumnInfo;
import io.github.pomzwj.dbexport.core.dbservice.db2.Db2ColumnInfo;
import io.github.pomzwj.dbexport.core.dbservice.db2.Db2IndexInfo;
import io.github.pomzwj.dbexport.core.dbservice.dm.DmDbColumnInfo;
import io.github.pomzwj.dbexport.core.dbservice.dm.DmIndexInfo;
import io.github.pomzwj.dbexport.core.dbservice.mysql.MySqlColumnInfo;
import io.github.pomzwj.dbexport.core.dbservice.mysql.MySqlIndexInfo;
import io.github.pomzwj.dbexport.core.dbservice.oracle.OracleColumnInfo;
import io.github.pomzwj.dbexport.core.dbservice.oracle.OracleIndexInfo;
import io.github.pomzwj.dbexport.core.dbservice.postgresql.PostgresqlColumnInfo;
import io.github.pomzwj.dbexport.core.dbservice.postgresql.PostgresqlIndexInfo;
import io.github.pomzwj.dbexport.core.dbservice.sqlite.SqliteColumnInfo;
import io.github.pomzwj.dbexport.core.dbservice.sqlite.SqliteIndexInfo;
import io.github.pomzwj.dbexport.core.dbservice.sqlserver.SqlServerColumnInfo;
import io.github.pomzwj.dbexport.core.dbservice.sqlserver.SqlServerIndexInfo;
import io.github.pomzwj.dbexport.core.domain.DbColumnInfo;
import io.github.pomzwj.dbexport.core.domain.DbIndexInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据库类型(详情见DbColumnInfo注解)
 *
 * @author PomZWJ
 */
public enum DataBaseType {
    /**
     * MYSQL
     */
    MYSQL(MySqlColumnInfo.class, MySqlIndexInfo.class),
    /**
     * ORACLE
     */
    ORACLE(OracleColumnInfo.class, OracleIndexInfo.class),
    /**
     * SQL_SERVER
     */
    SQLSERVER(SqlServerColumnInfo.class,SqlServerIndexInfo.class),

    /**
     * postgresql
     */
    POSTGRESQL(PostgresqlColumnInfo.class, PostgresqlIndexInfo.class),

    /**
     * clickhouse
     */
    CLICKHOUSE(ClickhouseColumnInfo.class, ClickHouseIndexInfo.class),

    /**
     * sqlite
     */
    SQLITE(SqliteColumnInfo.class, SqliteIndexInfo.class),
    /**
     * DM
     */
    DM(DmDbColumnInfo.class, DmIndexInfo.class),

    /**
     * DB2
     */
    DB2(Db2ColumnInfo.class, Db2IndexInfo.class);

    private Class<? extends DbColumnInfo> columnInfoClazz;
    private Class<? extends DbIndexInfo> indexInfoClazz;

    /**
     * 构造方法
     *
     * @param columnInfoClazz 字段信息处理类
     * @param indexInfoClazz  索引信息处理类
     */
    DataBaseType(Class<? extends DbColumnInfo> columnInfoClazz, Class<? extends DbIndexInfo> indexInfoClazz) {
        this.columnInfoClazz = columnInfoClazz;
        this.indexInfoClazz = indexInfoClazz;
    }

    /**
     * 获取字段信息处理类
     *
     * @return 字段信息处理类
     */
    public Class<? extends DbColumnInfo> getColumnInfoClazz() {
        return columnInfoClazz;
    }

    /**
     * 设置字段信息处理类
     *
     * @param columnInfoClazz 字段信息处理类
     */
    public void setColumnInfoClazz(Class<? extends DbColumnInfo> columnInfoClazz) {
        this.columnInfoClazz = columnInfoClazz;
    }


    /**
     * 获取索引信息处理类
     *
     * @return 索引信息处理类
     */
    public Class<? extends DbIndexInfo> getIndexInfoClazz() {
        return indexInfoClazz;
    }

    /**
     * 设置索引信息处理类
     *
     * @param indexInfoClazz 索引信息处理类
     */
    public void setIndexInfoClazz(Class<? extends DbIndexInfo> indexInfoClazz) {
        this.indexInfoClazz = indexInfoClazz;
    }

    /**
     * 根据字符串匹配对应的数据库类型
     *
     * @param value 数据库类型字符串
     * @return 匹配到的数据库类型，未匹配返回null
     */
    public static DataBaseType matchType(String value) {
        if (StringUtils.isNotEmpty(value)) {
            for (DataBaseType dataBase : DataBaseType.values()) {
                if (dataBase.name().equals(value.toUpperCase())) {
                    return dataBase;
                }
            }
        }
        return null;
    }
}
