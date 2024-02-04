package StudentProfile;

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

public class GetStudent {
    TestBase test = new TestBase();
    CreateStudent student = new CreateStudent();
    Database_Connection Connect = new Database_Connection();
    Response Get_Student_Profile;
    String refreshToken;
    String studentFirstName;
    String studentLastName;
    String studentEmail;
    Long StudentID;

    public Map<String, Object> pathParams = test.pathParams;

    public void get_student_data_from_database() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from students s where student_id ="+ StudentID +"");

        while (resultSet.next()) {
            studentFirstName = resultSet.getString("student_first_name");
            studentLastName = resultSet.getString("student_last_name");
            studentEmail = resultSet.getString("student_email");
        }}

        @When("Performing the Api of Get Student Profile")
        public void Get_Student_Profile() throws SQLException {
        student.Verify_Student_OTP_already_Auth();
        refreshToken = student.student_refresh_token;
        System.out.println("token " + refreshToken);
            Get_Student_Profile = test.sendRequest("GET", "/students/{student_id}/profile", null,refreshToken);
        }

        @Given("User Send valid student Id")
        public void Sending_valid_StudentId() throws SQLException {
            student.Create_Student();
            StudentID = student.studentId;
            System.out.println("StudentID "+ StudentID);
            pathParams.put("student_id",StudentID);
        }

        @Then("I verify the appearance of status code 200 and student data returned")
        public void Validate_Response_of_Getting_student_Profile() throws SQLException {
            get_student_data_from_database();
            Get_Student_Profile.prettyPrint();
            Get_Student_Profile.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentProfile/GetStudent.json")))
                .body("first_name", hasToString(studentFirstName), "last_name", hasToString(studentLastName), "email", hasToString(studentEmail),"user_id", equals(StudentID));
    }
}
