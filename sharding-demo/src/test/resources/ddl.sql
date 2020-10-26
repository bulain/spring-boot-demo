create table if not exists t_order0 (order_id bigint not null auto_increment, user_id int not null, status varchar(50), primary key (order_id));
create table if not exists t_order1 (order_id bigint not null auto_increment, user_id int not null, status varchar(50), primary key (order_id));
create table if not exists t_order_item0 (order_item_id bigint not null auto_increment, order_id bigint not null, user_id int not null, primary key (order_item_id));
create table if not exists t_order_item1 (order_item_id bigint not null auto_increment, order_id bigint not null, user_id int not null, primary key (order_item_id));

create table if not exists t_config (config_id bigint not null auto_increment, code int not null, value varchar(50), primary key (config_id));


