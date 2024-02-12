package EducatorsClassesAndSessions;

import AdminArea.GetSession;
import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
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

import static org.hamcrest.Matchers.hasToString;

public class ListEducatorClasses {
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
    @Given("User Create Classes and Session for Educator to list classes for educator")
    public void Create_Session_for_educator ()throws SQLException{
    session.user_send_valid_sessionID();
    session.Get_Session();
    educatorID = session.educatorID;
}

    @When("Performing the Api of list classes for educator")
    public void List_Educator_classes() throws SQLException {
        SessionID = session.SessionID;

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
            List_Educator_Classes = test.sendRequest("GET", "/educators/{educator_id}/classes", null,EducatorRefreshToken);
    }

    @Then("I verify the appearance of status code 200 and Educator classes data returned")
    public void Validate_Response_of_Educator_classes_returned_successfully() {
        List_Educator_Classes.prettyPrint();
        List_Educator_Classes.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorClassesAndSession/ListEducatorClasses.json")));
    }
}
