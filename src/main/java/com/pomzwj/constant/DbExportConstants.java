package com.pomzwj.constant;

/**
 * 类说明:常用字段
 *
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2018/10/29/0029.
 */

public class DbExportConstants {

    /**
     * 得到连接地址
     * @param dbKind
     * @param ip
     * @param port
     * @param dbName
     * @return
     */
    public static String getJdbcUrl(String dbKind, String ip, String port, String dbName) {
        String url = null;
        //MySQL数据库
        if (dbKind.toUpperCase().equals("MYSQL")) {
            url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
        }
        //oracle数据库
        else if (dbKind.toUpperCase().equals("ORACLE")) {
            url = "jdbc:oracle:thin:@//" + ip + ":" + port + "/" + dbName;
        }
        //SQL SERVER数据库
        else if (dbKind.toUpperCase().equals("SQLSERVER")) {
            url = "jdbc:sqlserver://" + ip + ":" + port + ";databaseName=" + dbName;
        }
        return url;
    }

    /**
     * 得到驱动名称
     * @param dbKind
     * @return
     */
    public static String getDriverClassName(String dbKind) {
        String driverClassName = null;
        if (dbKind.toUpperCase().equals("MYSQL")) {
            driverClassName = "com.mysql.jdbc.Driver";
        } else if (dbKind.toUpperCase().equals("ORACLE")) {
            driverClassName = "oracle.jdbc.driver.OracleDriver";
        } else if (dbKind.toUpperCase().equals("SQLSERVER")) {
            driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        }
        return driverClassName;
    }

    /**
     * 获取得到所有表和表注释的SQL语句
     * @param dbKind
     * @param dbName--mysql需要数据库名称
     * @return
     */
    public static String getTableNameSQL(String dbKind ,String dbName){
        String sql = null;
        if (dbKind.toUpperCase().equals("MYSQL")) {
            sql = "select table_name TABLE_NAME, table_comment COMMENTS from information_schema.tables where table_schema='"+dbName+"' and table_type='base table'";
        } else if (dbKind.toUpperCase().equals("ORACLE")) {
            sql = "select t1.TABLE_NAME,t2.COMMENTS from user_tables t1 ,user_tab_comments t2 where t1.table_name = t2.table_name(+)";
        } else if (dbKind.toUpperCase().equals("SQLSERVER")) {
            sql = "select TABLE_NAME=d.name,COMMENTS=f.value  from sysobjects d left join sys.extended_properties f on d.id=f.major_id and f.minor_id=0 where d.xtype = 'u' and d.name != 'sysdiagrams'";
        }
        return sql;
    }

    /**
     * 得到SQL
     * @param dbKind
     * @param tableName
     * @return
     */
    public static String getColNameInfoSQL(String dbKind,String tableName){
        String sql = null;
        if (dbKind.toUpperCase().equals("MYSQL")) {
            sql = "select column_name COLUMN_NAME,column_default DATA_DEFAULT,is_nullable NULLABLE,data_type DATA_TYPE,character_maximum_length DATA_LENGTH,column_comment COMMENTS from information_schema.columns where table_name = '"+tableName+"' and table_schema = (select database()) order by ordinal_position";
        } else if (dbKind.toUpperCase().equals("ORACLE")) {
            sql = "select t1.COLUMN_NAME,t1.DATA_TYPE,t1.DATA_LENGTH,t1.NULLABLE,t1.DATA_DEFAULT,t2.COMMENTS from user_tab_cols t1, user_col_comments t2 where t1.table_name = '"+tableName+"' and t1.TABLE_NAME = t2.table_name and t1.COLUMN_NAME = t2.column_name(+)";
        } else if (dbKind.toUpperCase().equals("SQLSERVER")) {
            sql = "select  COLUMN_NAME = a.name ,DATA_TYPE = b.name,DATA_LENGTH = columnproperty(a.id, a.name, 'PRECISION') , NULLABLE = case when a.isnullable = 1 then '√' else '' end , DATA_DEFAULT = isnull(e.text, ''),COMMENTS = isnull(g.[value], '') from syscolumns a left join systypes b on a.xusertype = b.xusertype inner join sysobjects d on a.id = d.id and d.xtype = 'U' and d.name <> 'dtproperties' left join syscomments e on a.cdefault = e.id left join sys.extended_properties g on a.id = g.major_id and a.colid = g.minor_id left join sys.extended_properties f on d.id = f.major_id and f.minor_id = 0 where d.name = '"+tableName+"'";
        }
        return sql;
    }
}
