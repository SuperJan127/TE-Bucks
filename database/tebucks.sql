BEGIN TRANSACTION;

--rollback;

drop table if exists transfers;
drop table if exists accounts;
DROP TABLE IF EXISTS users;
drop table if exists transfer_types;
drop table if exists transfer_statuses;



CREATE TABLE users (
	user_id serial NOT NULL,
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(50),
	role varchar(20),
	CONSTRAINT pk_users PRIMARY KEY (user_id),
	CONSTRAINT uq_username UNIQUE (username)
);

create table accounts(
	account_id serial not null,
	user_id int not null,
	balance numeric not null, 
	constraint pk_accounts primary key (account_id),
	constraint fk_accounts_users foreign key (user_id) references users (user_id)
);

create table transfer_types (
	type_id serial not null,
	type_name varchar(50) not null,
	constraint pk_transfer_types primary key (type_id)
);

create table transfer_statuses (
	status_id serial not null,
	status_name varchar(50) not null,
	constraint pk_transfer_statuses primary key (status_id)
);


create table transfers (
	transfer_id serial not null,
	transfer_type_id int,
	transfer_status_id int,
	account_from int not null,
	account_to int not null,
	amount numeric not null,
	constraint pk_transfers primary key (transfer_id),
	constraint fk_transfer_type foreign key (transfer_type_id) references transfer_types(type_id),
	constraint fk_transfer_status foreign key (transfer_status_id) references transfer_statuses(status_id),
	constraint fk_transfer_account_to foreign key (account_to) references accounts(account_id),
	constraint fk_transfer_account_from foreign key (account_from) references accounts(account_id)
);

insert into transfer_types(type_name)
values ('Request'),
		('Send');

insert into transfer_statuses(status_name)
values ('Pending'),
		('Approved'),
		('Rejected');
		
		
COMMIT TRANSACTION;
