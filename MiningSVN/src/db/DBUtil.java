/**
 * 
 */
package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Saimir Bala
 *
 */
public class DBUtil {

	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				/*log or print or ignore*/
			}
		}
	}

	public static void close(java.sql.Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				/*log or print or ignore*/
			}
		}
	}

	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				/*log or print or ignore*/
			}
		}
	}
}