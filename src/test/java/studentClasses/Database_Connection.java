package studentClasses;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database_Connection  {

	TestBase Test =new TestBase();
	TestData data = new TestData();
	    public String Database_Connection (String SQL_Query) {
		try {
			Connection connect = DriverManager.getConnection(data.host, data.user, data.password);
			PreparedStatement pst = connect.prepareStatement(SQL_Query);
			ResultSet rs = pst.executeQuery();
			rs.next() ;
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
	}
	    }
