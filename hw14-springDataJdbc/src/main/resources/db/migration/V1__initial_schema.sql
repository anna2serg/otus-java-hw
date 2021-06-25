create table client
(
    id bigserial not null primary key,
	name varchar(255) not null check (name <> '')
);

create table address
(
    id bigserial not null primary key,
    street varchar(255) not null check (street <> ''),
    client_id bigint not null constraint fk_client_address references client(id)
);

create table phone
(
    id bigserial not null primary key,
	number varchar(255) not null check (number <> ''),
	client_id bigint not null constraint fk_client_phone references client(id)
);