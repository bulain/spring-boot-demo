package com.bulain.mybatis.core.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;

/**
 * 忽略逻辑删除字段（无视逻辑删除字段）
 */
public abstract class DirectMethod extends AbstractMethod {

    public DirectMethod(String name) {
        super(name);
    }

    @Override
    protected String sqlWhereEntityWrapper(boolean newLine, TableInfo table) {
        /*
         * Wrapper SQL
         */
        String _sgEs_ = "<bind name=\"_sgEs_\" value=\"ew.sqlSegment != null and ew.sqlSegment != ''\"/>";
        String andSqlSegment = SqlScriptUtils.convertIf(String.format(" AND ${%s}", WRAPPER_SQLSEGMENT), String.format("_sgEs_ and %s", WRAPPER_NONEMPTYOFNORMAL), true);
        String lastSqlSegment = SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT), String.format("_sgEs_ and %s", WRAPPER_EMPTYOFNORMAL), true);

        /*
         * 普通 SQL 注入
         */
        String sqlScript = table.getAllSqlWhere(false, false, true, WRAPPER_ENTITY_DOT);
        sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER_ENTITY), true);
        sqlScript = SqlScriptUtils.convertWhere(sqlScript + NEWLINE + andSqlSegment) + NEWLINE + lastSqlSegment;
        sqlScript = SqlScriptUtils.convertIf(_sgEs_ + NEWLINE + sqlScript, String.format("%s != null", WRAPPER), true);
        return newLine ? NEWLINE + sqlScript : sqlScript;
    }

}
