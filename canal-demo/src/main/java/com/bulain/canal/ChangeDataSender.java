package com.bulain.canal;

import io.debezium.embedded.EmbeddedEngine;
import io.debezium.engine.DebeziumEngine;
import io.debezium.relational.history.MemorySchemaHistory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.source.SourceRecord;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Clock;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ChangeDataSender implements Runnable {
    private final JsonConverter valueConverter;
    private final Properties properties;
    private DebeziumEngine<SourceRecord> engine;

    public ChangeDataSender() {
        properties = new Properties();
        properties.put("connector.class", "io.debezium.connector.mysql.MySqlConnector");
        properties.put("name", "canal");
        properties.put("topic.prefix", "canal");
        properties.put("snapshot.mode", "initial");
        properties.put("database.server.id", "3");
        properties.put("database.hostname", "127.0.0.1");
        properties.put("database.port", "3307");
        properties.put("database.user", "repl");
        properties.put("database.password", "repl");
        properties.put("database.Database", "lip_dev");
        properties.put("offset.storage", "org.apache.kafka.connect.storage.MemoryOffsetBackingStore");
        properties.put("schema.history.internal", MemorySchemaHistory.class.getName());
        properties.put("inconsistent.schema.handling.mode", "ignore");
        properties.put("schemas.enable", false);

        valueConverter = new JsonConverter();
        valueConverter.configure(new HashMap<>(), false);

    }

    @Override
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

        cleanUp();

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

    private void cleanUp() {

    }

    private void sendRecord(SourceRecord record) {

        log.info("{}", record);

        // We are interested only in data events not schema change events
        if (record.topic().equals("canal")) {
            return;
        }

        Schema schema = null;
        if (null == record.keySchema()) {
            log.error("The keySchema is missing. Something is wrong.");
            return;
        }

        // For deletes, the value node is null
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
        log.info("{}", message);

        String partitionKey = String.valueOf(record.key() != null ? record.key().hashCode() : -1);
        final byte[] payload = valueConverter.fromConnectData("canal", schema, message);

        log.info("{}/{}/{}", record.topic(), partitionKey, ByteBuffer.wrap(payload));

    }

    public static void main(String[] args) {
        new ChangeDataSender().run();
    }

}