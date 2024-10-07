package com.bulain.flink;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cdc.connectors.mysql.source.MySqlSource;
import org.apache.flink.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkDataSender {

    public static void main(String[] args) throws Exception {

        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
                .hostname("127.0.0.1")
                .port(3307)
                .serverTimeZone("GMT+8")
                .scanNewlyAddedTableEnabled(true)
                .databaseList("dbzm_dev")
                .tableList("dbzm_dev.demo_blog")
                .username("repl")
                .password("repl")
                .serverId("5")
                .deserializer(new JsonDebeziumDeserializationSchema())
                .build();

        Configuration config = new Configuration();
        config.setString("rest.port", "8081");
        config.setString("execution.checkpointing.interval","3000");
        config.setString("state.checkpoints.dir","file:/tmp/checkpoints");
        config.setString("state.backend.type","rocksdb");
        config.setString("state.savepoints.dir","file:/tmp/savepoints");
        config.setString("restart-strategy.type","fixed-delay");
        config.setString("restart-strategy.fixed-delay.attempts","10");
        config.setString("restart-strategy.fixed-delay.delay","1s");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);
        env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL Source")
                .setParallelism(2)
                .print()
                .setParallelism(1);

        env.execute("Print MySQL Snapshot + Binlog");
    }

}
