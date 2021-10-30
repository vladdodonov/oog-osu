insert into principal (username, password, role)
VALUES ('admin', 'admin', 'ADMIN')
     , ('inspector', 'inspector', 'INSPECTOR')
     , ('ivanovii', 'ivanovii', 'LEAD')
     , ('petrovpp', 'petrovpp', 'EXECUTOR')
     , ('sidorovss', 'sidorovss', 'EXECUTOR')
     , ('evgenovee', 'evgenovee', 'EXECUTOR')
     , ('sergeevss', 'sergeevss', 'LEAD')
     , ('stanislavovss', 'stanislavovss', 'EXECUTOR')
     , ('antonovaa', 'antonovaa', 'EXECUTOR')
     , ('alekseevaa', 'alekseevaa', 'EXECUTOR');

insert into d_department(name)
values ('Юридический департамент'),
       ('Автохозяйственный департамент');


insert into d_employee(last_name, first_name, middle_name, email, phone, department_id, qualification, username)
values ('Иванов', 'Иван', 'Иванович', 'ivanov@mail.ru', '440000',
        (select id from d_department where name = 'Автохозяйственный департамент'),
        'LEAD', 'ivanovii'),
       ('Петров', 'Петр', 'Петрович', 'petrov@mail.ru', '440001',
        (select id from d_department where name = 'Автохозяйственный департамент'),
        'SENIOR', 'petrovpp'),
       ('Сидоров', 'Сидор', 'Сидорович', 'sidorov@mail.ru', '440002',
        (select id from d_department where name = 'Автохозяйственный департамент'),
        'MIDDLE', 'sidorovss'),
       ('Евгенов', 'Евгений', 'Евгеньевич', 'evgenov@mail.ru', '440003',
        (select id from d_department where name = 'Автохозяйственный департамент'),
        'JUNIOR', 'evgenovee'),
       ('Сергеев', 'Сергей', 'Сергеевич', 'sergeev@mail.ru', '450000',
        (select id from d_department where name = 'Юридический департамент'),
        'LEAD', 'sergeevss'),
       ('Станиславов', 'Станислав', 'Станиславович', 'stanislavov@mail.ru', '450001',
        (select id from d_department where name = 'Юридический департамент'), 'SENIOR', 'stanislavovss'),
       ('Антонов', 'Антон', 'Антонович', 'antonov@mail.ru', '450002',
        (select id from d_department where name = 'Юридический департамент'),
        'MIDDLE', 'antonovaa'),
       ('Алексеев', 'Алексей', 'Алексеевич', 'alekseev@mail.ru', '450003',
        (select id from d_department where name = 'Юридический департамент'), 'JUNIOR', 'alekseevaa');

insert into d_topic (name, department_id)
VALUES ('Законодательство и акты органов МСУ', (select id from d_department where name = 'Юридический департамент')),
       ('Обжалование решений органов МСУ', (select id from d_department where name = 'Юридический департамент')),
       ('Состояние дорог', (select id from d_department where name = 'Автохозяйственный департамент')),
       ('Налоги и сборы в сфере автомобильного транспорта',
        (select id from d_department where name = 'Автохозяйственный департамент'));