package StudentHomeScreen;

import AdminArea.CreateClass;
import AdminArea.GetSession;
import EducatorProfile.Educator_TestData;
import StudentProfile.CreateStudent;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
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
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class GetClassDetailsForStudent {
    TestBase test = new TestBase();
    CreateClass classData = new CreateClass();
    CreateStudent student = new CreateStudent();
    GetSession session = new GetSession();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    Response Enroll_Student_Into_Class;
    Response Get_Class_Details;
    Long Class_ID;
    Long class_id;
    Long student_id;
    Long student_Id;
    Long session_Id;
    Long educator_Id;
    String student_refreshToken;
    String class_title ;
    Integer class_payment_option_id;
    String class_payment_option_name ;
    Long educator_id;
    Long session_id;
    Long subject_id ;

    @Given("Getting student already enrolled into class")
    public void enroll_student_into_class_has_sessions() throws SQLException {
        session.user_send_valid_sessionID();
        session.Get_Session();
        Class_ID = session.ClassID;
        session_Id = session.SessionID;
        educator_Id = session.educatorID;
        student.Create_Student();
        student_Id = student.studentId;
        pathParams.put("student_id",student_Id);
        pathParams.put("class_id",Class_ID);
        student_refreshToken = student.student_refreshToken;
        Enroll_Student_Into_Class = test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/enroll", null,student_refreshToken);
    }

    @When("Performing the Api of Get Class Details")
    public void Get_Class_Details(){
        student_refreshToken = student.student_refreshToken;
        pathParams.put("student_id",student_Id);
        pathParams.put("class_id",Class_ID);
        Get_Class_Details = test.sendRequest("GET", "/students/{student_id}/classes/{class_id}", null,student_refreshToken);
    }

    @And("Getting class details from database")
    public void get_Class_Details_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from classes_students cs \n" +
                "join classes c \n" +
                "on c.class_id = cs.class_id \n" +
                "join class_payment_options cpo \n" +
                "on c.class_payment_option_id = cpo.class_payment_option_id \n" +
                "join classes_subjects cs2 \n" +
                "on cs2.class_id = c.class_id \n" +
                "join classes_subjects_sessions css \n" +
                "on css.class_subject_id = cs2.class_subject_id \n" +
                "where cs.student_id ="+ student_Id +"");

        while (resultSet.next()) {
            class_id = resultSet.getLong("class_id");
            class_title = resultSet.getString("class_title");
            class_payment_option_id = resultSet.getInt("class_payment_option_id");
            class_payment_option_name = resultSet.getString("class_payment_option_name");
//            educator_id = resultSet.getLong("educator_id");
            session_id = resultSet.getLong("session_id");
            subject_id = resultSet.getLong("subject_id");

        }
    }


    @Then("I verify the appearance of status code 200 and student classes details returned")
    public void Validate_Response_classes_details_returned_successfully () {
            Get_Class_Details.prettyPrint();
            Get_Class_Details.then()
                    .statusCode(HttpStatus.SC_OK)
                    .assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentHomeScreen/GetClassDetailsForStudent.json")))
                    .body("class_id", equalTo(class_id), "class_title", hasToString(class_title), "class_payment_option_id", equalTo(class_payment_option_id),
                            "class_payment_option_name", hasToString(class_payment_option_name), "student_is_enrolled", equalTo(true),
                            "sessions.session_id", hasItem(equalTo(session_id)), "subjects.subject_id", hasItem(equalTo(subject_id)));
//        "educators.educator_id", hasItem(equalTo(educator_id)),
        }
}
