-- create database db1, user db1
create user db1 with password 'dev';
create database db1;
grant all privileges on database db1 to db1;

create table db1.public.t_order_0 (order_id bigint not null, user_id int not null, status varchar(50), primary key (order_id));
create table db1.public.t_order_1 (order_id bigint not null, user_id int not null, status varchar(50), primary key (order_id));
create table db1.public.t_order_item_0 (order_item_id bigint not null, order_id bigint not null, user_id int not null, primary key (order_item_id));
create table db1.public.t_order_item_1 (order_item_id bigint not null, order_id bigint not null, user_id int not null, primary key (order_item_id));

create table db1.public.t_hu (hu varchar(50) not null, dn varchar(50) not null, pgi timestamp, primary key (hu));
create table db1.public.t_hu_2021 (hu varchar(50) not null, dn varchar(50) not null, pgi timestamp, primary key (hu));
create table db1.public.t_hu_2022 (hu varchar(50) not null, dn varchar(50) not null, pgi timestamp, primary key (hu));
create table db1.public.t_hu_2023 (hu varchar(50) not null, dn varchar(50) not null, pgi timestamp, primary key (hu));
create table db1.public.t_hu_2024 (hu varchar(50) not null, dn varchar(50) not null, pgi timestamp, primary key (hu));

create table db1.public.t_hint (hu varchar(50) not null, dn varchar(50) not null, pgi timestamp, primary key (hu));
create table db1.public.t_hint_2021 (hu varchar(50) not null, dn varchar(50) not null, pgi timestamp, primary key (hu));
create table db1.public.t_hint_2022 (hu varchar(50) not null, dn varchar(50) not null, pgi timestamp, primary key (hu));
create table db1.public.t_hint_2023 (hu varchar(50) not null, dn varchar(50) not null, pgi timestamp, primary key (hu));
create table db1.public.t_hint_2024 (hu varchar(50) not null, dn varchar(50) not null, pgi timestamp, primary key (hu));

create table db1.public.t_dn (dn varchar(50) not null, pgi timestamp, primary key (dn));

create table db1.public.t_config (config_id bigint not null, code int not null, value varchar(50), primary key (config_id));

-- create database db2, user db2
create user db2 with password 'dev';
create database db2;
grant all privileges on database db2 to db2;

create table db2.public.t_order_0 (order_id bigint not null, user_id int not null, status varchar(50), primary key (order_id));
create table db2.public.t_order_1 (order_id bigint not null, user_id int not null, status varchar(50), primary key (order_id));
create table db2.public.t_order_item_0 (order_item_id bigint not null, order_id bigint not null, user_id int not null, primary key (order_item_id));
create table db2.public.t_order_item_1 (order_item_id bigint not null, order_id bigint not null, user_id int not null, primary key (order_item_id));

create table db2.public.t_config (config_id bigint not null, code int not null, value varchar(50), primary key (config_id));

