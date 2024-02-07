package StudentProfile;

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
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.hasToString;

public class UpdateStudentProfile {
    TestBase test = new TestBase();
    GetStudent studentData = new GetStudent();
    CreateStudent student = new CreateStudent();
    Faker fakeDate =new Faker();
    String firstName = fakeDate.name().firstName();
    String lastName = fakeDate.name().lastName();
    String student_firstName_DB;
    String student_lastName_DB;
    Long student_grade_DB;
    Long StudentID;
    String grade;
    Map<String, Object> pathParams = test.pathParams;
    String refreshToken;
    Response Update_Student;

    @When("Performing the Api of Update Student Profile")
    public void Update_Student_Profile() throws SQLException {
        student.get_grade_from_database ();
        grade = student.Grade_ID.toString();
        System.out.println(grade);
        String valid_body = "{\"student_first_name\":\""+ firstName +"\",\"student_last_name\":\""+ lastName +"\",\"grade_id\":\""+ grade +"\"}";
        student.Verify_Student_OTP_already_Auth();
        refreshToken = student.student_refresh_token;
        System.out.println(refreshToken);
        Update_Student = test.sendRequest("PATCH", "/students/{student_id}/profile", valid_body,refreshToken);
    }

    @Given("User Send valid student Id to update profile")
    public void Sending_valid_StudentId_update_profile() throws SQLException {
        student.Create_Student();
        StudentID = student.studentId;
        System.out.println(StudentID);
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
         student_firstName_DB= studentData.studentFirstName;
         student_lastName_DB = studentData.studentLastName;
         student_grade_DB = studentData.gradeId;
            assertEquals(student_firstName_DB,firstName);
            assertEquals(student_lastName_DB,lastName);
            assertEquals(student_grade_DB,grade);
    }

    @When("Performing the Api of Update Student Profile with invalid data")
    public void Update_Student_Profile_with_invalidData() throws SQLException {
        student.get_grade_from_database ();
        grade = student.Grade_ID.toString();
        System.out.println(grade);
        String valid_body = "{\"student_first_name\":\"\",\"student_last_name\":\"\",\"grade_id\":\"\"}";
        student.Verify_Student_OTP_already_Auth();
        refreshToken = student.student_refresh_token;
        System.out.println(refreshToken);
        Update_Student = test.sendRequest("PATCH", "/students/{student_id}/profile", valid_body,refreshToken);
    }

    @Then("I verify the appearance of status code 400 and student data Invalid")
    public void Validate_Response_of_update_student_Profile_invalidData() {
        Response invalid_data = Update_Student;
        test.Validate_Error_Messages(invalid_data,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

    @When("Performing the Api of Update Student Profile with invalid grade")
    public void Update_Student_Profile_invalidGrade() throws SQLException {
        student.get_grade_from_database ();
        grade = student.Grade_ID.toString();
        System.out.println(grade);
        String valid_body = "{\"student_first_name\":\""+ firstName +"\",\"student_last_name\":\""+ lastName +"\",\"grade_id\":\"123456789098\"}";
        student.Verify_Student_OTP_already_Auth();
        refreshToken = student.student_refresh_token;
        System.out.println(refreshToken);
        Update_Student = test.sendRequest("PATCH", "/students/{student_id}/profile", valid_body,refreshToken);
    }



}
