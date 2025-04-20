-- create database db1, user sharding
-- create database db2, user sharding
create user sharding identified by "dev" default tablespace users temporary tablespace temp quota unlimited on users;
grant connect, resource to sharding;
grant create view to sharding;

create user db1 default tablespace users temporary tablespace temp quota unlimited on users;
grant resource to sharding;
grant create view to sharding;

create user db2 default tablespace users temporary tablespace temp quota unlimited on users;
grant resource to sharding;
grant create view to sharding;

create table db1.t_order_0 (order_id number(20) not null, user_id int not null, status varchar2(50), primary key (order_id));
create table db1.t_order_1 (order_id number(20) not null, user_id int not null, status varchar2(50), primary key (order_id));
create table db1.t_order_item_0 (order_item_id number(20) not null, order_id number(20) not null, user_id int not null, primary key (order_item_id));
create table db1.t_order_item_1 (order_item_id number(20) not null, order_id number(20) not null, user_id int not null, primary key (order_item_id));

create table db2.t_order_0 (order_id number(20) not null, user_id int not null, status varchar2(50), primary key (order_id));
create table db2.t_order_1 (order_id number(20) not null, user_id int not null, status varchar2(50), primary key (order_id));
create table db2.t_order_item_0 (order_item_id number(20) not null, order_id number(20) not null, user_id int not null, primary key (order_item_id));
create table db2.t_order_item_1 (order_item_id number(20) not null, order_id number(20) not null, user_id int not null, primary key (order_item_id));

create table db1.t_hu (hu varchar2(50) not null, dn varchar2(50) not null, pgi datetime, primary key (hu));
create table db1.t_hu_2021 (hu varchar2(50) not null, dn varchar2(50) not null, pgi datetime, primary key (hu));
create table db1.t_hu_2022 (hu varchar2(50) not null, dn varchar2(50) not null, pgi datetime, primary key (hu));
create table db1.t_hu_2023 (hu varchar2(50) not null, dn varchar2(50) not null, pgi datetime, primary key (hu));
create table db1.t_hu_2024 (hu varchar2(50) not null, dn varchar2(50) not null, pgi datetime, primary key (hu));

create table db1.t_hint (hu varchar2(50) not null, dn varchar2(50) not null, pgi datetime, primary key (hu));
create table db1.t_hint_2021 (hu varchar2(50) not null, dn varchar2(50) not null, pgi datetime, primary key (hu));
create table db1.t_hint_2022 (hu varchar2(50) not null, dn varchar2(50) not null, pgi datetime, primary key (hu));
create table db1.t_hint_2023 (hu varchar2(50) not null, dn varchar2(50) not null, pgi datetime, primary key (hu));
create table db1.t_hint_2024 (hu varchar2(50) not null, dn varchar2(50) not null, pgi datetime, primary key (hu));

create table db1.t_dn (dn varchar2(50) not null, pgi datetime, primary key (dn));

create table db1.t_config (config_id number(20) not null, code int not null, value varchar2(50), primary key (config_id));
create table db2.t_config (config_id number(20) not null, code int not null, value varchar2(50), primary key (config_id));


