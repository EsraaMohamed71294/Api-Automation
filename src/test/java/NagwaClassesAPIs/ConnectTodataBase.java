package NagwaClassesAPIs;

import org.junit.jupiter.api.Test;

import java.sql.*;

public class ConnectTodataBase {

    private final String URL ="jdbc:postgresql://beta-1.cluster-cmmuo3lde4yu.us-east-1.rds.amazonaws.com:5432/mmOtpDataBase";
    private final String user = "qc_testing";
    private  final String password = "UsB7cKKz6RbBD$(T";
    private  final String fname = "testing_team";
    @Test
    public void connect(){

        try {

            Connection conn = DriverManager.getConnection(URL, user ,password);

            {                if (conn != null && !conn.isClosed()) {

                Statement myStmt = conn.createStatement();
                ResultSet myRs = myStmt.executeQuery("select * from public.\"UserOtp\" uo where \"UserId\" = 782146025154 ");
                {
                    while (myRs.next()) {

                        String  Otp = myRs.getString("Otp");
                        System.out.println("The value from the table is : "+Otp);
                    }
                }

            } else
                System.out.println("Failed to connect");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
