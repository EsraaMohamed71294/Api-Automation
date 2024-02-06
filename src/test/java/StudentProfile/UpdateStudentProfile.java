package StudentProfile;

import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import com.github.javafaker.Faker;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class UpdateStudentProfile {
    TestBase test = new TestBase();
    GetStudent studentData = new GetStudent();
    CreateStudent student = new CreateStudent();
    Database_Connection Connect = new Database_Connection();
    Faker fakeDate =new Faker();
    String firstName = fakeDate.name().firstName();
    String lastName = fakeDate.name().lastName();

    Long StudentID;
    Map<String, Object> pathParams = test.pathParams;
    String refreshToken;

    Response Update_Student;

    @When("Performing the Api of Update Student Profile")
    public void Update_Student_Profile() throws SQLException {
        student.get_grade_from_database ();
        Long grade = student.Grade_ID;
        String valid_body = "{\"student_first_name\":\""+ firstName +"\",\"student_last_name\":\""+ lastName +"\",\"grade_id\":"+ grade +"}";
        student.Verify_Student_OTP_already_Auth();
        refreshToken = student.student_refresh_token;
        Update_Student = test.sendRequest("PATCH", "/students/{student_id}/profile", valid_body,refreshToken);
    }

    @Given("User Send valid student Id to update profile")
    public void Sending_valid_StudentId_update_profile() throws SQLException {
        student.Create_Student();
        StudentID = student.studentId;
        pathParams.put("student_id",StudentID);
    }

    @Then("I verify the appearance of status code 200 and student data updated")
    public void Validate_Response_of_update_student_Profile() throws SQLException {
        Update_Student.prettyPrint();
        Update_Student.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentProfile/GetStudent.json")))
                .body("message", hasToString("Profile updated successfully."));
    }

    @And("Validate data updated successfully into database")
    public void validate_update_student_into_database() throws SQLException {
        studentData.get_student_data_from_database();
        String student_firstName= studentData.studentFirstName;
        String student_lastName = studentData.studentLastName;
        String student_email = studentData.studentEmail;
        Long student_grade = studentData.gradeId;
//        Assert(student_firstName)
    }

}
