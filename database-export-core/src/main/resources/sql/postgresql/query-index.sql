select
    cls.relname as INDEX_NAME,
    am.amname as INDEX_TYPE,
    idx.indisunique as NON_UNIQUE,
    array(
        select
		att.attname
	from
		pg_attribute att
	where
		att.attrelid = tab.oid
		and att.attnum = any(idx.indkey)
		and not att.attisdropped
		and att.attnum > 0
	order by
		att.attnum ) as COLUMN_NAME
from
    pg_index idx
        inner join pg_class cls on
        cls.oid = idx.indexrelid
        inner join pg_class tab on
        tab.oid = idx.indrelid
        inner join pg_am am on
        am.oid = cls.relam
        inner join pg_namespace ns on
        ns.oid = tab.relnamespace
where
    tab.relname = '%s' and ns.nspname = '%s'
