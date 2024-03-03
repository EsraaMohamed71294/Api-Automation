package studentClasses;

import java.sql.ResultSet;
import java.sql.SQLException;
import TestConfig.Database_Connection;
import TestConfig.TestBase;

import java.util.Map;
import java.io.File;

import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpStatus;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;


import static org.hamcrest.Matchers.*;

public class getEnrolledClasses{
	TestBase test = new TestBase();
	TestData data = new TestData();
	public String class_Id;
	public String class_title;
	public String session_Id;
	public String session_title;
	Database_Connection connect = new Database_Connection();
	String user_token = data.refresh_token;
	String student_id = data.student_Id;
	Map<String,Object> pathParams = test.pathParams;
	public Response Get_Enrolled_Classes;

	public getEnrolledClasses()throws SQLException{
		get_sessions_data();
	}

	public void get_sessions_data() throws SQLException {
		ResultSet resultSet = connect.connect_to_database("\n" +
				"select * from public.classes_students cs  inner join classes_subjects cs2 on cs.class_id = cs2.class_id left join \n" +
				"classes_subjects_sessions css on css.class_subject_id = cs2.class_subject_id join sessions s on s.session_id = css.session_id join classes c\n" +
				"on c.class_id = cs.class_id  where cs.student_id ="+student_id);

			while(resultSet.next()){
				class_Id = resultSet.getString("class_id");
				class_title = resultSet.getString("class_title");
				session_Id=  resultSet.getString("session_id");
				session_title = resultSet.getString("session_title");
			}
		}
	@When("Perform the api of Get_Enrolled_Classes")
	public void Get_Enrolled_Classes(){
		Get_Enrolled_Classes =  test.sendRequest("GET", "/students/{studentId}/enrolled-classes", null,user_token);
				System.out.print(class_Id);
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
				.assertThat().body("upcoming_sessions.findAll{it.class_id=="+ class_Id +"}", hasItem(allOf(hasEntry("class_title",class_title),
				hasEntry("session_title",session_title))))
				.body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/GetEnrolledClasses_Schema.json")))
				.body("classes.findAll{it.class_id=="+class_Id+"}" , hasItems(hasEntry("class_title",class_title)));

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
