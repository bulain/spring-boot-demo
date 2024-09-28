package com.bulain.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MybatisPlusContext {

    private static DbType dbType;

    public static DbType getDbType(){
        return MybatisPlusContext.getDbType();
    }

    public static void setDbType(DbType dbType){
        MybatisPlusContext.dbType = dbType;
    }

}
