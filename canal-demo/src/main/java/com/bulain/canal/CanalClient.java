//package com.bulain.canal;
//
//import java.net.InetSocketAddress;
//import java.util.List;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.stereotype.Component;
//
//import com.alibaba.otter.canal.client.CanalConnector;
//import com.alibaba.otter.canal.client.CanalConnectors;
//import com.alibaba.otter.canal.protocol.CanalEntry.Column;
//import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
//import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
//import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
//import com.alibaba.otter.canal.protocol.CanalEntry.Header;
//import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
//import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
//import com.alibaba.otter.canal.protocol.Message;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//public class CanalClient implements InitializingBean {
//
//    private final static int BATCH_SIZE = 1000;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("127.0.0.1", 11111),
//                "example", "", "");
//        try {
//            connector.connect();
//            connector.subscribe(".*\\..*");
//            connector.rollback();
//            while (true) {
//                Message message = connector.getWithoutAck(BATCH_SIZE);
//                long batchId = message.getId();
//                int size = message.getEntries().size();
//                if (batchId == -1 || size == 0) {
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        log.error("afterPropertiesSet()", e);
//                    }
//                } else {
//                    printEntry(message.getEntries());
//                }
//                connector.ack(batchId);
//            }
//        } catch (Exception e) {
//            log.error("afterPropertiesSet()", e);
//        } finally {
//            connector.disconnect();
//        }
//    }
//
//    private static void printEntry(List<Entry> entrys) {
//        for (Entry entry : entrys) {
//            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN
//                    || entry.getEntryType() == EntryType.TRANSACTIONEND) {
//                continue;
//            }
//            RowChange rowChage;
//            try {
//                rowChage = RowChange.parseFrom(entry.getStoreValue());
//            } catch (Exception e) {
//                throw new RuntimeException("ERROR ## parser of event, data:" + entry.toString(),
//                        e);
//            }
//            EventType eventType = rowChage.getEventType();
//            Header header = entry.getHeader();
//            log.info("=======>;binlog[{}:{}] , name[{},{}] , eventType : {}",
//                    header.getLogfileName(), header.getLogfileOffset(),
//                    header.getSchemaName(), header.getTableName(), eventType);
//            if (rowChage.getIsDdl()) {
//                log.info("=======>;isDdl: true,sql:" + rowChage.getSql());
//            }
//            for (RowData rowData : rowChage.getRowDatasList()) {
//                if (eventType == EventType.DELETE) {
//                    printColumn(rowData.getBeforeColumnsList());
//                } else if (eventType == EventType.INSERT) {
//                    printColumn(rowData.getAfterColumnsList());
//                } else {
//                    log.info("------->; before");
//                    printColumn(rowData.getBeforeColumnsList());
//                    log.info("------->; after");
//                    printColumn(rowData.getAfterColumnsList());
//                }
//            }
//        }
//    }
//
//    private static void printColumn(List<Column> columns) {
//        for (Column column : columns) {
//            log.info(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
//        }
//    }
//}