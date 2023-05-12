package connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
	public ConnectionFactory() {};
	
	private static String getURL() {
		try (InputStream input = ConnectionFactory.class.getResourceAsStream("./db_connection.properties")) {
			if (input == null) {
				System.out.println("Unable to generate jdbc connection string, properties file not found");
				return null;
			}
			
			Properties props = new Properties();
			props.load(input);
			StringBuilder jdbcString = new StringBuilder();
			jdbcString.append("jdbc:mysql://");
			jdbcString.append(props.getProperty("db.username"));
			jdbcString.append(":" + props.getProperty("db.pwd") + "@");
			jdbcString.append(props.getProperty("db.host"));
			jdbcString.append(":");
			jdbcString.append(props.getProperty("db.port") != null ? props.getProperty("db.port") : "3306");
			jdbcString.append("/" + props.getProperty("db.dbname"));
			jdbcString.append("?useSSL=true&verifyServerCertificate=false&sendFractionalSecondsForTime=true");
			
			return jdbcString.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static Connection getConnection() {
		String jdbcURL = getURL();
		if (jdbcURL != null) {
			
			try {
				return DriverManager.getConnection(jdbcURL);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
