select
    name,
    type,
    comment,
    default_kind,
    default_expression
from
    system.columns
where
    1 = 1
  and table = '%s'
  and database = '%s'
