package studentClasses;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.object.HasToString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Result;

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
public class GetEducationalResourcesOfSession {

    TestBase test = new TestBase();
    TestData data = new TestData();
    Database_Connection connect = new Database_Connection();
    String student_id = data.student_Id;
    String class_id;
    String session_id;
    String resource_id;

    String educational_resource_type;
    String user_token = data.refresh_token;
    String session_with_no_Educational_resources;

    String class_id_of_session_with_no_er;
    Map<String,Object> PathParams = test.pathParams;

    public Response get_Educational_resource_of_Session;

    public GetEducationalResourcesOfSession()throws SQLException{
        get_data_of_session_Educational_resource_from_database();
    }


    public void get_data_of_session_Educational_resource_from_database() throws SQLException {
        ResultSet resultSet = connect.connect_to_database("\n" +
                "select * from sessions_educational_resources ser join public.classes_subjects_sessions css  \n" +
                "on ser.session_id = css.session_id  join classes_subjects cs on css.class_subject_id = cs.class_subject_id \n" +
                "join classes_students cs2 on cs2.class_id = cs.class_id join classes c on c.class_id = cs2.class_id \n" +
                "join educational_resources er on er.educational_resource_id =  \n" +
                "ser.educational_resource_id  join educational_resources_types ert on ert.educational_resource_type_id = er.educational_resource_type_id join\n" +
                " \t\t\t\tpublic.students_access_rights sar on sar.student_access_right_session_id = ser.session_id or sar.student_access_right_class_id = cs2.class_id \n" +
                " \t\t\t\tjoin sessions s on s.session_id  = ser.session_id \n" +
                " \t\t\t\twhere cs2.student_id ="+student_id+" "+"and s.is_test_session = false and s.session_ended_date notnull ");

        while(resultSet.next()){
            class_id = resultSet.getString("class_id");
            session_id = resultSet.getString("session_id");
            resource_id = resultSet.getString("educational_resource_id");
            educational_resource_type= resultSet.getString("educational_resource_type");
        }

        ResultSet resultSet_of_session_with_no_educational_Resources = connect.connect_to_database("select * from  public.classes_subjects_sessions css join classes_subjects cs on css.class_subject_id = cs.class_subject_id  \n" +
                "join classes_students cs2 on cs2.class_id = cs.class_id join classes c on c.class_id = cs2.class_id left join  \n" +
                "sessions_educational_resources ser on ser.session_id  = css.session_id  join\n" +
                " \t\t\t\tpublic.students_access_rights sar on sar.student_access_right_session_id = ser.session_id or sar.student_access_right_class_id = cs2.class_id \n" +
                " \t\t\t\tjoin sessions s on s.session_id = css.session_id  \n" +
                " \t\t\t\twhere cs2.student_id ="+student_id+" "+"and s.session_ended_date notnull\n" +
                " \t\t\t\tand ser.session_educational_resource_id is null \n" +
                " \t\t\t\t\n" +
                " \t\t\t\t");

            while(resultSet_of_session_with_no_educational_Resources.next()) {
             session_with_no_Educational_resources = resultSet_of_session_with_no_educational_Resources.getString("session_id");
                class_id_of_session_with_no_er = resultSet_of_session_with_no_educational_Resources.getString("class_id");
            }
    }
    @When("Performing The Api Of GetEducationalResources")
    public void get_Educational_resource_of_Session(){
        System.out.println(session_with_no_Educational_resources + "   " + class_id_of_session_with_no_er);
        get_Educational_resource_of_Session = test.sendRequest("GET" ,"/students/{student_id}/classes/{class_id}/sessions/{session_id}/resources" ,null,user_token);
    }
    @Given("User Send Valid Parameters To The Request")
    public void get_EducationalResource(){

        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
    }
    @Then("The Response Should Contain Status Code 200 And The Educational Resources Of The Session")
    public void Validate_Response_Of_Get_Educational_Resources(){
        get_Educational_resource_of_Session.prettyPrint();
        get_Educational_resource_of_Session.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/GetEducationalResourcesOfSession.json")))
                .body("educational_resources.educational_resource_type", hasItems(hasToString(educational_resource_type)))
                .body("class_id", hasToString(class_id),"session_id" , hasToString(session_id),
                        "educational_resources.educational_resources.educational_resource_id", hasItems(hasItem(hasToString(resource_id))));
        }
    @Given("User Send Invalid UserId In The Request")
    public void unAuthorized_User(){
        PathParams.put("student_id", "123456754223");
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
    }
    @Then("The Response Should Contain Status Code 403 And Error Message")
    public void Validate_Response_Of_Unauthorized_User(){
        Response Educational_Resources_Response = get_Educational_resource_of_Session;
        test.Validate_Error_Messages(Educational_Resources_Response, HttpStatus.SC_FORBIDDEN,"Unauthorized" , 4031);
    }
    @Given("User Send SessionId That Doesn't Have Educational Resources")
    public void No_educational_resource_found(){
        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id_of_session_with_no_er);
        PathParams.put("session_id",session_with_no_Educational_resources);
         }
    @Then("The Response Should Contains Status Code 404 And Message That No Educational resources Found")
    public void Validate_Response_For_NotFound_Response(){
        Response Educational_Resources_Response = get_Educational_resource_of_Session;
         test.Validate_Error_Messages(Educational_Resources_Response , HttpStatus.SC_NOT_FOUND ,"Session not found or not eligible for display." , 4048 );
        }

}