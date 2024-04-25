select
    TABLE_NAME,
    INDEX_COMMENT,
    INDEX_NAME,
    COLUMN_NAME,
    NON_UNIQUE,
    INDEX_TYPE,
    SEQ_IN_INDEX
from
    information_schema.STATISTICS
where
    1 = 1
  and TABLE_SCHEMA = '%s'
  and TABLE_NAME = '%s'
