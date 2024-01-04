package studentClasses;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.hamcrest.Matchers.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class getSessionsForEnrolledClasses extends TestBase {
	
	TestBase object = new TestBase();
	Database_Connection Connect =new Database_Connection();
	String user_token = object.access_token;
	String student_id = object.student_Id;
	String class_id = object.class_Id;
	String class_title = object.class_Title;
	String class_id_has_no_sessions = "624185604856";
	String class_title_has_no_sessions= "اللغة العربية";
	Map <String,Object> pathParams = new HashMap<String, Object>();
	
	
	public Response get_sessions_for_enrolled_class () {
		RequestSpecification request = RestAssured.
			given()
				.pathParams(pathParams)
				.header("Content-Type", "application/json")
				.header("Authorization", user_token); 
			Response response = request
			.when()
			.get("/students/{studentId}/classes/{classId}/sessions");;
		return response;
	}
	
	public Void Database_Connection (String SQL_Query) {
		try {
			Connection connect = DriverManager.getConnection(object.host, object.user, object.password);
			 PreparedStatement pst = connect.prepareStatement(SQL_Query);
	         ResultSet rs = pst.executeQuery();
	         rs.next() ;
	         }
			catch(SQLException e){
	            System.out.println(e.getMessage());
			}

	}
	
	
	@DisplayName("Get sessions for Enrolled user into class")
    @Test
    public void Get_Sessions_for_Enrolled_Classes () {
	
		pathParams.put("studentId", student_id);
		pathParams.put("classId", class_id);

				get_sessions_for_enrolled_class ().prettyPrint();
				get_sessions_for_enrolled_class ().then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/eclipse-workspace/NagwaClasses/src/test/resources/Schemas/GetSessionsForEnrolledClasses.json")))
				.body("[0].class_id", hasToString(class_id),"[0].class_title", hasToString(class_title))
				.body("[0].classes_sessions.findAll{it.session_id==197178626527L}",hasItems(hasEntry("session_title","Session 18: Inequality in One Triangle: Angle Comparison"),hasEntry("session_start_date","2023-11-29T18:00:00")));

    }
	
	@DisplayName("Class that has no sessions")
    @Test
    public void Get_Class_has_no_sessions () {
		
		pathParams.put("studentId", student_id);
		pathParams.put("classId", class_id_has_no_sessions);
		
				get_sessions_for_enrolled_class ().prettyPrint();
				get_sessions_for_enrolled_class ().then()
				.statusCode(HttpStatus.SC_OK)
				.assertThat()
				.body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/eclipse-workspace/NagwaClasses/src/test/resources/Schemas/GetSessionsForEnrolledClasses.json")))
				.body("class_id", hasToString(class_id_has_no_sessions),"class_title", hasToString(class_title_has_no_sessions),"sessions",empty());
	
    }
	
	@DisplayName("student is not enroled in the class")
    @Test
    public void unauthorized_student () {
		
		pathParams.put("studentId", "123456789987");
		pathParams.put("classId", class_id);
		
				get_sessions_for_enrolled_class ().prettyPrint();
				get_sessions_for_enrolled_class ().then()
				.statusCode(HttpStatus.SC_FORBIDDEN)
				.assertThat()
				.body("error_message", containsString("Unauthorized") ,"error_id", equalTo(4031));
	
    }
	
	@DisplayName("class is not exist")
    @Test
    public void Class_Not_Found () {
		
		pathParams.put("studentId", student_id);
		pathParams.put("classId", "123456789098");
				
				get_sessions_for_enrolled_class ().prettyPrint();
				get_sessions_for_enrolled_class ().then()
				.statusCode(HttpStatus.SC_NOT_FOUND)
				.assertThat()
				.body("error_message", containsString("Class not found or not eligible for display.") ,"error_id", equalTo(4046));
	
    }

}
