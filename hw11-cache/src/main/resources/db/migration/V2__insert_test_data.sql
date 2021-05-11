insert into address(id, street)
values('f07ee9b1-422c-4112-ab2a-8370e16ae266', 'test street');

insert into client(id, name, address_id)
values(0, 'test client', 'f07ee9b1-422c-4112-ab2a-8370e16ae266');

insert into phone(id, number, client_id)
values(0, '000-000', 0);