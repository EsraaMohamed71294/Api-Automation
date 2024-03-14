//package studentClasses;
//
//import java.io.File;
//import TestConfig.Database_Connection;
//import TestConfig.TestBase;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Map;
//
//import TestConfig.Database_Connection;
//import TestConfig.TestBase;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.apache.http.HttpStatus;
//import io.restassured.module.jsv.JsonSchemaValidator;
//import io.restassured.response.Response;
//import org.junit.jupiter.api.Test;
//
//import static org.hamcrest.Matchers.*;
//
//public class getSessionsForEnrolledClasses{
//
//	TestBase test = new TestBase();
//	TestData data = new TestData();
//	Database_Connection connect =new Database_Connection();
//	String user_token = data.refresh_token;
//	String student_id = data.student_Id;
//
//	public String class_Id;
//	public String  session_Id;
//	public  String  session_title;
//	public String class_title;
//	public  Long  class_id_has_no_sessions;
//	public  String class_title_has_no_sessions;
//	Map<String,Object> pathParams = test.pathParams;
//	public Response get_sessions_for_enrolled_class;
//
//
//	public getSessionsForEnrolledClasses()throws SQLException{
//		get_sessions_of_enrolled_classes_data();
//	}
//	public void get_sessions_of_enrolled_classes_data() throws SQLException {
//		ResultSet resultSet_with_sessions = connect.connect_to_database("\n" +
//				"select * from public.classes_students cs  inner join classes_subjects cs2 on cs.class_id = cs2.class_id left join  \n" +
//				"\t\t\t\tclasses_subjects_sessions css on css.class_subject_id = cs2.class_subject_id join sessions s on s.session_id = css.session_id join classes c \n" +
//				"\t\t\t\ton c.class_id = cs.class_id  where cs.student_id ="+student_id+" "+"\n" +
//				"\t\t\t\tand c.class_archive_date > now() and c.class_public_listing_date < now()");
//
//		ResultSet resultSet_without_sessions = connect.connect_to_database("select*from public.classes c join public.classes_subjects cs \n" +
//				"\t\t\t\t on c.class_id  = cs.class_id left join classes_subjects_sessions css on css.class_subject_id  \n" +
//				"\t\t\t\t= cs.class_subject_id join classes_students cs2 on cs2.class_id = c.class_id where css.class_subject_session_id is null and cs2.student_id ="+student_id+" "+"\n" +
//				"\t\t\t\tand c.class_archive_date > now() and c.class_public_listing_date < now() ");
//
//		while(resultSet_with_sessions.next()){
//			class_Id = resultSet_with_sessions.getString("class_id");
//			class_title = resultSet_with_sessions.getString("class_title");
//			session_Id=  resultSet_with_sessions.getString("session_id");
//			session_title = resultSet_with_sessions.getString("session_title");
//		}
//		while (resultSet_without_sessions.next()){
//			class_id_has_no_sessions =resultSet_without_sessions.getLong("class_id");
//			class_title_has_no_sessions = resultSet_without_sessions.getString("class_title");
//		}
//	}
//
//	@When("Perform then api of get_sessions_for_enrolled_class")
//	public void get_sessions_for_enrolled_class () {
//		get_sessions_for_enrolled_class =  test.sendRequest("GET", "/students/{studentId}/classes/{classId}/sessions",null,user_token);
//		System.out.println(class_Id + " " +class_id_has_no_sessions);
//	}
//	@Given("user send class contains sessions that user enrolled in")
//    public void Get_Sessions_for_Enrolled_Classes () {
//		pathParams.put("studentId", student_id);
//		pathParams.put("classId", class_Id);
//    }
//	@Then("I verify the appearance of status code 200 and all sessions of enrolled class")
//	public void Validate_Response_of_Get_Sessions_for_Enrolled_Classes (){
//		get_sessions_for_enrolled_class.prettyPrint();
//		get_sessions_for_enrolled_class.then()
//				.statusCode(HttpStatus.SC_OK)
//				.assertThat()
//				.body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/GetSessionsForEnrolledClasses.json")))
//				.body("class_id", hasToString(class_Id),"class_title", hasToString(class_title))
//				.body("sessions.findAll{it.session_id=="+ session_Id +"}",hasItems(hasEntry("session_title",session_title)));
//	}
//	@Given("User Send Class that has no sessions")
//    public void Get_Class_has_no_sessions () {
//		pathParams.put("studentId", student_id);
//		pathParams.put("classId", class_id_has_no_sessions);
//    }
//	@Then("I verify the appearance of status code 200 and empty list of classes")
//	public void Validate_Response_for_Class_has_no_sessions () {
//		get_sessions_for_enrolled_class.prettyPrint();
//		get_sessions_for_enrolled_class.then()
//				.statusCode(HttpStatus.SC_OK)
//				.assertThat()
//				.body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/GetSessionsForEnrolledClasses.json")))
//				.body("class_id", equalTo(class_id_has_no_sessions),"class_title", equalTo(class_title_has_no_sessions),"sessions",empty());
//	}
//	@Given("user send student is not enrolled in the class")
//    public void unauthorized_student () {
//		pathParams.put("studentId", "123456789987");
//		pathParams.put("classId", class_Id);
//    }
//	@Then("I verify the appearance of status code 403 and user unauthorized")
//	public void Validate_Response_For_Student_NotEnrolled () {
//        Response GetSessionForEnrolledClassesResponse = get_sessions_for_enrolled_class;
//        test.Validate_Error_Messages(GetSessionForEnrolledClassesResponse,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
//	}
//	@Given("user send class is not exist")
//    public void Class_Not_Found () {
//		pathParams.put("studentId", student_id);
//		pathParams.put("classId", "123456789098");
//    }
//	@Then("I verify the appearance of status code 404 and class not found")
//	public void Validate_Response_of_Class_Not_Found(){
//        Response GetSessionForEnrolledClassesResponse = get_sessions_for_enrolled_class;
//        test.Validate_Error_Messages(GetSessionForEnrolledClassesResponse,HttpStatus.SC_NOT_FOUND,"Class not found or not eligible for display.",4046);
//	}
//
//}