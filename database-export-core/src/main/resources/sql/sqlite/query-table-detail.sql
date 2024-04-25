select
    name as COLUMN_NAME,
    type as DATA_TYPE,
    `notnull` as NULLABLE ,
    dflt_value as DATA_DEFAULT ,
    pk as ISPK
from
    pragma_table_info('%s')