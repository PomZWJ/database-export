package io.github.pomzwj.dbexport.core;

import io.github.pomzwj.dbexport.core.dbservice.mysql.MySqlColumnInfo;
import io.github.pomzwj.dbexport.core.dbservice.sqlserver.SqlServerColumnInfo;
import io.github.pomzwj.dbexport.core.domain.DbColumnInfo;
import io.github.pomzwj.dbexport.core.type.DataBaseType;
import io.github.pomzwj.dbexport.core.utils.ClassUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class BuddyTest {
    @Test
    public void test() throws Exception{
        Class<? extends DbColumnInfo> columnInfoClazz = DataBaseType.MYSQL.getColumnInfoClazz();
        Class<? extends DbColumnInfo> dbColumnLoaded = ClassUtils.createDbColumBean(columnInfoClazz, new ArrayList<>()).load(DbColumnInfo.class.getClassLoader()).getLoaded();

       /* SqlServerColumnInfo sqlServerColumnInfo = new SqlServerColumnInfo();
        sqlServerColumnInfo.setColumnName("name");
        sqlServerColumnInfo.setNullAble(false);*/
        MySqlColumnInfo mySqlColumnInfo = new MySqlColumnInfo();
        mySqlColumnInfo.setColumnName("name");
        mySqlColumnInfo.setDataType("varchar");
        mySqlColumnInfo.setNullAble(false);
        mySqlColumnInfo.setAutoIncrement(false);
        mySqlColumnInfo.setPrimary(true);
        mySqlColumnInfo.setDefaultVal("123");
        mySqlColumnInfo.setComments("111");
        DbColumnInfo dbColumnInfo = ClassUtils.copyDbColumnTarget(dbColumnLoaded, mySqlColumnInfo);
        for (Field declaredField : ClassUtils.sortColumnField(dbColumnLoaded)) {
            declaredField.setAccessible(true);
            //System.out.println(String.valueOf(declaredField.get(sqlServerColumnInfo)));
            System.out.println(declaredField.get(dbColumnInfo));
        }
    }
}
