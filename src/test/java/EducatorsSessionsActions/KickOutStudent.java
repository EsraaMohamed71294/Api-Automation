package EducatorsSessionsActions;

import AdminArea.CreateEducationalResource;
import AdminArea.CreateEducator;
import AdminArea.GetSession;
import EducatorProfile.Educator_TestData;
import StudentHomeScreen.EnrollStudentIntoClass;
import StudentHomeScreen.StudentWallet;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.parsing.Parser;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class KickOutStudent {
    TestBase test = new TestBase();
    EnrollStudentIntoClass student = new EnrollStudentIntoClass();
    StudentWallet wallet = new StudentWallet();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    String educator_Email;
    Long educatorID;
    Response Kick_OUt;
    String EducatorRefreshToken;
    String OTP;
    Long Class_ID;
    Long session_id;
    Long student_Id;
    String student_refreshToken;

    @Given("student join started session")
    public void student_join_session () throws SQLException, InterruptedException {
        student. create_student_and_class ();
        student.Enroll_Student_Into_Class();
        Class_ID = student.Class_ID;
        educatorID = student.Educator_Id;
        session_id = student.Session_Id;
        student_Id = student.student_Id;
        student_refreshToken = student.student_refreshToken;

//            RestAssured.defaultParser = Parser.JSON;
//            wallet.NagwaClasses_Checkout(student_Id);
//            wallet.test_call_back();

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

        Response Start_Session = test.sendRequest("POST", "/educators/"+ educatorID +"/sessions/"+ session_id+ "/start", null,EducatorRefreshToken);
        Start_Session.prettyPrint();
        Response joinSession =  test.sendRequest("POST", "/students/"+ student_Id +"/classes/"+ Class_ID +"/sessions/"+ session_id +"/join",null,student_refreshToken);
        joinSession.prettyPrint();

    }

    @When("Performing the Api of Kick Out Student From Session")
    public void  Kick_Out() throws SQLException {
        pathParams.put("educator_id",educatorID);
        pathParams.put("session_id",session_id);
        pathParams.put("student_id",student_Id);

        Kick_OUt = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/kickout/{student_id}",null,EducatorRefreshToken);

    }

    @Then("I verify the appearance of status code 200 and student kicked out successfully")
    public void Validate_Response_of_end_session_successfully() {
        Kick_OUt.prettyPrint();
        Kick_OUt.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorsSessionsActions/KickOutStudent.json")))
                .body("session_id",equalTo(session_id),"message",hasToString("Student successfully kicked out from the session."),
                        "student_id",equalTo(student_Id));
    }

}
