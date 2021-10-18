-- // create table demo_order
-- Migration SQL that makes the change goes here.
create table demo_order
(
    id           varchar(20) not null,

    order_no     varchar(50) not null,
    extn_ref_no1 varchar(50),
    extn_ref_no2 varchar(50),
    extn_ref_no3 varchar(50),

    created_via  varchar(50),
    remarks      varchar(500),
    created_at   datetime,
    created_by   varchar(50),
    updated_at   datetime,
    updated_by   varchar(50),
    version      bigint(20)      default 0,
    deleted      int(1) unsigned default 0,
    archived     char(1),

    primary key (id)
);
create unique index idx_demo_order_1 on demo_order (order_no);

-- //@UNDO
-- SQL to undo the change goes here.
drop table demo_order;

