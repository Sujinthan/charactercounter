import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;

public class DB {
	public static Connection getConnection() throws ClassNotFoundException,
			SQLException {
		try {
			String driver = "com.mysql.jdbc.Driver";
			Class.forName("com.mysql.jdbc.Driver");
			String connURL = "jdbc:mysql://localhost/SUBCOMP";
			String username = "root";
			String password = "waterloo92";

			/*
			 * Represents a physical connection with the database
			 */
			return DriverManager.getConnection(connURL, username, password);
		} catch (SQLException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		}
	}

	public static void closeConnection(Connection conn) throws SQLException {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			throw e;
		}
	}
}
