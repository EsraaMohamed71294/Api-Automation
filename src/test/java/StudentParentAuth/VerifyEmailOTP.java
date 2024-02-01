package StudentParentAuth;

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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class VerifyEmailOTP {
    Educator_TestData data = new Educator_TestData();
    TestBase test = new TestBase();
    Database_Connection Connect = new Database_Connection();

    String OTP;
    String Email;
    String Access_token;
    public String studentFirstName ;
    public String studentLastName ;
    public String studentEmail;
    Response Verify_Student_OTP;
    public void get_Student_OTP_from_database() throws SQLException {
        ResultSet resultSet = Connect.Connect_to_OTP_Database("select \"Email\" ,\"Otp\"  from \"UserMailOtp\" umo where \"Email\" = 'test.automation@nagwa.com'");

        while (resultSet.next()) {
            Email = resultSet.getString("Email");
            OTP = resultSet.getString("Otp");
            System.out.println("Email: " + Email + "OTP: " + OTP );
        }
    }

    public void get_Student_data_from_database() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select  educator_first_name, educator_last_name, educator_email from public.educators where educator_id =343256786543");

        while (resultSet.next()) {
            studentFirstName = resultSet.getString("student_first_name");
            studentLastName = resultSet.getString("student_last_name");
            studentEmail = resultSet.getString("student_email");
            System.out.println("student_first_name: " + studentFirstName + " student_last_name: " + studentLastName + " student_email: " + studentEmail);
        }
    }

    public String getEmailForStudent() {
        return Email;
    }
    public String getOTPForStudent() {
        return OTP;
    }

    @And("Get Student OTP and mail from database")
    public void getting_educator_otp_email_from_db() throws SQLException {
        get_Student_OTP_from_database();
        get_Student_data_from_database();
        Email = getEmailForStudent();
        OTP =getOTPForStudent();
    }
    @When("Performing the Api of Verify Student OTP with valid data")
    public String Verify_Educator_OTP() throws SQLException {
        String Valid_body_request = "{\"email\":\""+ Email +"\",\"otp\":\"" + OTP + "\"}";
        Verify_Student_OTP = test.sendRequest("POST", "/auth/verify-otp", Valid_body_request,data.Admin_Token);
        return Access_token = Verify_Student_OTP.then().extract().path("tokens.access_token");
    }

    @Then("I verify the appearance of status code 200 and student authenticated")
    public void Validate_Response_of_verify_Student_OTP() {
        Verify_Student_OTP.prettyPrint();
        Verify_Student_OTP.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/Api_Automation/src/test/resources/Schemas/StudentParentAuthSchemas/VerifyStudentOTP.json")))
                .body("message", hasToString("Existing user authenticated."),"message_id",equalTo(2001),
                        "data.email",hasToString(Email),"data.first_name",hasToString(studentFirstName),"data.last_name",hasToString(studentLastName),
                        "data.role",hasToString("student"));
    }

    @Given("Performing the Api of Verify Student OTP with Invalid OTP")
    public void Verify_Student_OTP_with_Invalid_OTP() {
        String Invalid_OTP = "{\"email\":\""+ Email +"\",\"otp\":\"123456\"}";
        Verify_Student_OTP = test.sendRequest("POST", "/auth/verify-otp", Invalid_OTP,data.Admin_Token);
    }

    @Then("I verify the appearance of status code 401 and Invalid student OTP")
    public void Validate_Response_Invalid_Student_OTP() {
        Response Invalid_OTP = Verify_Student_OTP;
        test.Validate_Error_Messages(Invalid_OTP,HttpStatus.SC_UNAUTHORIZED,"Invalid OTP.",4014);
    }

}
