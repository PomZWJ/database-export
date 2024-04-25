SELECT a.INDEX_NAME,
       b.INDEX_TYPE,
       b.UNIQUENESS,
       a.COLUMN_NAME,
       a.COLUMN_POSITION,
       c.CONSTRAINT_TYPE
FROM USER_IND_COLUMNS a
         inner join user_indexes b
                    ON a.index_name = b.index_name
                        AND a.table_name = b.table_name
         left join USER_CONSTRAINTS c
                   on a.INDEX_NAME = c.INDEX_NAME
WHERE 1 = 1
  and a.TABLE_NAME = '%s'
  and b.INDEX_TYPE = 'NORMAL'

ORDER BY a.INDEX_NAME, a.COLUMN_POSITION
