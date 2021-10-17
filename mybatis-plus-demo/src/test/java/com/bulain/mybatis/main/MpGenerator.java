package com.bulain.mybatis.main;

public class MpGenerator {
    public static void main(String[] args) throws Exception {

//        //用来获取Mybatis-Plus.properties文件的配置信息
//        Properties rb = new Properties();
//        rb.load(new FileInputStream("src/test/resources/mybatis-plus.properties"));
//
//        // 代码生成器
//        AutoGenerator mpg = new AutoGenerator();
//
//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
//        gc.setOutputDir(rb.getProperty("mp.OutputDir"));
//        gc.setOpen(false);
//        gc.setBaseResultMap(true);
//        gc.setBaseColumnList(true);
//        gc.setDateType(DateType.ONLY_DATE);
//        gc.setAuthor(null);
//        gc.setMapperName("%sMapper");
//        gc.setXmlName("%sMapper");
//        gc.setServiceName(null);
//        gc.setServiceImplName(null);
//        gc.setControllerName(null);
//        mpg.setGlobalConfig(gc);
//
//        // 数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setDbType(DbType.MYSQL);
//        dsc.setUrl(rb.getProperty("mp.url"));
//        dsc.setDriverName(rb.getProperty("mp.driver"));
//        dsc.setUsername(rb.getProperty("mp.username"));
//        dsc.setPassword(rb.getProperty("mp.password"));
//        mpg.setDataSource(dsc);
//
//        // 包配置
//        PackageConfig pc = new PackageConfig();
//        pc.setParent(rb.getProperty("mp.parent"));
//        pc.setModuleName(rb.getProperty("mp.module"));
//        pc.setMapper("dao");
//        pc.setEntity("model");
//        mpg.setPackageInfo(pc);
//
//        // 自定义配置
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//            }
//        };
//        List<FileOutConfig> focList = new ArrayList<>();
//        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                tableInfo.getEntityName();
//                return rb.getProperty("mp.OutputDirXml")  + "/" + rb.getProperty("mp.module") + "/"
//                        + tableInfo.getMapperName() + StringPool.DOT_XML;
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);
//        mpg.setTemplate(new TemplateConfig().setXml(null));
//
//        // 策略配置
//        StrategyConfig sc = new StrategyConfig();
//        sc.setNaming(NamingStrategy.underline_to_camel);
//        sc.setColumnNaming(NamingStrategy.underline_to_camel);
//        sc.setEntityLombokModel(true);
//        sc.setEntityTableFieldAnnotationEnable(true);
//        sc.setVersionFieldName(rb.getProperty("mp.versionField"));
//        sc.setLogicDeleteFieldName(rb.getProperty("mp.logicDeleteField"));
//        sc.setInclude(new String[]{"orders"});
//        sc.setTablePrefix(rb.getProperty("mp.module"));
//        mpg.setStrategy(sc);
//        mpg.setTemplateEngine(new VelocityTemplateEngine());
//        mpg.execute();

    }
}
