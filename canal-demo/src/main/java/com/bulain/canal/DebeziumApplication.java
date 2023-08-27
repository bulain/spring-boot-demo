package com.bulain.canal;

import io.debezium.connector.mysql.MySqlConnector;
import io.debezium.connector.oracle.OracleConnector;
import io.debezium.connector.sqlserver.SqlServerConnector;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import io.debezium.storage.file.history.FileSchemaHistory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.storage.FileOffsetBackingStore;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class DebeziumApplication {

    public static void main(String[] args) throws IOException {
        //mysql();
        mssql();
        //oracle();
    }

    public static void mysql() throws IOException {

        // Define the configuration for the Debezium Engine with MySQL connector...
        final Properties props = new Properties();
        props.setProperty("name", "mysql-engine");
        props.setProperty("connector.class", MySqlConnector.class.getName());
        props.setProperty("offset.storage", FileOffsetBackingStore.class.getName());
        props.setProperty("offset.storage.file.filename", "/data/debezium/mysql/offsets.dat");
        props.setProperty("offset.flush.interval.ms", "60000");
        props.setProperty("schema.history.internal", FileSchemaHistory.class.getName());
        props.setProperty("schema.history.internal.file.filename", "/data/debezium/mysql/schemahistory.dat");
        /* begin connector properties */
        props.setProperty("database.hostname", "localhost");
        props.setProperty("database.port", "3306");
        props.setProperty("database.user", "dbzrepl");
        props.setProperty("database.password", "dev");
        props.setProperty("database.server.id", "1");
        props.setProperty("database.connectionTimeZone", "PRC");
        props.setProperty("database.include.list", "lip_dev");
        props.setProperty("inconsistent.schema.handling.mode", "warn");
        props.setProperty("topic.prefix", "dev");

        // Create the engine with this configuration ...
        try (DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                .using(props)
                .notifying(record -> {
                    log.info("{}", record);
                })
                .using((success, message, error) -> {
                    if (error != null) {
                        log.error("{}/{}", error, message);
                    }
                })
                .build()
        ) {

            // Run the engine asynchronously ...
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(engine);

            // Do something else or wait for a signal or an event
        }
        // Engine is stopped when the main code is finished

    }


    public static void mssql() throws IOException {

        // Define the configuration for the Debezium Engine with MySQL connector...
        final Properties props = new Properties();
        props.setProperty("name", "mssql-engine");
        props.setProperty("connector.class", SqlServerConnector.class.getName());
        props.setProperty("offset.storage", FileOffsetBackingStore.class.getName());
        props.setProperty("offset.storage.file.filename", "/data/debezium/mssql/offsets.dat");
        props.setProperty("offset.flush.interval.ms", "60000");
        props.setProperty("schema.history.internal", FileSchemaHistory.class.getName());
        props.setProperty("schema.history.internal.file.filename", "/data/debezium/mssql/schemahistory.dat");
        /* begin connector properties */
        props.setProperty("database.hostname", "localhost");
        props.setProperty("database.port", "1433");
        props.setProperty("database.user", "dbzrepl");
        props.setProperty("database.password", "dev");
        props.setProperty("database.names", "lip_dev");
        props.setProperty("database.encrypt", "false");
        props.setProperty("inconsistent.schema.handling.mode", "warn");
        props.setProperty("topic.prefix", "mssql");

        // Create the engine with this configuration ...
        try (DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                .using(props)
                .notifying(record -> {
                    log.info("{}", record);
                })
                .using((success, message, error) -> {
                    if (error != null) {
                        log.error("{}/{}", error, message);
                    }
                })
                .build()
        ) {

            // Run the engine asynchronously ...
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(engine);

            // Do something else or wait for a signal or an event
        }
        // Engine is stopped when the main code is finished

    }


    public static void oracle() throws IOException {

        // Define the configuration for the Debezium Engine with MySQL connector...
        final Properties props = new Properties();
        props.setProperty("name", "oracle-engine");
        props.setProperty("connector.class", OracleConnector.class.getName());
        props.setProperty("offset.storage", FileOffsetBackingStore.class.getName());
        props.setProperty("offset.storage.file.filename", "/data/debezium/oracle/offsets.dat");
        props.setProperty("offset.flush.interval.ms", "60000");
        props.setProperty("schema.history.internal", FileSchemaHistory.class.getName());
        props.setProperty("schema.history.internal.file.filename", "/data/debezium/oracle/schemahistory.dat");
        /* begin connector properties */
        props.setProperty("database.hostname", "localhost");
        props.setProperty("database.port", "1521");
        props.setProperty("database.user", "c##dbzrepl");
        props.setProperty("database.password", "dev");
        props.setProperty("database.server.id", "1");
        props.setProperty("database.dbname", "xe");
        props.setProperty("database.serverTimezone", "RPC");
        props.setProperty("database.connection.adapter", "logminer");
        props.setProperty("database.include.list", "LIP_DEV");
        props.setProperty("inconsistent.schema.handling.mode", "warn");
        props.setProperty("topic.prefix", "oracle");

        // Create the engine with this configuration ...
        try (DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                .using(props)
                .notifying(record -> {
                    log.info("{}", record);
                })
                .using((success, message, error) -> {
                    if (error != null) {
                        log.error("{}/{}", error, message);
                    }
                })
                .build()
        ) {

            // Run the engine asynchronously ...
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(engine);

            // Do something else or wait for a signal or an event
        }
        // Engine is stopped when the main code is finished

    }

}
