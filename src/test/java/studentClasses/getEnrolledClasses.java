package studentClasses;

import java.util.HashMap;
import java.util.Map;
import java.io.File;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.hamcrest.Matchers.*;


public class getEnrolledClasses   {
	
	TestBase object = new TestBase();
	String user_token = object.access_token;
	String student_id = object.student_Id;
	Map <String,Object> pathParams = new HashMap<String, Object>();
	
	public Response Get_Enrolled_Classes() {
		RequestSpecification request =  
				RestAssured.
					given()
						.pathParams(pathParams)
						.header("Content-Type", "application/json")
						.header("Authorization", user_token); 
					Response response = request
					.when()
					.get("/students/{studentId}/enrolled-classes");
					
					return response;
	}
	
	@DisplayName("Get Enrolled Classes and upcoming session for user")
	@Test
	public void get_Enrolled_Classes_And_Upcoming_Sessions() {
		pathParams.put("studentId", student_id);
				
					Get_Enrolled_Classes().prettyPrint();
					Get_Enrolled_Classes().then()
				    .statusCode(HttpStatus.SC_OK)
				    .assertThat()
					.body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/eclipse-workspace/NagwaClasses/src/test/resources/Schemas/GetEnrolledClasses_Schema.json")))
					.body("classes.findAll{it.class_id=='145146571350'}",hasItems(hasEntry("class_title","الفيزياء")))
					.body("upcoming_sessions.findAll{it.class_id=='145146571350'}",hasItems(hasEntry("class_title","الفيزياء"),hasEntry("session_id","626126564756"),hasEntry("session_title","الحصة الحادية والعشرون: إشعاع الجسم الأسود")));
			
				
//					.body("classes.class_id", hasItem("216137850656"),"classes.class_title", hasItem("العلوم"))
//					.body("upcoming_sessions.class_id", hasItem("216137850656"),"upcoming_sessions.class_title", hasItem("العلوم"),"upcoming_sessions.session_id", hasItem("111111"),"upcoming_sessions.session_title", hasItem("title"));

	}
	
	@DisplayName("unAuthorized-User ID mismatch")
    @Test
    public void Invalid_User_ID() {
		pathParams.put("studentId", "123456789111");
				
				Get_Enrolled_Classes().prettyPrint();
				Get_Enrolled_Classes().then()
				.statusCode(HttpStatus.SC_FORBIDDEN)
				.assertThat()
				.body("error_message", containsString("Unauthorized") ,"error_id", equalTo(4031));
	
    }
}
