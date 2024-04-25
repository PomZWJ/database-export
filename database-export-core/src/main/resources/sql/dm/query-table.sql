SELECT
    TABLE_NAME,
    COMMENTS
FROM
    all_tab_comments
WHERE
    table_type = 'TABLE'
  AND OWNER = ?