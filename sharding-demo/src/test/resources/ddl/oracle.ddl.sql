-- create database db1, user db1
create user db1 identified by "dev" default tablespace users temporary tablespace temp quota unlimited on users;
grant connect, resource to db1;
grant create view to db1;

create table db1.t_order (order_id number(20) not null, pgi date not null, user_id number(10) not null, status varchar2(50), primary key (order_id));
create table db1.t_order_2023 (order_id number(20) not null, pgi date not null, user_id number(10) not null, status varchar2(50), primary key (order_id));
create table db1.t_order_2024 (order_id number(20) not null, pgi date not null, user_id number(10) not null, status varchar2(50), primary key (order_id));
create table db1.t_order_item (order_item_id number(20) not null, order_id number(20) not null, pgi date not null, user_id number(10) not null, primary key (order_item_id));
create table db1.t_order_item_2023 (order_item_id number(20) not null, order_id number(20) not null, pgi date not null, user_id number(10) not null, primary key (order_item_id));
create table db1.t_order_item_2024 (order_item_id number(20) not null, order_id number(20) not null, pgi date not null, user_id number(10) not null, primary key (order_item_id));

create table db1.t_hu (hu varchar2(50) not null, dn varchar2(50) not null, pgi date not null, primary key (hu));
create table db1.t_hu_2021 (hu varchar2(50) not null, dn varchar2(50) not null, pgi date not null, primary key (hu));
create table db1.t_hu_2022 (hu varchar2(50) not null, dn varchar2(50) not null, pgi date not null, primary key (hu));
create table db1.t_hu_2023 (hu varchar2(50) not null, dn varchar2(50) not null, pgi date not null, primary key (hu));
create table db1.t_hu_2024 (hu varchar2(50) not null, dn varchar2(50) not null, pgi date not null, primary key (hu));

create table db1.t_hint (hu varchar2(50) not null, dn varchar2(50) not null, pgi date not null, primary key (hu));
create table db1.t_hint_2021 (hu varchar2(50) not null, dn varchar2(50) not null, pgi date not null, primary key (hu));
create table db1.t_hint_2022 (hu varchar2(50) not null, dn varchar2(50) not null, pgi date not null, primary key (hu));
create table db1.t_hint_2023 (hu varchar2(50) not null, dn varchar2(50) not null, pgi date not null, primary key (hu));
create table db1.t_hint_2024 (hu varchar2(50) not null, dn varchar2(50) not null, pgi date not null, primary key (hu));

create table db1.t_dn (dn varchar2(50) not null, pgi date not null, primary key (dn));

create table db1.t_config (config_id number(20) not null, code number(10) not null, value varchar2(50), primary key (config_id));

-- create database db2, user db2
--create user db2 identified by "dev" default tablespace users temporary tablespace temp quota unlimited on users;
--grant connect, resource to db2;
--grant create view to db2;
--
--create table db2.t_order (order_id number(20) not null, pgi date not null, user_id number(10) not null, status varchar2(50), primary key (order_id));
--create table db2.t_order_2023 (order_id number(20) not null, pgi date not null, user_id number(10) not null, status varchar2(50), primary key (order_id));
--create table db2.t_order_2024 (order_id number(20) not null, pgi date not null, user_id number(10) not null, status varchar2(50), primary key (order_id));
--create table db2.t_order_item (order_item_id number(20) not null, order_id number(20) not null, pgi date not null, user_id number(10) not null, primary key (order_item_id));
--create table db2.t_order_item_2023 (order_item_id number(20) not null, order_id number(20) not null, pgi date not null, user_id number(10) not null, primary key (order_item_id));
--create table db2.t_order_item_2024 (order_item_id number(20) not null, order_id number(20) not null, pgi date not null, user_id number(10) not null, primary key (order_item_id));
--
--create table db2.t_config (config_id number(20) not null, code number(10) not null, value varchar2(50), primary key (config_id));
