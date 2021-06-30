insert into client(name)
values('test client');

insert into address(street, client_id)
values('test street', (select id from client where name='test client'));

insert into phone(number, client_id)
values('000-000', (select id from client where name='test client')),
       ('111-111', (select id from client where name='test client'));