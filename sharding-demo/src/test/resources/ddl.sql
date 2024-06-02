create database if not exists db1 default character set utf8mb4 default collate utf8mb4_general_ci;
create database if not exists db2 default character set utf8mb4 default collate utf8mb4_general_ci;
grant all on db1.* to sharding@localhost identified by 'dev';
grant all on db2.* to sharding@localhost identified by 'dev';

create table if not exists db1.t_order_0 (order_id bigint not null auto_increment, user_id int not null, status varchar(50), primary key (order_id));
create table if not exists db1.t_order_1 (order_id bigint not null auto_increment, user_id int not null, status varchar(50), primary key (order_id));
create table if not exists db1.t_order_item_0 (order_item_id bigint not null auto_increment, order_id bigint not null, user_id int not null, primary key (order_item_id));
create table if not exists db1.t_order_item_1 (order_item_id bigint not null auto_increment, order_id bigint not null, user_id int not null, primary key (order_item_id));

create table if not exists db2.t_order_0 (order_id bigint not null auto_increment, user_id int not null, status varchar(50), primary key (order_id));
create table if not exists db2.t_order_1 (order_id bigint not null auto_increment, user_id int not null, status varchar(50), primary key (order_id));
create table if not exists db2.t_order_item_0 (order_item_id bigint not null auto_increment, order_id bigint not null, user_id int not null, primary key (order_item_id));
create table if not exists db2.t_order_item_1 (order_item_id bigint not null auto_increment, order_id bigint not null, user_id int not null, primary key (order_item_id));

create table if not exists db1.t_hu (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hu_2021 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hu_2022 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hu_2023 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hu_2024 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));

create table if not exists db1.t_hint (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hint_2021 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hint_2022 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hint_2023 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));
create table if not exists db1.t_hint_2024 (hu varchar(50) not null, dn varchar(50) not null, pgi datetime, primary key (hu));

create table if not exists db1.t_dn (dn varchar(50) not null, pgi datetime, primary key (dn));

create table if not exists db1.t_config (config_id bigint not null auto_increment, code int not null, value varchar(50), primary key (config_id));
create table if not exists db2.t_config (config_id bigint not null auto_increment, code int not null, value varchar(50), primary key (config_id));


