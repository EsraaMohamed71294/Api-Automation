package EducatorsClassesAndSessions;

import AdminArea.CreateEducationalResource;
import AdminArea.CreateEducator;
import AdminArea.GetClass;
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
import static org.hamcrest.Matchers.equalTo;

public class GetEducatorSessions {
    TestBase test = new TestBase();
    GetSession session =new GetSession();
    GetClass classData =new GetClass();
    CreateEducator educator = new CreateEducator();
    CreateEducationalResource resource = new CreateEducationalResource();
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
    Long sessionId;
    Long session_id;
    Integer class_block_number;
    Integer session_duration_in_minutes;
    String class_title;
    String session_title;
    Integer educational_resource_type_id;
    String educational_resource_type;
    Long educational_resource_id;
    String educational_resource_name;
    Long educator_id;
    Long ResourceId;

    @Given("User Create Classes and Session for Educator to Get Sessions for educator")
    public void Create_Session_for_educator ()throws SQLException {
        session.user_send_valid_sessionID();
        session.Get_Session();
        educatorID = session.educatorID;
        session_id = session.SessionID;
        Class_ID = session.ClassID;
        System.out.println("session_id" + session_id);
        resource.Create_new_educational_resources();
        ResourceId = resource.resourceId;
        System.out.println("ResourceId "+ResourceId);
        String valid_body = "{\"sessions_ids\":["+ session_id +"],\"educational_resource_id\":"+ ResourceId +"}";
        test.sendRequest("POST", "/admin/assign-educational-resource", valid_body, data.Admin_Token);
    }

    @When("Performing the Api of Get sessions for educator")
    public void Get_Educator_sessions() throws SQLException {

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
        pathParams.put("session_id",session_id);

        List_Educator_Sessions = test.sendRequest("GET", "/educators/{educator_id}/classes/{class_id}/sessions/{session_id}", null,EducatorRefreshToken);
    }

    @And("Get Educator's Sessions from database")
    public void get_Educator_sessions_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select *  from sessions s \n" +
                "join classes_subjects_sessions css \n" +
                "on s.session_id = css.session_id \n" +
                "join classes_subjects cs \n" +
                "on cs.class_subject_id = css.class_subject_id \n" +
                "join classes c \n" +
                "on c.class_id = cs.class_id \n" +
                "join sessions_educational_resources ser \n" +
                "on ser.session_id = s.session_id \n" +
                "join educational_resources er \n" +
                "on er.educational_resource_id = ser.educational_resource_id \n" +
                "join educational_resources_types ert \n" +
                "on ert.educational_resource_type_id = er.educational_resource_type_id \n" +
                "where s.session_id = " + session_id + "");


        while (resultSet.next()) {
            class_id = resultSet.getLong("class_id");
            class_title = resultSet.getString("class_title");
            educator_id = resultSet.getLong("educator_id");
            sessionId = resultSet.getLong("session_id");
            session_title = resultSet.getString("session_title");
            session_duration_in_minutes = resultSet.getInt("session_duration_in_minutes");
            educational_resource_type_id = resultSet.getInt("educational_resource_type_id");
            educational_resource_type = resultSet.getString("educational_resource_type");
            educational_resource_id = resultSet.getLong("educational_resource_id");
            educational_resource_name = resultSet.getString("educational_resources_name");

        }
    }

    @Then("I verify the appearance of status code 200 and Educator Sessions data returned successfully")
    public void Validate_Response_of_Educator_sessions_returned_successfully() {
        List_Educator_Sessions.prettyPrint();
        List_Educator_Sessions.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorClassesAndSession/GetEducatorSessions.json")))
                .body("class_id",equalTo(class_id),"class_title",hasToString(class_title),"session_id",equalTo(sessionId),"educator_id",equalTo(educator_id)
                ,"session_title",hasToString(session_title),"session_duration_in_minutes",equalTo(session_duration_in_minutes),"educational_resources.educational_resource_type_id"
                        ,hasItem(equalTo(educational_resource_type_id)),"educational_resources.educational_resource_type",hasItem(hasToString(educational_resource_type)),
                        "educational_resources.educational_resources.educational_resource_id",hasItems(hasItem(equalTo(educational_resource_id))),
                        "educational_resources.educational_resources.educational_resource_name",hasItems(hasItem(hasToString(educational_resource_name))));
    }



    }

