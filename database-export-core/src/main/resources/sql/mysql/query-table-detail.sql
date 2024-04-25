select
    column_name as COLUMN_NAME,
    column_default as DATA_DEFAULT,
    is_nullable as NULLABLE,
    data_type as DATA_TYPE,
    character_maximum_length as DATA_LENGTH,
    column_comment as COMMENTS,
    COLUMN_TYPE as COLUMN_TYPE,
    EXTRA as EXTRA_INFO,
    COLUMN_KEY as COLUMN_KEY
from
    information_schema.columns
where
    1 = 1
  and table_name = '%s'
  and table_schema = (
    select
        database())
order by
    ordinal_position
