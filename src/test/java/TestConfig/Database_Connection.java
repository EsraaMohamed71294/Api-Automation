package TestConfig;

import java.sql.*;


import java.sql.DriverManager;
import java.sql.SQLException;

public class Database_Connection {
	public  String host ;
	public  String user ;
	public  String password ;
	public ResultSet resultSet;
	public Connection connection;
	public Statement statement;
	public ResultSet connect_to_database (String query) {
		DatabaseEnvironment("DEMO_NagwaClasses");
		try {
			connection = DriverManager.getConnection(host, user, password);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	public ResultSet Connect_to_OTP_Database  (String query){
		DatabaseEnvironment("DEMO_OTP");
		try {
			connection = DriverManager.getConnection(host, user, password);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	public void DatabaseEnvironment (String environment){
		switch (environment) {
			case "BETA_NagwaClasses":
				host = "jdbc:postgresql://nagwa-classes-beta.cluster-c4iigfolsbo7.us-east-1.rds.amazonaws.com:5432/nagwa_classes";
				user = "testing_readwrite";
				password = "8yZ%`6!e?~0q6<MM?hHO";
				break;
			case "DEMO_NagwaClasses":
				host = "jdbc:postgresql://nagwa-classes-beta.cluster-c4iigfolsbo7.us-east-1.rds.amazonaws.com:5432/demo_nagwa_classes";
				user = "testing_readwrite";
				password = "8yZ%`6!e?~0q6<MM?hHO";
				break;
			case "LIVE_NagwaClasses":
				host = "jdbc:postgresql://nagwa-classes-prod.cluster-c4iigfolsbo7.us-east-1.rds.amazonaws.com:5432/nagwa_classes";
				user = "testing_readwrite";
				password = "8yZ%`6!e?~0q6<MM?hHO";
				break;
			case "BETA_OTP":
			case "DEMO_OTP":
				host = "jdbc:postgresql://beta-1.cluster-cmmuo3lde4yu.us-east-1.rds.amazonaws.com:5432/mmOtpDataBase";
				user = "qc_testing";
				password = "UsB7cKKz6RbBD$(T";
				break;
			case "LIVE_OTP":
				host = "jdbc:postgresql://live-nagwa-otp.cluster-ro-cmmuo3lde4yu.us-east-1.rds.amazonaws.com:5432/live-mmOtpDataBase";
				user = "postgres";
				password = "gufQl5nCa94kC6wqBEPomP";
				break;
			default:
				throw new IllegalArgumentException("Invalid environment: " + environment);
		}
	}

}




