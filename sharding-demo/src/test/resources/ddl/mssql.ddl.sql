-- create database db1, user db1
create login db1 with password = 'dev';
go
create database db1;
go
use db1;
go
create user db1 for login db1;
go
exec sp_addrolemember 'db_owner', 'db1';
go

create table db1.dbo.t_order (order_id bigint not null, pgi datetime not null, user_id int not null, status varchar(50), primary key (order_id));
create table db1.dbo.t_order_2023 (order_id bigint not null, pgi datetime not null, user_id int not null, status varchar(50), primary key (order_id));
create table db1.dbo.t_order_2024 (order_id bigint not null, pgi datetime not null, user_id int not null, status varchar(50), primary key (order_id));
create table db1.dbo.t_order_item (order_item_id bigint not null, order_id bigint not null, pgi datetime not null, user_id int not null, primary key (order_item_id));
create table db1.dbo.t_order_item_2023 (order_item_id bigint not null, order_id bigint not null, pgi datetime not null, user_id int not null, primary key (order_item_id));
create table db1.dbo.t_order_item_2024 (order_item_id bigint not null, order_id bigint not null, pgi datetime not null, user_id int not null, primary key (order_item_id));

create table db1.dbo.t_hu (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.dbo.t_hu_2021 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.dbo.t_hu_2022 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.dbo.t_hu_2023 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.dbo.t_hu_2024 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));

create table db1.dbo.t_hint (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.dbo.t_hint_2021 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.dbo.t_hint_2022 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.dbo.t_hint_2023 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));
create table db1.dbo.t_hint_2024 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime not null, primary key (hu));

create table db1.dbo.t_dn (dn varchar(50) not null, pgi datetime not null, primary key (dn));

create table db1.dbo.t_config (config_id bigint not null, code int not null, value varchar(50), primary key (config_id));

-- create database db2, user db2
--create login db2 with password = 'dev';
--go
--create database db2;
--go
--use db2;
--go
--create user db2 for login db2;
--go
--exec sp_addrolemember 'db_owner', 'db2';
--go
--
--create table db2.dbo.t_order (order_id bigint not null, pgi datetime not null, user_id int not null, status varchar(50), primary key (order_id));
--create table db2.dbo.t_order_2023 (order_id bigint not null, pgi datetime not null, user_id int not null, status varchar(50), primary key (order_id));
--create table db2.dbo.t_order_2024 (order_id bigint not null, pgi datetime not null, user_id int not null, status varchar(50), primary key (order_id));
--create table db2.dbo.t_order_item (order_item_id bigint not null, order_id bigint not null, pgi datetime not null, user_id int not null, primary key (order_item_id));
--create table db2.dbo.t_order_item_2023 (order_item_id bigint not null, order_id bigint not null, pgi datetime not null, user_id int not null, primary key (order_item_id));
--create table db2.dbo.t_order_item_2024 (order_item_id bigint not null, order_id bigint not null, pgi datetime not null, user_id int not null, primary key (order_item_id));
--
--create table db2.dbo.t_config (config_id bigint not null, code int not null, value varchar(50), primary key (config_id));
