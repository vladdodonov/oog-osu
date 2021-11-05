update principal
set role = 'LEAD'
where username in (with cnt as (select count(de.username) as cnt, de.department_id as did
                                from d_employee de
                                group by de.department_id)
                   select distinct de.username
                   from d_employee de,
                        cnt
                   where de.department_id in (select did from cnt where cnt = 1));