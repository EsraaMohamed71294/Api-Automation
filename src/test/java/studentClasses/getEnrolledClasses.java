package studentClasses;

import java.util.HashMap;
import java.util.Map;
import java.io.File;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.hamcrest.Matchers.*;


public class getEnrolledClasses{
	TestBase test = new TestBase();
	TestData data = new TestData();
	String user_token = test.refresh_token;
	String student_id = data.student_Id;
	Map<String,Object> pathParams = test.pathParams;
	public Response Get_Enrolled_Classes;

	@When("Perform the api of Get_Enrolled_Classes")
	public void Get_Enrolled_Classes() {
		Get_Enrolled_Classes =  test.sendRequest("GET", "/students/{studentId}/enrolled-classes", null,user_token);
	}
	@Given("user send user id to get all upcoming sessions")
	public void get_Enrolled_Classes_And_Upcoming_Sessions() {
		pathParams.put("studentId", student_id);
	}
	@Then("I Verify The appearance of status code 200 and all upcoming sessions")
	public void Validate_Response_of_Upcoming_Sessions() {
		Get_Enrolled_Classes.prettyPrint();
		Get_Enrolled_Classes.then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/eclipse-workspace/NagwaClasses/src/test/resources/Schemas/GetEnrolledClasses_Schema.json")))
				.body("classes.findAll{it.class_id=='145146571350'}",hasItems(hasEntry("class_title","الفيزياء")))
				.body("upcoming_sessions.findAll{it.class_id=='145146571350'}",hasItems(hasEntry("class_title","الفيزياء"),hasEntry("session_id","626126564756"),hasEntry("session_title","الحصة الحادية والعشرون: إشعاع الجسم الأسود")));
	}
	@Given("user send invalid user id")
    public void Invalid_User_ID() {
		pathParams.put("studentId", "123456789111");
    }
	@Then("I verify the appearance of  status code 403 and user unauthorized in getEnrolledClasses")
	public void Validate_Response_of_unauthorized_user() {
		Response getEnrolledClassesResponse = Get_Enrolled_Classes;
		test.Validate_Error_Messages(getEnrolledClassesResponse,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
	}
}
