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

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class CreateStudent {
    TestBase test = new TestBase();
    Database_Connection Connect = new Database_Connection();
    Faker fakeDate =new Faker();
    String firstName = fakeDate.name().firstName();
    String lastName = fakeDate.name().lastName();
    String email = fakeDate.internet().emailAddress();
    Long studentId;
    Long Grade_ID;
    Long walletId;
    String Valid_body_request ;
    Response Create_Student;
    @Given("Get grades from database")
    public void get_grade_from_database () throws SQLException {
        ResultSet GradeResult = Connect.connect_to_database("SELECT * FROM public.grades g\n" +
                "join stages s \n" +
                "on s.stage_id = g.stage_id \n" +
                "where s.country_id = 102123867837 and g.grade_url_name = '11'");
        while (GradeResult.next()) {
            Grade_ID = GradeResult.getLong("grade_id");
        }

    }
    @And("Get student data from database")
    public void get_student_data_from_database () throws SQLException {
        ResultSet Student_Result = Connect.connect_to_database("select* from students s \n" +
                "join students_wallets sw \n" +
                "on s.student_id = sw.student_id \n" +
                "where s.student_id ="+ studentId +"");
        while (Student_Result.next()) {
            walletId = Student_Result.getLong("student_wallet_id");
        }


    }
    @When("Performing the Api of Create Student With valid data")
    public Long Create_Student() {
        Valid_body_request = "{\"student_first_name\":\""+ firstName +"\",\"student_last_name\":\""+ lastName +"\",\"student_email\":\""+ email +"\"" +
                ",\"grade_id\":"+ Grade_ID +",\"social_media_id\":null}" ;
        Create_Student = test.sendRequest("POST", "/students/create", Valid_body_request,null);
        return studentId;
    }
    @Then("I verify the appearance of status code 201 and Student created successfully")
    public void Validate_Response_of_create_Student_successfully() {
        Create_Student.prettyPrint();
        Create_Student.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentProfile/CreateStudent.json")))
                .body("message", hasToString("Student account created successfully."),"student_id",equalTo(studentId),"student_wallet_id",equalTo(walletId));
    }
}
