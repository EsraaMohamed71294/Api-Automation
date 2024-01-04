package studentClasses;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;


public class Database_Connection {
	
	
	    
	    @Test
	    public void connect() {

	        try {
				   DriverManager.getConnection(host, user, password);

	                   
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	        	
	    }
	    }
