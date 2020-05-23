CREATE TABLE IF NOT EXISTS DB1.T_ORDER0 (ORDER_ID BIGINT NOT NULL AUTO_INCREMENT, USER_ID INT NOT NULL, STATUS VARCHAR(50), PRIMARY KEY (ORDER_ID));
CREATE TABLE IF NOT EXISTS DB1.T_ORDER1 (ORDER_ID BIGINT NOT NULL AUTO_INCREMENT, USER_ID INT NOT NULL, STATUS VARCHAR(50), PRIMARY KEY (ORDER_ID));
CREATE TABLE IF NOT EXISTS DB1.T_ORDER_ITEM0 (ORDER_ITEM_ID BIGINT NOT NULL AUTO_INCREMENT, ORDER_ID BIGINT NOT NULL, USER_ID INT NOT NULL, PRIMARY KEY (ORDER_ITEM_ID));
CREATE TABLE IF NOT EXISTS DB1.T_ORDER_ITEM1 (ORDER_ITEM_ID BIGINT NOT NULL AUTO_INCREMENT, ORDER_ID BIGINT NOT NULL, USER_ID INT NOT NULL, PRIMARY KEY (ORDER_ITEM_ID));

CREATE TABLE IF NOT EXISTS DB2.T_ORDER0 (ORDER_ID BIGINT NOT NULL AUTO_INCREMENT, USER_ID INT NOT NULL, STATUS VARCHAR(50), PRIMARY KEY (ORDER_ID));
CREATE TABLE IF NOT EXISTS DB2.T_ORDER1 (ORDER_ID BIGINT NOT NULL AUTO_INCREMENT, USER_ID INT NOT NULL, STATUS VARCHAR(50), PRIMARY KEY (ORDER_ID));
CREATE TABLE IF NOT EXISTS DB2.T_ORDER_ITEM0 (ORDER_ITEM_ID BIGINT NOT NULL AUTO_INCREMENT, ORDER_ID BIGINT NOT NULL, USER_ID INT NOT NULL, PRIMARY KEY (ORDER_ITEM_ID));
CREATE TABLE IF NOT EXISTS DB2.T_ORDER_ITEM1 (ORDER_ITEM_ID BIGINT NOT NULL AUTO_INCREMENT, ORDER_ID BIGINT NOT NULL, USER_ID INT NOT NULL, PRIMARY KEY (ORDER_ITEM_ID));