package com.bulain.sharding;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShardingApplication.class)
public class ShardingDemo {

	@Autowired
	private DataSource dataSource;

	@Test
	public void testSharding() throws SQLException {
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

	@Test
	public void testShardingYear() throws SQLException {

		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);

			long dt = LocalDateTime.now().minusYears(1).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
//			long dt = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
			long hu = new Date().getTime() % (365 * 24 * 3600);
			for (int i = 0; i < 10; i++) {
				long dn = new Date().getTime() % (365 * 24 * 3600);
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
			}
		}

	}

	@Test
	public void testShardingSearch() throws SQLException {

		String sql = "SELECT * from t_hu WHERE dn = ?";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "26716058");

			try (ResultSet rs = pstmt.executeQuery()) {
				int count = 0;
				while (rs.next()) {
					count ++;
				}
				log.info("count: {}", count);
			}
		}

        sql = "SELECT * from t_hu WHERE pgi > ? and pgi < ?";
		//sql = "SELECT * from t_hu WHERE pgi between ? and ?";
		long prev = LocalDateTime.now().minusYears(2).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
		long curr = LocalDateTime.now().plusYears(2).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setTimestamp(1, new Timestamp(prev));
			ps.setTimestamp(2, new Timestamp(curr));

			try (ResultSet rs = ps.executeQuery()) {
				int count = 0;
				while (rs.next()) {
					count ++;
				}
				log.info("count: {}", count);
			}
		}

	}

}
