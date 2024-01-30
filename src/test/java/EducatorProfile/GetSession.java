package EducatorProfile;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import studentClasses.Database_Connection;
import studentClasses.TestBase;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class GetSession {
    TestBase test = new TestBase();
    CreateClass Class = new CreateClass();
    CreateSession session =new CreateSession();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    Long SessionID;
    String session_title;
    String session_start_date;
    Long Classid;
    Long educatorID;
    String session_end_date;
    Integer session_duration_in_minutes;
    Long educator_id;
    Long class_id;
    Long subject_id;

    Response Get_Session;

    @When("Performing the Api of Get session")
    public void Get_Session() {
        Get_Session = test.sendRequest("GET", "/admin/sessions/{session_id}", null,data.Admin_Token);
    }
    @Given("User Send valid session Id to get session data")
    public void user_send_valid_sessionID() throws SQLException {
        session.Create_Session();
        SessionID = session.sessionId;
        Classid =session.Class_ID;
        educatorID = session.EducatorId;
        System.out.println(SessionID);
        pathParams.put("session_id", SessionID);
    }

    @And("Getting data of created session from database")
    public void getSessionDetails () throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from sessions s \n" +
                "join classes_educators ce \n" +
                "on ce.educator_id = s.educator_id \n" +
                "join classes_subjects cs \n" +
                "on ce.class_id = cs.class_id \n" +
                "where s.session_id ="+ SessionID +"  and s.educator_id = "+ educatorID +" and ce.class_id = " + Classid+ "");

        while (resultSet.next()) {
            session_title = resultSet.getString("session_title");
            session_start_date = resultSet.getString("session_start_date").replace(" ","T")+ "Z";
            session_end_date = resultSet.getString("session_end_date").replace(" ","T")+ "Z";
            session_duration_in_minutes = resultSet.getInt("session_duration_in_minutes");
            educator_id = resultSet.getLong("educator_id");
            class_id = resultSet.getLong("class_id");
            subject_id = resultSet.getLong("subject_id");
        }
    }
    @Then("I verify the appearance of status code 200 and session data returned successfully")
    public void Validate_Response_of_Get_Session() {
        Get_Session.prettyPrint();
        Get_Session.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorProfileSchemas/GetSession.json")))
                .body("session_id", equalTo(SessionID),"session_title",hasToString(session_title),"session_start_date",hasToString(session_start_date),
                        "session_end_date",hasToString(session_end_date), "session_duration_in_minutes",equalTo(session_duration_in_minutes),"educator_id",equalTo(educator_id),
                        "classes_subjects.class_id",hasItem(class_id), "classes_subjects.subject_id",hasItem(subject_id));
    }
}
