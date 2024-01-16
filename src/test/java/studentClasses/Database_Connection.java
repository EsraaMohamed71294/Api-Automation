package studentClasses;

import java.sql.*;


import java.sql.DriverManager;
import java.sql.SQLException;

public class Database_Connection {
	public final String host = "jdbc:postgresql://nagwa-classes-beta.cluster-c4iigfolsbo7.us-east-1.rds.amazonaws.com:5432/nagwa_classes";

	public final String user = "testing_readwrite";
	public final String password = "8yZ%`6!e?~0q6<MM?hHO";
	public ResultSet resultSet;
	public Connection connection;
	public Statement statement;

	public ResultSet connect_to_database (String query) {
		try {
			connection = DriverManager.getConnection(host, user, password);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
}
