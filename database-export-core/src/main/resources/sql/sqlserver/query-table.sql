SELECT
    TABLE_NAME = d.NAME,
    COMMENTS = f.VALUE
FROM
    sysobjects d
        LEFT JOIN sys.extended_properties f ON d.id = f.major_id
        AND f.minor_id = 0
WHERE
    d.xtype = 'u'
  AND d.NAME != 'sysdiagrams'