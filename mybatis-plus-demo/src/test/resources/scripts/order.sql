CREATE TABLE ORDERS(
        ID                      BIGINT(20) NOT NULL AUTO_INCREMENT,
                
        ORDER_NO                VARCHAR(50) NOT NULL,
        EXTN_REF_NO1            VARCHAR(50),
        EXTN_REF_NO2            VARCHAR(50),
        EXTN_REF_NO3            VARCHAR(50),
        
        CREATED_VIA             VARCHAR(50),
        REMARKS                 VARCHAR(500),
        CREATED_AT              DATETIME,
        CREATED_BY              VARCHAR(50),
        UPDATED_AT              DATETIME,
        UPDATED_BY              VARCHAR(50),
        VERSION                 BIGINT(20),
        
        PRIMARY KEY (ID)
)ENGINE=INNODB DEFAULT CHARSET=UTF8;
CREATE UNIQUE INDEX IDX_ORDERS_1 ON ORDERS(ORDER_NO);