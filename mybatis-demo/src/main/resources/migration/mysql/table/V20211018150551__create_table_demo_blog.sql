-- // create table demo_blog
-- Migration SQL that makes the change goes here.
create table demo_blog
(
    id          varchar(20) not null,

    title       varchar(50) not null,
    descr       varchar(50),
    active_flag varchar(50),

    created_via varchar(50),
    remarks     varchar(500),
    created_at  datetime,
    created_by  varchar(50),
    updated_at  datetime,
    updated_by  varchar(50),

    primary key (id)
);

create index idx_demo_blog_1 on demo_blog (title);
create index idx_demo_blog_2 on demo_blog (descr);
create index idx_demo_blog_3 on demo_blog (created_at);
