CREATE TABLE BLOG(
        ID                      BIGINT(20) NOT NULL AUTO_INCREMENT,
                
        TITLE                   VARCHAR(50) NOT NULL,
        DESCR                   VARCHAR(50),
        ACTIVE_FLAG             VARCHAR(50),
        
        CREATED_VIA             VARCHAR(50),
        REMARKS                 VARCHAR(500),
        CREATED_AT              DATETIME,
        CREATED_BY              VARCHAR(50),
        UPDATED_AT              DATETIME,
        UPDATED_BY              VARCHAR(50),
        
        PRIMARY KEY (ID)
)ENGINE=INNODB DEFAULT CHARSET=UTF8;
