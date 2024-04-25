SELECT
    a.INDEX_NAME,
    b.INDEX_TYPE,
    b.UNIQUENESS,
    a.COLUMN_NAME,
    a.COLUMN_POSITION
FROM
    USER_IND_COLUMNS a
        inner join user_indexes b ON a.index_name = b.index_name AND a.table_name = b.table_name
WHERE 1=1
  and b.TABLE_OWNER = '%s' and a.TABLE_NAME = '%s' and b.INDEX_TYPE = 'NORMAL'
  AND a.INDEX_NAME NOT IN (
    SELECT  c a.
    FROM USER_CONSTRAINTS
    WHERE CONSTRAINT_TYPE = 'P'
    AND OWNER = '%s'  AND TABLE_NAME = '%s'
)
ORDER BY
    a.INDEX_NAME, a.COLUMN_POSITION;