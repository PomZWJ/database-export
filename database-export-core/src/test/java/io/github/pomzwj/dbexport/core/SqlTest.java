package io.github.pomzwj.dbexport.core;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import io.github.pomzwj.dbexport.core.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SqlTest {
    @Test
    public void test() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" COLNAME AS COLUMN_NAME , ");
        sql.append(" TYPESCHEMA, ");
        sql.append(" TYPENAME AS DATA_TYPE, ");
        sql.append(" 'LENGTH' AS DATA_LENGTH, ");
        sql.append(" 'SCALE' AS DATA_SCALE,");
        sql.append(" 'DEFAULT' AS DATA_DEFAULT,");
        sql.append(" 'NULLS' AS NULLABLE,");
        sql.append(" REMARKS AS COMMENTS ");
        sql.append(" FROM SYSCAT.COLUMNS c ");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND TABSCHEMA = ? ");
        sql.append(" AND TABNAME = ? ");
        sql.append(" ORDER BY COLNO");
        System.out.println(sql.toString());
    }

}
