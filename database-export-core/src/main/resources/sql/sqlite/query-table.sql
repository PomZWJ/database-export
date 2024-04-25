select
    name as TABLE_NAME,
    '' as COMMENTS
from
    sqlite_master
where
    type = 'table'
  and name <> 'sqlite_sequence'