select t1.COLUMN_NAME,
       t1.DATA_TYPE,
       case
           when t1.DATA_TYPE = 'NUMBER' then
            case
            when t1.DATA_PRECISION is null then t1.DATA_LENGTH
            else t1.DATA_PRECISION end
           else t1.CHAR_LENGTH
       end as "DATA_LENGTH",
       t1.NULLABLE,
       t1.DATA_DEFAULT,
       t2.COMMENTS
from user_tab_cols t1,
     user_col_comments t2
where t1.table_name = ?
  and t1.TABLE_NAME = t2.table_name
  and t1.COLUMN_NAME = t2.column_name(+)
