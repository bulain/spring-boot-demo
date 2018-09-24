package com.bulain.sharding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingApplication.class)
public class ShardingDemo {

	@Autowired
	private DataSource dataSource;

	@Test
	public void testSharding() throws SQLException {
		Connection conn = dataSource.getConnection();
		conn.setAutoCommit(false);

		for (int i = 1; i < 100; i++) {
			PreparedStatement ps1 = conn
					.prepareStatement("insert into t_order(order_id,user_id,status) values (?,?,?)");
			ps1.setLong(1, i);
			ps1.setInt(2, i % 10);
			ps1.setString(3, "Y");
			ps1.executeUpdate();

			for (int j = 1; j < 9; j++) {
				PreparedStatement ps2 = conn
						.prepareStatement("insert into t_order_item(order_item_id,order_id,user_id) values (?,?,?)");
				ps2.setLong(1, i * 10 + j);
				ps2.setLong(2, i);
				ps2.setInt(3, i % 10);
				ps2.executeUpdate();
			}
			conn.commit();
		}

		conn.close();

	}

}
