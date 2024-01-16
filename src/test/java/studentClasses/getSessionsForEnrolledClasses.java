package studentClasses;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpStatus;


import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.hamcrest.Matchers.*;

public class getSessionsForEnrolledClasses{

	TestBase test = new TestBase();
	TestData data = new TestData();
	Database_Connection Connect =new Database_Connection();
	String user_token = test.refresh_token;
	String student_id = data.student_Id;
	String class_id = data.class_Id;
	String class_title = data.class_Title;
	Long class_id_has_no_sessions = data.class_id_has_no_sessions;
	String class_title_has_no_sessions= data.class_title_has_no_sessions;
	Map<String,Object> pathParams = test.pathParams;
	public Response get_sessions_for_enrolled_class;

	@When("Perform then api of get_sessions_for_enrolled_class")
	public void get_sessions_for_enrolled_class () {
		get_sessions_for_enrolled_class =  test.sendRequest("GET", "/students/{studentId}/classes/{classId}/sessions");
	}
	@Given("user send class contains sessions that user enrolled in")
    public void Get_Sessions_for_Enrolled_Classes () {
		pathParams.put("studentId", student_id);
		pathParams.put("classId", class_id);
    }
	@Then("I verify the appearance of status code 200 and all sessions of enrolled class")
	public void Validate_Response_of_Get_Sessions_for_Enrolled_Classes (){
		get_sessions_for_enrolled_class.prettyPrint();
		get_sessions_for_enrolled_class.then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/eclipse-workspace/NagwaClasses/src/test/resources/Schemas/GetSessionsForEnrolledClasses.json")))
				.body("[0].class_id", hasToString(class_id),"[0].class_title", hasToString(class_title))
				.body("[0].classes_sessions.findAll{it.session_id==197178626527L}",hasItems(hasEntry("session_title","Session 18: Inequality in One Triangle: Angle Comparison"),hasEntry("session_start_date","2023-11-29T18:00:00")));
	}
	@Given("User Send Class that has no sessions")
    public void Get_Class_has_no_sessions () {
		pathParams.put("studentId", student_id);
		pathParams.put("classId", class_id_has_no_sessions);
    }
	@Then("I verify the appearance of status code 200 and empty list of classes")
	public void Validate_Response_for_Class_has_no_sessions () {
		get_sessions_for_enrolled_class.prettyPrint();
		get_sessions_for_enrolled_class.then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/Api_Automation/src/test/resources/Schemas/GetSessionsForEnrolledClasses.json")))
				.body("[0].class_id", equalTo(class_id_has_no_sessions),"[0].class_title", hasToString(class_title_has_no_sessions),"[0].classes_sessions",empty());
	}
	@Given("user send student is not enrolled in the class")
    public void unauthorized_student () {
		pathParams.put("studentId", "123456789987");
		pathParams.put("classId", class_id);
    }
	@Then("I verify the appearance of status code 403 and user unauthorized")
	public void Validate_Response_For_Student_NotEnrolled () {
        Response GetSessionForEnrolledClassesResponse = get_sessions_for_enrolled_class;
        test.Validate_Error_Messages(GetSessionForEnrolledClassesResponse,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
	}
	@Given("user send class is not exist")
    public void Class_Not_Found () {
		pathParams.put("studentId", student_id);
		pathParams.put("classId", "123456789098");
    }
	@Then("I verify the appearance of status code 404 and class not found")
	public void Validate_Response_of_Class_Not_Found(){
        Response GetSessionForEnrolledClassesResponse = get_sessions_for_enrolled_class;
        test.Validate_Error_Messages(GetSessionForEnrolledClassesResponse,HttpStatus.SC_NOT_FOUND,"Class not found or not eligible for display.",4046);
	}

}
