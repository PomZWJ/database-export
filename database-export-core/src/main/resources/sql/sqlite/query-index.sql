select
    name,
    sql
from
    sqlite_master
where
    type = 'index'
  and tbl_name = '%s'