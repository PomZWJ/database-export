SELECT
    col_description ( A.attrelid, A.attnum ) AS COMMENTS,
    concat_ws ( '', T.typname, SUBSTRING ( format_type ( A.atttypid, A.atttypmod ) FROM '\(.*\)' ) ) AS DATA_TYPE,
    A.attname AS COLUMN_NAME,
    TC.is_nullable AS NULLABLE,
    P.contype AS IS_PRIMARY,
    TC.column_default AS DATA_DEFAULT
FROM
    pg_class AS C  ,
    pg_attribute AS A left join pg_constraint P on A.atttypid = P.conrelid,
    pg_type T,
    information_schema.columns TC
WHERE
        C.relname = ?
  AND A.attrelid = C.oid
  AND A.atttypid = T.oid
  AND A.attnum > 0
  AND TC.table_catalog = ? and TC.table_name = ? AND A.attname = TC.column_name