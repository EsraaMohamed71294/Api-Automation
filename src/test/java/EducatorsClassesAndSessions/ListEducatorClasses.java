package EducatorsClassesAndSessions;

import AdminArea.CreateEducator;
import AdminArea.CreateSession;
import AdminArea.GetSession;
import EducatorProfile.Educator_TestData;
import EducatorProfile.VerifyEducator_OTP;
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

import static org.hamcrest.Matchers.hasToString;

public class ListEducatorClasses {
    TestBase test = new TestBase();
    GetSession session =new GetSession();
    CreateEducator educator = new CreateEducator();

    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    VerifyEducator_OTP educatorOTP = new VerifyEducator_OTP();
    Map<String, Object> pathParams = test.pathParams;
    Long SessionID;
    String educator_Email;
    Long educatorID;
    Response List_Educator_Classes;
    String EducatorRefreshToken;
    String OTP;
@And("first method")
public void Create_Session_for_educator ()throws SQLException{
    session.user_send_valid_sessionID();
    session.Get_Session();
    educatorID = session.educatorID;
}
    @Given("User Send valid educator Id to list classes for educator")
    public void Send_Valid_EducatorId_To_List_Educator_classes() throws SQLException {
        Map<String, Object> pathParams = test.pathParams;
//        session.user_send_valid_sessionID();
//        session.Get_Session();
//        educatorID = session.educatorID;

//        System.out.println(educatorID);
//        educatorOTP.getting_educator_otp_email_from_db();
//        educatorOTP.Verify_Educator_OTP();
//        educatorID =educator.Educator_ID;
//        System.out.println("educatorID" +educatorID);
        pathParams.put("educator_id", educatorID);
    }

    @When("Performing the Api of list classes for educator")
    public void List_Educator_classes() throws SQLException {
        SessionID = session.SessionID;
        educator_Email =educator.Email;
        System.out.println("SessionID is " + SessionID + "educator_Email "+educator_Email);

//        Response testOTP = test.sendRequest("POST", "/educators/auth/send-otp", "{\"email\":\""+ educator_Email +"\",\"language\":\"en\"}",data.Admin_Token);
//        testOTP.prettyPrint();

        ResultSet resultSet = Connect.Connect_to_OTP_Database("select \"Email\" ,\"Otp\"  from \"UserMailOtp\" umo where \"Email\" = '"+ educator_Email +"'");
        while (resultSet.next()) {
        OTP = resultSet.getString("Otp");};
        System.out.println(OTP);

       Response VerifyOTP = test.sendRequest("POST", "/educators/auth/verify-otp", "{\"email\":\""+ educator_Email +"\",\"otp\":\""+ OTP +"\"}",data.Admin_Token);
        VerifyOTP.prettyPrint();
        EducatorRefreshToken = VerifyOTP.then().extract().path("tokens.refresh_token");
        System.out.println("EducatorRefreshToken " +EducatorRefreshToken);
//        educatorOTP.getting_educator_otp_email_from_db();
//        educatorOTP.Verify_Educator_OTP();
//        EducatorRefreshToken = educatorOTP.Educator_Refresh_Token;

//                List_Educator_Classes = test.sendRequest("GET", "/educators/{educator_id}/classes", null,EducatorRefreshToken);
    }

    @Then("I verify the appearance of status code 200 and Educator classes data returned")
    public void Validate_Response_of_Educator_classes_returned_successfully() {
        List_Educator_Classes.prettyPrint();
        List_Educator_Classes.then()
                .statusCode(HttpStatus.SC_OK);
//                .assertThat()
//                .body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/Api_Automation/src/test/resources/Schemas/EducatorProfileSchemas/UpdateEducatorProfile.json")))
//                .body("message", hasToString("Profile updated successfully."));
    }
}
