select
    table_name as TABLE_NAME,
    table_comment as COMMENTS
from
    information_schema.tables
where
    1 = 1
  and table_schema = ?
  and table_type = 'BASE TABLE'
