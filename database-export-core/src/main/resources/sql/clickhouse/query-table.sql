select
    name as TABLE_NAME,
    concat('engine: ', engine) as COMMENTS
from
    system.tables
where
    database = ?
  and engine != 'Distributed'
