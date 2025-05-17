package com.bulain.sharding;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShardingService {

    public static final ZoneOffset OFFSET = ZoneOffset.ofHours(8);

    private final DataSource dataSource;

    @Value("${spring.datasource.master.driver-class-name}")
    private String driverClassName;

    @SneakyThrows
    @ShardingDs
    public void shardingInsert() {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            String hdel = "truncate table t_order";
            String ddel = "truncate table t_order_item";
            try (PreparedStatement pshdel = conn.prepareStatement(hdel);
                 PreparedStatement psddel = conn.prepareStatement(ddel)) {
                pshdel.executeUpdate();
                psddel.executeUpdate();
                conn.commit();
            }

            long hid = LocalDateTime.now().toInstant(OFFSET).toEpochMilli();
            long did = LocalDateTime.now().toInstant(OFFSET).toEpochMilli();
            for (int i = 0; i < 5; i++) {
                int userId = i % 10;
                long pgi = LocalDateTime.now().with(Year.of(2025)).minusYears(i % 3).toInstant(OFFSET).toEpochMilli();

                String hsql = "insert into t_order(order_id, pgi, user_id, status) values (?, ?, ?, ?)";
                String dsql = "insert into t_order_item(order_item_id, order_id, pgi, user_id) values (?, ?, ?, ?)";
                try (HintManager instance = HintManager.getInstance();
                     PreparedStatement ps1 = conn.prepareStatement(hsql)) {
                    instance.addTableShardingValue("t_order", new Date(pgi));
                    instance.addTableShardingValue("t_order_item", new Date(pgi));

                    int hidx = 1;
                    ps1.setLong(hidx++, hid);
                    ps1.setTimestamp(hidx++, new Timestamp(pgi));
                    ps1.setInt(hidx++, userId);
                    ps1.setString(hidx++, "Y");
                    ps1.executeUpdate();

                    for (int j = 0; j < 4; j++) {
                        int didx = 1;
                        try (PreparedStatement ps2 = conn.prepareStatement(dsql)) {
                            ps2.setLong(didx++, did++);
                            ps2.setLong(didx++, hid);
                            ps2.setTimestamp(didx++, new Timestamp(pgi));
                            ps2.setInt(didx++, userId);
                            ps2.executeUpdate();
                        }
                    }
                }

                hid++;
                conn.commit();
            }

        }
    }

    @SneakyThrows
    @ShardingDs
    public void shardingQuery() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT t.order_id, t.pgi, t.user_id, t.status, d.order_item_id from t_order t left join t_order_item d on d.order_id = t.order_id WHERE t.pgi > ? and t.pgi < ? order by d.order_item_id";
            long prev = LocalDateTime.now().with(Year.of(2024)).with(TemporalAdjusters.firstDayOfYear()).toInstant(OFFSET).toEpochMilli();
            long curr = LocalDateTime.now().with(Year.of(2025)).toInstant(OFFSET).toEpochMilli();
            try (HintManager instance = HintManager.getInstance();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                instance.addTableShardingValue("t_order", new Date(prev));
                instance.addTableShardingValue("t_order", new Date(curr));
                instance.addTableShardingValue("t_order_item", new Date(prev));
                instance.addTableShardingValue("t_order_item", new Date(curr));

                ps.setTimestamp(1, new Timestamp(prev));
                ps.setTimestamp(2, new Timestamp(curr));

                try (ResultSet rs = ps.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                    }
                    log.info("count: {}", count);
                }
            }
        }
    }

    @SneakyThrows
    @ShardingDs
    public void shardingYear() {

        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);
            String dndel = "truncate table t_dn";
            try (PreparedStatement psdel = conn.prepareStatement(dndel)) {
                psdel.executeUpdate();
                conn.commit();
            }

            String dnsql = "insert into t_dn (dn, pgi) values (?, ?)";
            String husql = "insert into t_hu (hu, dn, pgi) values (?, ?, ?)";

            for (int year = 2021; year < 2026; year++) {
                long dt = LocalDateTime.now().with(Year.of(year)).toInstant(OFFSET).toEpochMilli();
                long hu = dt / (1000);
                long dn = dt / (60 * 1000);
                for (int i = 0; i < 10; i++) {
                    try (PreparedStatement ps = conn.prepareStatement(dnsql)) {
                        ps.setString(1, Long.toString(dn));
                        ps.setTimestamp(2, new Timestamp(dt));
                        ps.executeUpdate();
                    }
                    for (int j = 0; j < 5; j++) {
                        try (PreparedStatement ps = conn.prepareStatement(husql)) {
                            ps.setString(1, Long.toString(hu));
                            ps.setString(2, Long.toString(dn));
                            ps.setTimestamp(3, new Timestamp(dt));
                            ps.executeUpdate();
                        }
                        hu++;
                    }
                    conn.commit();
                    dn++;
                    dt = dt + 60 * 1000;
                }
            }
        }

    }

    @SneakyThrows
    @ShardingDs
    public void shardingSearch() {

        String sql = "SELECT t.hu, n.dn, n.pgi from t_hu t left join t_dn n on n.dn = t.dn WHERE t.dn = ? order by t.hu";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "26981776");

            try (ResultSet rs = pstmt.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                }
                log.info("count: {}", count);
            }
        }

        sql = "SELECT t.hu, n.dn, n.pgi from t_hu t left join t_dn n on n.dn = t.dn WHERE t.pgi > ? and t.pgi < ? order by t.hu";
        long prev = LocalDateTime.now().minusYears(2).toInstant(OFFSET).toEpochMilli();
        long curr = LocalDateTime.now().toInstant(OFFSET).toEpochMilli();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(prev));
            ps.setTimestamp(2, new Timestamp(curr));

            try (ResultSet rs = ps.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                }
                log.info("count: {}", count);
            }
        }

    }


    @SneakyThrows
    @ShardingDs
    public void shardingPage() {

        String sql = "SELECT t.hu, n.dn, n.pgi from t_hu t left join t_dn n on n.dn = t.dn WHERE t.pgi between ? and ? order by t.hu ";
        sql = rowLimit(sql);
        long prev = LocalDateTime.now().minusYears(2).toInstant(OFFSET).toEpochMilli();
        long curr = LocalDateTime.now().toInstant(OFFSET).toEpochMilli();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(prev));
            ps.setTimestamp(2, new Timestamp(curr));

            try (ResultSet rs = ps.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                }
                log.info("count: {}", count);
            }
        }

    }

    private String rowLimit(String originalSql) {
        String limitSql;
        if ("org.postgresql.Driver".equals(driverClassName)
                || "com.kingbase8.Driver".equals(driverClassName)
                || "org.opengauss.Driver".equals(driverClassName)) {
            limitSql = originalSql + " LIMIT 10 OFFSET 10";
        } else if ("oracle.jdbc.OracleDriver".equals(driverClassName)
                || "dm.jdbc.driver.DmDriver".equals(driverClassName)) {
            limitSql = "SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM ( " + originalSql + " ) TMP WHERE ROWNUM <= 20) WHERE ROW_ID > 10";
        } else if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(driverClassName)) {
            limitSql = originalSql + " OFFSET 10 ROWS FETCH NEXT 10 ROWS ONLY";
        } else {
            limitSql = originalSql + " LIMIT 10, 10";
        }
        return limitSql;
    }

    @SneakyThrows
    @ShardingDs
    public void shardingHint() {

        String sql = "SELECT t.hu, n.dn, n.pgi from t_hint t left join t_dn n on n.dn = t.dn WHERE n.pgi > ? and n.pgi < ?  order by t.hu";
        long prev = LocalDateTime.now().minusYears(2).toInstant(OFFSET).toEpochMilli();
        long curr = LocalDateTime.now().minusYears(1).toInstant(OFFSET).toEpochMilli();

        try (HintManager hint = HintManager.getInstance(); Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            hint.addTableShardingValue("t_hint", new Date(prev));
            hint.addTableShardingValue("t_hint", new Date(curr));

            ps.setTimestamp(1, new Timestamp(prev));
            ps.setTimestamp(2, new Timestamp(curr));
            try (ResultSet rs = ps.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                }
                log.info("count: {}", count);
            }
        }

    }

}
