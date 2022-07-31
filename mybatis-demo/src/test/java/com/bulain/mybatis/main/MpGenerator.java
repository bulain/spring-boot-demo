package com.bulain.mybatis.main;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.util.Collections;
import java.util.List;

public class MpGenerator {
    public static void main(String[] args) throws Exception {

        YamlPropertySourceLoader yamlLoader = new YamlPropertySourceLoader();
        List<PropertySource<?>> application = yamlLoader.load("application", new ClassPathResource("application.yml"));
        PropertySource<?> propertySource = application.iterator().next();
        String url = (String) propertySource.getProperty("spring.datasource.url");
        String username = (String) propertySource.getProperty("spring.datasource.username");
        String password = (String) propertySource.getProperty("spring.datasource.password");
        
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("Bulain") //设置作者
                            .dateType(DateType.TIME_PACK) //时间类型
                            .enableSwagger() //开启swagger模式
                            .fileOverride() //覆盖已生成文件
                            .disableOpenDir() //打开输出目录
                            .outputDir("target/mybatis-mysql"); //指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.bulain.mybatis") //设置父包名
                            .moduleName("demo") //设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "target/mybatis-mysql/mapper")); //设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("demo_blog", "demo_order") //设置需要生成的表名
                            .addTablePrefix("demo") //设置过滤表前缀
                            .entityBuilder()
                            .enableChainModel() //开启链式模型
                            .enableLombok() //开启lombok模型
                            .enableTableFieldAnnotation() //开启生成实体时生成字段注解
                            .idType(IdType.ASSIGN_ID) //主键的ID类型
                            //.superClass() //自定义继承的Entity类全称
                            .versionColumnName("version")//乐观锁字段
                            //.logicDeleteColumnName("deleted") //逻辑删除字段
                            .mapperBuilder()
                            .enableMapperAnnotation() //开启 @Mapper 注解
                            .enableBaseResultMap() //开启baseResultMap
                            .enableBaseColumnList() //开启baseColumnList
                            .serviceBuilder()
                            .formatServiceFileName("%sService") //服务接口
                            .controllerBuilder()
                            .formatFileName("%sCtrl"); //控制器
                })
                .templateEngine(new VelocityTemplateEngine()) //使用Velocity引擎模板
                .execute();

    }
}
