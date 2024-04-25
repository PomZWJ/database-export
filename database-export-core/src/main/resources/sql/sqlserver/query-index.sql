SELECT
    i.index_id AS index_id,
    i.name AS index_name,
    i.type_desc AS index_type,
    c.name AS column_name,
    ic.is_descending_key AS is_descending
FROM
    sys.indexes i
        INNER JOIN
    sys.index_columns ic ON i.object_id = ic.object_id AND i.index_id = ic.index_id
        INNER JOIN
    sys.columns c ON ic.object_id = c.object_id AND c.column_id = ic.column_id
WHERE
    i.object_id = OBJECT_ID('%s')
  AND i.is_primary_key = 0
ORDER BY
    i.name,
    ic.index_column_id;