package com.bulain.mybatis.demo.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.bulain.mybatis.MybatisPlusApplication;
import com.bulain.mybatis.demo.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
public class SqlSessionDemo {

    @Test
    public void testSqlSession() {
        SqlSessionFactory sessionFactory = SqlHelper.sqlSessionFactory(Order.class);
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sessionFactory);

        Assertions.assertNotNull(sqlSession);

        String sqlStatement = SqlHelper.getSqlStatement(OrderMapper.class, SqlMethod.SELECT_LIST);
        try {
            List<Order> orders = sqlSession.selectList(sqlStatement);
            Assertions.assertNotNull(orders);
        } finally {
            SqlSessionUtils.closeSqlSession(sqlSession, sessionFactory);
        }

    }

    @Test
    public void testCusor() {
        SqlSessionFactory sessionFactory = SqlHelper.sqlSessionFactory(Order.class);
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sessionFactory);

        String sqlStatement = SqlHelper.getSqlStatement(OrderMapper.class, SqlMethod.SELECT_LIST);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        Map<String, Object> param = buildParam(queryWrapper);

        try (Cursor<Order> cursor = sqlSession.selectCursor(sqlStatement, param)) {
            Iterator<Order> iter = cursor.iterator();
            while (iter.hasNext()) {
                Order order = iter.next();
                log.debug("data: {}", order);
            }
        } catch (IOException e) {
            log.error("selectCursor()-", e);
        } finally {
            SqlSessionUtils.closeSqlSession(sqlSession, sessionFactory);
        }

    }

    @Test
    public void testResultHandler() {
        SqlSessionFactory sessionFactory = SqlHelper.sqlSessionFactory(Order.class);
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sessionFactory);

        String sqlStatement = SqlHelper.getSqlStatement(OrderMapper.class, SqlMethod.SELECT_LIST);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        Map<String, Object> param = buildParam(queryWrapper);

        try {
            sqlSession.select(sqlStatement, param,
                    (ResultHandler<Order>) resultContext -> {
                        Order order = resultContext.getResultObject();
                        log.debug("data: {}", order);
                    });
        } finally {
            SqlSessionUtils.closeSqlSession(sqlSession, sessionFactory);
        }

    }

    private static Map<String, Object> buildParam(QueryWrapper<?> queryWrapper) {
        Map<String, Object> param = new MapperMethod.ParamMap<>();
        param.put("ew", queryWrapper);
        return param;
    }

}
