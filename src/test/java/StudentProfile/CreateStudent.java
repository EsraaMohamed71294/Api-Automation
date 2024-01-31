package StudentProfile;

import TestConfig.TestBase;
import com.github.javafaker.Faker;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class CreateStudent {
    TestBase test = new TestBase();
    Faker fakeDate =new Faker();
    String firstName = fakeDate.name().firstName();
    String lastName = fakeDate.name().lastName();
    String email = fakeDate.internet().emailAddress();
    Long studentId;
    String Valid_body_request = "" ;
    Response Create_Student;
    @Given("Performing the Api of Create Student With valid data")
    public Long Create_Student() {
        Create_Student = test.sendRequest("POST", "/students/create", Valid_body_request,null);
        return studentId;
    }
    @Then("I verify the appearance of status code 200 and Student created successfully")
    public void Validate_Response_of_create_Student_successfully() {
        Create_Student.prettyPrint();
        Create_Student.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/CreateEducator.json")))
                .body("message", hasToString("Educator created successfully."),"educator_id",equalTo(studentId));
    }
}
