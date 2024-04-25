SELECT
    TYPE,
    fields
FROM
    (
        SELECT
            'partition_key' AS TYPE,
            partition_key AS fields,
            0 AS indexOrder
        FROM
            system.tables
        WHERE
            database = '%s'
          AND name = '%s'
        UNION ALL
        SELECT
            'primary_key' AS TYPE,
            primary_key AS fields,
            1 AS indexOrder
        FROM
            system.tables
        WHERE
            database = '%s'
          AND name = '%s'
        UNION ALL
        SELECT
            'sorting_key' AS TYPE,
            sorting_key AS fields,
            2 AS indexOrder
        FROM
            system.tables
        WHERE
            database = '%s'
          AND name = '%s'
        UNION ALL
        SELECT
            'sampling_key' AS TYPE,
            sampling_key AS fields,
            3 AS indexOrder
        FROM
            system.tables
        WHERE
            database = '%s'
          AND name = '%s')
ORDER BY
    indexOrder ASC
