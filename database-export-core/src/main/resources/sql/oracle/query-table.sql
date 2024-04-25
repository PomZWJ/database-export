SELECT
    t1.TABLE_NAME,
    t2.COMMENTS
FROM
    user_tables t1,
    user_tab_comments t2
WHERE
    t1.table_name = t2.table_name (+)