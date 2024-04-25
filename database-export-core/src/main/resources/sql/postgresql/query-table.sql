select
    c.relname as TABLE_NAME,
    obj_description(c.oid,'pg_class') as COMMENTS
from
    pg_class c
        left join pg_namespace n on
        n.oid = c.relnamespace
where
    n.nspname = ?
  and c.relkind = 'r'
  and n.nspname not in ('pg_catalog', 'information_schema')
order by
    n.nspname,
    c.relname
