create table blog(
        id                      bigint(20) not null auto_increment,
                
        title                   varchar(50) not null,
        descr                   varchar(50),
        active_flag             varchar(50),
        
        created_via             varchar(50),
        remarks                 varchar(500),
        created_at              datetime,
        created_by              varchar(50),
        updated_at              datetime,
        updated_by              varchar(50),
        
        primary key (id)
)engine=innodb default charset=utf8;
