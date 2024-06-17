package com.bulain.canal;

import io.debezium.connector.mysql.MySqlConnector;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.engine.DebeziumEngine;
import io.debezium.storage.file.history.FileSchemaHistory;
import io.debezium.storage.jdbc.history.JdbcSchemaHistory;
import io.debezium.storage.jdbc.offset.JdbcOffsetBackingStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.storage.FileOffsetBackingStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Clock;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ChangeDataSender implements InitializingBean {
    private final JsonConverter valueConverter;
    private final Properties properties;
    private DebeziumEngine<SourceRecord> engine;

    public ChangeDataSender() {

        properties = new Properties();
        properties.put("connector.class", MySqlConnector.class.getName());
        properties.put("name", "canal");
        properties.put("topic.prefix", "canal");
        properties.put("snapshot.mode", "initial");
        properties.put("inconsistent.schema.handling.mode", "warn");
        properties.put("database.server.id", "3");
        properties.put("database.hostname", "127.0.0.1");
        properties.put("database.port", "3307");
        properties.put("database.user", "repl");
        properties.put("database.password", "repl");
        properties.put("database.database", "lip_dev");
        properties.put("database.connectionTimeZone", "PRC");
        properties.put("table.exclude.list", Arrays.asList("debezium_database_history,debezium_offset_storage"));

        properties.put("offset.storage", JdbcOffsetBackingStore.class.getName());
        properties.put("offset.storage.jdbc.url", "jdbc:mysql://127.0.0.1:3307/mybatis_dev?useSSL=false&autoReconnect=true&serverTimezone=PRC&useUnicode=true&characterEncoding=utf-8&nullCatalogMeansCurrent=true&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true");
        properties.put("offset.storage.jdbc.user", "mybatis");
        properties.put("offset.storage.jdbc.password", "mybatis");
        properties.put("offset.storage.jdbc.waitRetryDelay", "60s");

        properties.put("schema.history.internal", JdbcSchemaHistory.class.getName());
        properties.put("schema.history.internal.jdbc.url", "jdbc:mysql://127.0.0.1:3307/mybatis_dev?useSSL=false&autoReconnect=true&serverTimezone=PRC&useUnicode=true&characterEncoding=utf-8&nullCatalogMeansCurrent=true&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true");
        properties.put("schema.history.internal.jdbc.user", "mybatis");
        properties.put("schema.history.internal.jdbc.password", "mybatis");
        properties.put("schema.history.internal.jdbc.waitRetryDelay", "60s");
        properties.put("schema.history.internal.jdbc.schema.history.table.ddl", "create table debezium_database_history (id varchar ( 36 ) not null,history_data longtext,history_data_seq integer,record_insert_ts datetime not null,record_insert_seq integer not null,primary key (id))");

        HashMap<String, Object> configs = new HashMap<>();
        configs.put("schemas.enable", false);
        valueConverter = new JsonConverter();
        valueConverter.configure(configs, false);

    }

    @Override
    public void afterPropertiesSet() {
        this.run();
    }

    public void run() {

        DebeziumEngine.Builder<SourceRecord> builder = new EmbeddedEngine.EngineBuilder();
        engine = builder
                .using(properties)
                .using(this.getClass().getClassLoader())
                .using(Clock.system(ZoneId.of("UTC+8")))
                .notifying(this::sendRecord)
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(engine);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Requesting embedded engine to shutdown");
            try {
                engine.close();
            } catch (IOException e) {
                log.error("shutdown()-", e);
            }
        }));

        // the submitted task keeps running, only no more new ones can be added
        executor.shutdown();

        awaitTermination(executor);

        log.info("Engine terminated");
    }

    private void awaitTermination(ExecutorService executor) {
        try {
            while (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                log.info("Waiting another 10 seconds for the embedded engine to complete");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sendRecord(SourceRecord record) {

        if ("canal".equals(record.topic())) {
            log.info("Engine initialized");
            return;
        }

        log.info("{}", record);

        Schema schema = null;
        if (null == record.keySchema()) {
            log.error("The keySchema is missing. Something is wrong.");
            return;
        }

        if (null != record.valueSchema()) {
            schema = SchemaBuilder.struct()
                    .field("key", record.keySchema())
                    .field("value", record.valueSchema())
                    .build();
        } else {
            schema = SchemaBuilder.struct()
                    .field("key", record.keySchema())
                    .build();
        }

        Struct message = new Struct(schema);
        message.put("key", record.key());
        if (null != record.value()) {
            message.put("value", record.value());
        }
        String partitionKey = String.valueOf(record.key() != null ? record.key().hashCode() : -1);
        final byte[] payload = valueConverter.fromConnectData("canal", schema, message);

        log.info("{}/{}/{}", record.topic(), partitionKey, ByteBuffer.wrap(payload));

    }

}