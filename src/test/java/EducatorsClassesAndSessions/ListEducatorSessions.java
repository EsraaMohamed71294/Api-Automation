package EducatorsClassesAndSessions;

import AdminArea.GetSession;
import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class ListEducatorSessions {
    TestBase test = new TestBase();
    GetSession session =new GetSession();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    Long SessionID;
    String educator_Email;
    Long educatorID;
    Response List_Educator_Sessions;
    String EducatorRefreshToken;
    String OTP;
    Long Class_ID;
    Long class_id;
    Integer class_block_number;
    Long grade_id;
    Long session_id;
    Integer session_duration_in_minutes;
    String class_title;
    String session_title;

    @And("Get Educator Sessions from database")
    public void get_Educator_classes_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select *  from sessions s \n" +
                "join classes_subjects_sessions css \n" +
                "on s.session_id = css.session_id \n" +
                "join classes_subjects cs \n" +
                "on cs.class_subject_id = css.class_subject_id \n" +
                "join classes c \n" +
                "on c.class_id = cs.class_id \n" +
                "where cs.class_id = "+ Class_ID +" ");


        while (resultSet.next()) {
            class_id = resultSet.getLong("class_id");
            session_id = resultSet.getLong("session_id");
            class_title = resultSet.getString("class_title");
            class_block_number = resultSet.getInt("class_block_number");
            session_id = resultSet.getLong("session_id");
            session_title = resultSet.getString("session_title");
            session_duration_in_minutes = resultSet.getInt("session_duration_in_minutes");
        }

    }

    @Given("User Create Classes and Session for Educator to list Sessions for educator")
    public void Create_Session_for_educator ()throws SQLException {
        session.user_send_valid_sessionID();
        session.Get_Session();
        educatorID = session.educatorID;

    }

    @When("Performing the Api of list sessions for educator")
    public void List_Educator_sessions() throws SQLException {
        SessionID = session.SessionID;
        Class_ID = session.ClassID;

        ResultSet GetEducatorEmail = Connect.connect_to_database("select educator_email from public.educators e where educator_id ="+ educatorID +"");
        while (GetEducatorEmail.next()) {
            educator_Email = GetEducatorEmail.getString("educator_email");};

        Response testOTP = test.sendRequest("POST", "/educators/auth/send-otp", "{\"email\":\""+ educator_Email +"\",\"language\":\"en\"}",data.Admin_Token);
        testOTP.prettyPrint();

        ResultSet GetEducatorOTP = Connect.Connect_to_OTP_Database("select \"Email\" ,\"Otp\"  from \"UserMailOtp\" umo where \"Email\" = '"+ educator_Email +"'");
        while (GetEducatorOTP.next()) {
            OTP = GetEducatorOTP.getString("Otp");};

        Response VerifyOTP = test.sendRequest("POST", "/educators/auth/verify-otp", "{\"email\":\""+ educator_Email +"\",\"otp\":\""+ OTP +"\"}",data.Admin_Token);
        VerifyOTP.prettyPrint();
        EducatorRefreshToken = VerifyOTP.then().extract().path("tokens.refresh_token");

        pathParams.put("educator_id", educatorID);
        pathParams.put("class_id",Class_ID);
        List_Educator_Sessions = test.sendRequest("GET", "/educators/{educator_id}/classes/{class_id}/sessions", null,EducatorRefreshToken);
    }

    @Then("I verify the appearance of status code 200 and Educator Sessions data returned")
    public void Validate_Response_of_Educator_sessions_returned_successfully() {
        List_Educator_Sessions.prettyPrint();
        List_Educator_Sessions.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorClassesAndSession/ListEducatorSessions.json")))
                .body("class_id",equalTo(class_id),"class_title",hasToString(class_title),"sessions.class_block_number",hasItem(equalTo(class_block_number)),
                        "sessions.session_id",hasItem(equalTo(session_id)),"sessions.session_title",hasItem(hasToString(session_title)),
                        "sessions.session_duration_in_minutes",hasItem(equalTo(session_duration_in_minutes)));

    }



}
