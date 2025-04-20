package com.bulain.sharding;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShardingService {

    private final DataSource dataSource;

    @Value("${spring.datasource.master.driver-class-name}")
    private String driverClassName;

    @SneakyThrows
    @ShardingDs
    public void sharding() {
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);

        for (int i = 1; i < 10; i++) {
            PreparedStatement ps1 = conn.prepareStatement("insert into t_order(user_id, status) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps1.setInt(1, i % 10);
            ps1.setString(2, "Y");
            ps1.executeUpdate();
            ResultSet generatedKeys = ps1.getGeneratedKeys();
            generatedKeys.next();
            long orderId = generatedKeys.getLong(0);

            for (int j = 1; j < 9; j++) {
                PreparedStatement ps2 = conn.prepareStatement("insert into t_order_item(order_id, user_id) values (?, ?)");
                ps2.setLong(1, orderId);
                ps2.setInt(2, i % 10);
                ps2.executeUpdate();
            }
            conn.commit();
        }

        conn.close();

    }

    @SneakyThrows
    @ShardingDs
    public void shardingYear() {

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            for (int year = 2021; year < 2026; year++) {
                long dt = LocalDateTime.now().with(Year.of(year)).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
                long hu = dt / (1000);
                long dn = dt / (60 * 1000);
                for (int i = 0; i < 10; i++) {
                    String dnsql = "insert into t_dn (dn, pgi) values (?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(dnsql)) {
                        ps.setString(1, Long.toString(dn));
                        ps.setTimestamp(2, new Timestamp(dt));
                        ps.executeUpdate();
                    }
                    for (int j = 0; j < 5; j++) {
                        String husql = "insert into t_hu (hu, dn, pgi) values (?, ?, ?)";
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
        long prev = LocalDateTime.now().minusYears(2).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        long curr = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
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
        long prev = LocalDateTime.now().minusYears(2).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        long curr = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
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
        long prev = LocalDateTime.now().minusYears(2).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        long curr = LocalDateTime.now().minusYears(1).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();

        try (HintManager hint = HintManager.getInstance(); Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(prev));
            ps.setTimestamp(2, new Timestamp(curr));

            hint.addTableShardingValue("t_hint", new Date(prev));
            hint.addTableShardingValue("t_hint", new Date(curr));
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
