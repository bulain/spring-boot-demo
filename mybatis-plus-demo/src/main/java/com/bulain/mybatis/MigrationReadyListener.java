package com.bulain.mybatis;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.migration.ConnectionProvider;
import org.apache.ibatis.migration.DataSourceConnectionProvider;
import org.apache.ibatis.migration.MigrationLoader;
import org.apache.ibatis.migration.operations.PendingOperation;
import org.apache.ibatis.migration.operations.UpOperation;
import org.apache.ibatis.migration.options.DatabaseOperationOption;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import lombok.extern.slf4j.Slf4j;

/***
 * 数据库脚本升级程序
 */
@Slf4j
public class MigrationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("onApplicationEvent() - start");

        try {
            // 准备资源文件
            ConfigurableApplicationContext applicationContext = event.getApplicationContext();
            Resource resource = applicationContext.getResource("classpath:mysql/environments/development.properties");
            if (!resource.exists()) {
                log.error("{} not exists", resource.getDescription());
                return;
            }

            // 资源配置信息
            Properties envProps = new Properties();
            try (InputStream is = resource.getInputStream()) {
                envProps.load(is);
            }

            // 数据库连接
            DataSource dataSource = applicationContext.getBean(DataSource.class);
            ConnectionProvider connectionProvider = new DataSourceConnectionProvider(dataSource);

            // 升级脚本文件
            Resource[] resources = applicationContext.getResources("classpath:mysql/scripts/*.sql");
            String charset = envProps.getProperty("script_char_set");
            MigrationLoader migrationLoader = new ClasspathMigrationLoader(resources, charset, envProps);

            // 操作定义
            DatabaseOperationOption option = getOperationOption(envProps);

            // 输出流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(baos);
            printStream.println();

            // PendingOperation
            try {
                PendingOperation pending = new PendingOperation();
                pending.operate(connectionProvider, migrationLoader, option, printStream);
            } catch (Exception e) {
                log.warn("onApplicationEvent() - {}", e.getMessage());
            }

            // 日志输出
            String ret = baos.toString();
            if (StringUtils.isNotBlank(ret)) {
                log.info(ret);
            }
            baos.reset();

            // UpOperation
            UpOperation up = new UpOperation();
            up.operate(connectionProvider, migrationLoader, option, printStream);

            // 日志输出
            ret = baos.toString();
            if (StringUtils.isNotBlank(ret)) {
                log.info(ret);
            }
            baos.reset();

        } catch (Exception e) {
            log.error("onApplicationEvent() - ", e);
        }

        log.info("onApplicationEvent() - end");

    }

    private DatabaseOperationOption getOperationOption(Properties props) {
        DatabaseOperationOption option = new DatabaseOperationOption();
        option.setChangelogTable(props.getProperty("changelog"));
        option.setStopOnError(true);
        option.setEscapeProcessing(false);
        option.setAutoCommit(Boolean.valueOf(props.getProperty("auto_commit")));
        option.setFullLineDelimiter(Boolean.valueOf(props.getProperty("full_line_delimiter")));
        option.setSendFullScript(Boolean.valueOf(props.getProperty("send_full_script")));
        option.setRemoveCRs(Boolean.valueOf(props.getProperty("remove_crs")));
        String delimiterString = props.getProperty("delimiter");
        option.setDelimiter(delimiterString == null ? ";" : delimiterString);
        return option;
    }

}
