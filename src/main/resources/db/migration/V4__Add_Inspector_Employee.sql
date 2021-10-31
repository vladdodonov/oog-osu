insert into d_employee(last_name, first_name, middle_name, email, phone, department_id, qualification, username)
values ('Инспектор', 'Инспектор', 'Инспектор', 'admin@admin.ru', '440000',
        (select id from d_department where name = 'Департамент админа'),
        'LEAD', 'inspector');