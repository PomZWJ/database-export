select
    COMMENTS,
    DATA_TYPE,
    a.COLUMN_NAME,
    NULLABLE,
    DATA_DEFAULT,
    IS_PRIMARY
from
    (
        select
            col_description ( A.attrelid,
                              A.attnum ) as COMMENTS,
            concat_ws ( '',
                        T.typname,
                        SUBSTRING ( format_type ( A.atttypid,
                                                  A.atttypmod )
                                    from
                                    '\(.*\)' ) ) as DATA_TYPE,
            A.attname as COLUMN_NAME,
            TC.is_nullable as NULLABLE,
            TC.column_default as DATA_DEFAULT
        from
            pg_class as C
            left join pg_attribute as A on A.attrelid = C.oid
            left join pg_type as T on A.atttypid = T.oid
            left join information_schema.columns as TC on A.attname = TC.column_name
        where
            1 = 1
          and C.relname = '%s'
          and A.attnum > 0
          and TC.table_catalog = '%s'
          and TC.table_name = '%s'
          and TC.table_schema = '%s') a
        left join (
        select
            tc.constraint_type as IS_PRIMARY,
            kcu.COLUMN_NAME
        from
            information_schema.table_constraints as tc
                join information_schema.key_column_usage as kcu on
                tc.constraint_name = kcu.constraint_name
        where
            tc.constraint_type = 'PRIMARY KEY'
          and tc.table_name = '%s' and tc.constraint_schema = '%s') b on
        a.COLUMN_NAME = b.COLUMN_NAME
