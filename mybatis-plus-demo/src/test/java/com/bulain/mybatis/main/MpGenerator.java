package com.bulain.mybatis.main;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

public class MpGenerator {
    public static void main(String[] args) throws Exception {

        //用来获取Mybatis-Plus.properties文件的配置信息
        Properties rb = new Properties();
        rb.load(new FileInputStream("src/test/resources/mybatis-plus.properties"));

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(rb.getProperty("mp.OutputDir"));
        gc.setOpen(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setAuthor(null);
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName(null);
        gc.setServiceImplName(null);
        gc.setControllerName(null);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl(rb.getProperty("mp.url"));
        dsc.setDriverName(rb.getProperty("mp.driver"));
        dsc.setUsername(rb.getProperty("mp.username"));
        dsc.setPassword(rb.getProperty("mp.password"));
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(rb.getProperty("mp.parent"));
        pc.setModuleName(rb.getProperty("mp.module"));
        pc.setMapper("dao");
        pc.setEntity("model");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                tableInfo.getEntityName();
                return rb.getProperty("mp.OutputDirXml")  + "/" + rb.getProperty("mp.module") + "/"
                        + tableInfo.getMapperName() + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig sc = new StrategyConfig();
        sc.setNaming(NamingStrategy.underline_to_camel);
        sc.setColumnNaming(NamingStrategy.underline_to_camel);
        sc.setEntityLombokModel(true);
        sc.setEntityTableFieldAnnotationEnable(true);
        sc.setVersionFieldName(rb.getProperty("mp.versionField"));
        sc.setLogicDeleteFieldName(rb.getProperty("mp.logicDeleteField"));
        sc.setInclude(new String[]{"orders"});
        sc.setTablePrefix(rb.getProperty("mp.module"));
        mpg.setStrategy(sc);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();

    }
}
