create database if not exists db1 default character set utf8 default collate utf8_general_ci;
create database if not exists db2 default character set utf8 default collate utf8_general_ci;
grant all on db1.* to sharding@localhost identified by 'dev';
grant all on db2.* to sharding@localhost identified by 'dev';

create table if not exists db1.t_order0 (order_id bigint not null auto_increment, user_id int not null, status varchar(50), primary key (order_id));
create table if not exists db1.t_order1 (order_id bigint not null auto_increment, user_id int not null, status varchar(50), primary key (order_id));
create table if not exists db1.t_order_item0 (order_item_id bigint not null auto_increment, order_id bigint not null, user_id int not null, primary key (order_item_id));
create table if not exists db1.t_order_item1 (order_item_id bigint not null auto_increment, order_id bigint not null, user_id int not null, primary key (order_item_id));

create table if not exists db2.t_order0 (order_id bigint not null auto_increment, user_id int not null, status varchar(50), primary key (order_id));
create table if not exists db2.t_order1 (order_id bigint not null auto_increment, user_id int not null, status varchar(50), primary key (order_id));
create table if not exists db2.t_order_item0 (order_item_id bigint not null auto_increment, order_id bigint not null, user_id int not null, primary key (order_item_id));
create table if not exists db2.t_order_item1 (order_item_id bigint not null auto_increment, order_id bigint not null, user_id int not null, primary key (order_item_id));

create table if not exists db1.t_hu (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hu2021 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hu2022 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hu2023 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hu2024 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));

create table if not exists db1.t_hint (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hint2021 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hint2022 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hint2023 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hint2024 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));

create table if not exists db1.t_dn (dn varchar(50) not null, pgi datetime, primary key (dn));

create table if not exists t_config (config_id bigint not null auto_increment, code int not null, value varchar(50), primary key (config_id));


