select column_name              COLUMN_NAME,
       column_default           DATA_DEFAULT,
       is_nullable              NULLABLE,
       data_type                DATA_TYPE,
       character_maximum_length DATA_LENGTH,
       column_comment           COMMENTS,
       COLUMN_TYPE              COLUMN_TYPE,
       EXTRA                    EXTRA_INFO,
       COLUMN_KEY               COLUMN_KEY
from information_schema.columns
where table_name = ?
  and table_schema = (select database())
order by ordinal_position