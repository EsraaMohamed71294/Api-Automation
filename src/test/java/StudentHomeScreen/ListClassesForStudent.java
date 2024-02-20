package StudentHomeScreen;

import AdminArea.GetSession;
import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.And;
import io.restassured.response.Response;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ListClassesForStudent {
    TestBase test = new TestBase();
    GetSession session =new GetSession();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    Long SessionID;
    String educator_Email;
    Long educatorID;
    Response List_Educator_Classes;
    String EducatorRefreshToken;
    String OTP;
    Long Class_ID;
    Long class_id;
    Long subject_id;
    Long grade_id;
    Long session_id;
    Integer session_duration_in_minutes;
    String class_title;
    String session_title;

    @And("Get Student Classes and upcoming Session from database")
    public void get_Student_classes_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("");
        while (resultSet.next()) {
            class_id = resultSet.getLong("class_id");
            class_title = resultSet.getString("class_title");
            subject_id = resultSet.getLong("subject_id");
            grade_id = resultSet.getLong("grade_id");
            session_id = resultSet.getLong("session_id");
            session_title = resultSet.getString("session_title");
            session_duration_in_minutes = resultSet.getInt("session_duration_in_minutes");
        }
    }
    }
