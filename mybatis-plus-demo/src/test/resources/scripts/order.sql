create table orders(
        id                      bigint(20) not null auto_increment,
                
        order_no                varchar(50) not null,
        extn_ref_no1            varchar(50),
        extn_ref_no2            varchar(50),
        extn_ref_no3            varchar(50),
        
        created_via             varchar(50),
        remarks                 varchar(500),
        created_at              datetime,
        created_by              varchar(50),
        updated_at              datetime,
        updated_by              varchar(50),
        version                 bigint(20) default 0,
        deleted                 int(1) unsigned default 0,
        archived                char(1),
        
        primary key (id)
)engine=innodb default charset=utf8;
create unique index idx_orders_1 on orders(order_no);