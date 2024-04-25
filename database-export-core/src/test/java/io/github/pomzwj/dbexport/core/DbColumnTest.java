package io.github.pomzwj.dbexport.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.pomzwj.dbexport.core.dbservice.postgresql.PostgresqlColumnInfo;
import io.github.pomzwj.dbexport.core.domain.DbColumnInfo;
import io.github.pomzwj.dbexport.core.utils.ClassUtils;
import net.bytebuddy.dynamic.DynamicType;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class DbColumnTest {
    @Test
    public void test1()throws Exception{
        List<String>showColumnName = null;
        PostgresqlColumnInfo mySqlColumnInfo = new PostgresqlColumnInfo();
        mySqlColumnInfo.setColumnName("name");
        mySqlColumnInfo.setNullAble(false);
        mySqlColumnInfo.setDataType("varchar");
        DynamicType.Unloaded<DbColumnInfo> dbColumBean = ClassUtils.createDbColumBean(PostgresqlColumnInfo.class, showColumnName);
        dbColumBean.saveIn(new File("E://"));
        Class<? extends DbColumnInfo> clazz = dbColumBean.load(DbColumnInfo.class.getClassLoader()).getLoaded();
        DbColumnInfo target = clazz.newInstance();
        Gson gson = new GsonBuilder().serializeNulls().create();
        BeanUtils.copyProperties(target,mySqlColumnInfo);

        for (Field declaredField : ClassUtils.sortColumnField(clazz)) {
            //declaredField.setAccessible(true);
            System.out.println(declaredField.get(target));
        }
       /* for (Field declaredField : ClassUtils.sortColumnField(clazz) ) {
            declaredField.setAccessible(true);
            System.out.println(declaredField.get(target));
        }*/
    }
    @Test
    public void test2()throws Exception{

    }
}
