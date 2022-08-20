package com.bulain.mybatis.demo.dao;

import com.bulain.mybatis.MybatisPlusApplication;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
public class SqlSessionDemo {

    @Autowired
    private SqlSession sqlSession;

    @Test
    public void testSqlSession(){
        Assertions.assertNotNull(sqlSession);

        List<Object> objects = sqlSession.selectList("com.bulain.mybatis.demo.dao.OrderMapper.selectList");
        Assertions.assertNotNull(objects);

    }

}
