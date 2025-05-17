-- create database db1, user db1
create user db1@'%' identified by 'dev';
create database db1 default character set utf8mb4 default collate utf8mb4_general_ci;
grant all on db1.* to db1;

create table db1.t_order (order_id bigint not null, pgi datetime not null, user_id int not null, status varchar(50), primary key (order_id));
create table db1.t_order_2023 (order_id bigint not null, pgi datetime not null, user_id int not null, status varchar(50), primary key (order_id));
create table db1.t_order_2024 (order_id bigint not null, pgi datetime not null, user_id int not null, status varchar(50), primary key (order_id));
create table db1.t_order_item (order_item_id bigint not null, order_id bigint not null, pgi datetime not null, user_id int not null, primary key (order_item_id));
create table db1.t_order_item_2023 (order_item_id bigint not null, order_id bigint not null, pgi datetime not null, user_id int not null, primary key (order_item_id));
create table db1.t_order_item_2024 (order_item_id bigint not null, order_id bigint not null, pgi datetime not null, user_id int not null, primary key (order_item_id));

create table db1.t_hu (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.t_hu_2021 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.t_hu_2022 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.t_hu_2023 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.t_hu_2024 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));

create table db1.t_hint (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.t_hint_2021 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.t_hint_2022 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.t_hint_2023 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.t_hint_2024 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));

create table db1.t_dn (dn varchar(50) not null, pgi datetime not null, primary key (dn));

create table db1.t_config (config_id bigint not null, code int not null, value varchar(50), primary key (config_id));

-- create database db2, user db2
--create user db2@'%' identified by 'dev';
--create database db2 default character set utf8mb4 default collate utf8mb4_general_ci;
--grant all on db2.* to db2;
--
--create table db2.t_order (order_id bigint not null, pgi datetime not null, user_id int not null, status varchar(50), primary key (order_id));
--create table db2.t_order_2023 (order_id bigint not null, pgi datetime not null, user_id int not null, status varchar(50), primary key (order_id));
--create table db2.t_order_2024 (order_id bigint not null, pgi datetime not null, user_id int not null, status varchar(50), primary key (order_id));
--create table db2.t_order_2025 (order_id bigint not null, pgi datetime not null, user_id int not null, status varchar(50), primary key (order_id));
--create table db2.t_order_item (order_item_id bigint not null, order_id bigint not null, user_id int not null, primary key (order_item_id));
--create table db2.t_order_item_2023 (order_item_id bigint not null, order_id bigint not null, user_id int not null, primary key (order_item_id));
--create table db2.t_order_item_2024 (order_item_id bigint not null, order_id bigint not null, user_id int not null, primary key (order_item_id));
--create table db2.t_order_item_2025 (order_item_id bigint not null, order_id bigint not null, user_id int not null, primary key (order_item_id));
--
--create table db2.t_config (config_id bigint not null, code int not null, value varchar(50), primary key (config_id));
