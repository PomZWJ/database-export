select COLUMN_NAME = a.name,
       DATA_TYPE = b.name,
       DATA_LENGTH = columnproperty(a.id, a.name, 'PRECISION'),
       NULLABLE = case when a.isnullable = 1 then '√' else '' end,
       DATA_DEFAULT = isnull(e.text, ''),
       COMMENTS = isnull(g.[value], '')
from syscolumns a
         left join systypes b on a.xusertype = b.xusertype
         inner join sysobjects d on a.id = d.id and d.xtype = 'U' and d.name <> 'dtproperties'
         left join syscomments e on a.cdefault = e.id
         left join sys.extended_properties g on a.id = g.major_id and a.colid = g.minor_id
         left join sys.extended_properties f on d.id = f.major_id and f.minor_id = 0
where d.name = ?