package StudentClasses_Enhancement;

import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class GetDownloadURLsForEducationalResources2 {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    GetEducationalResourcesOfSession2 educationalResource = new GetEducationalResourcesOfSession2();
    Long student_id = data.student_Id;
    Database_Connection connect = new Database_Connection();
    Long class_id;
    Long session_id;
    public Long resource_id;
    String resources_educational_resource_type;
    String student_refresh_token = data.Student_refresh_Token;
    Map<String,Object> PathParams = test.pathParams;
    Response Get_download_URLs_Of_Educational_Resources;
    String valid_request_body;


    public void joinSessionToDownloadResources () throws SQLException, InterruptedException {
        educationalResource.EducationalResourceForEndedSession();
        student_id = data.student_Id;
        class_id = educationalResource.class_id;
        session_id = educationalResource.session_id;
        resource_id = educationalResource.resource_id;
        Response Enroll_Student_Into_Class = test.sendRequest("POST", "/students/"+ student_id +"/classes/"+ class_id +"/enroll", null,student_refresh_token);
        Response joinSession =  test.sendRequest("POST", "/students/"+ student_id +"/classes/"+ class_id +"/sessions/"+ session_id +"/join",null,student_refresh_token);
        joinSession.prettyPrint();
    }


    public void get_data_of_session_Educational_resource_from_database() throws SQLException {

        ResultSet resultSet = connect.connect_to_database("  select * from sessions_educational_resources ser join public.classes_subjects_sessions css  \n" +
                " on ser.session_id = css.session_id  join classes_subjects cs on css.class_subject_id = cs.class_subject_id \n" +
                "join classes_students cs2 on cs2.class_id = cs.class_id join classes c on c.class_id = cs2.class_id \n" +
                "join educational_resources er on er.educational_resource_id =  \n" +
                "ser.educational_resource_id  join educational_resources_types ert on ert.educational_resource_type_id = er.educational_resource_type_id join\n" +
                " \t\t\t\tpublic.students_access_rights sar on sar.student_access_right_session_id = ser.session_id or sar.student_access_right_class_id = cs2.class_id \n" +
                " \t\t\t\tjoin sessions s on s.session_id  = ser.session_id \n" +
                " \t\t\t\twhere cs2.student_id ="+student_id+" " +"and s.is_test_session = false and s.session_ended_date notnull ");

        while (resultSet.next()) {
            class_id = resultSet.getLong("class_id");
            session_id = resultSet.getLong("session_id");
            resource_id = resultSet.getLong("educational_resource_id");
            resources_educational_resource_type= resultSet.getString("educational_resource_type");
        }
    }

    @When("Performing The APi Of GetDownload URLs Of Educational Resources")
    public void Get_Download_URLs(){
        valid_request_body =  "{\"educational_resources_ids\":["+ resource_id +"]}";
        Get_download_URLs_Of_Educational_Resources = test.sendRequest("POST","/students/{student_id}/classes/{class_id}/sessions/{session_id}/download-resources",valid_request_body,student_refresh_token);
    }

    @Given("User Send Valid Parameters To GetDownloadURLs Request")
    public void Get_Download_URLs_Of_Educational_Resources() throws SQLException, InterruptedException {
        joinSessionToDownloadResources ();
        PathParams.put("student_id", student_id);
        PathParams.put("class_id", class_id);
        PathParams.put("session_id", session_id);
    }
    @Then("Response Status Code Is 200 And Response Body Contains EducationalResourceId And DownloadLink")
    public void Validate_GetDownloadURLs_Response(){
        Get_download_URLs_Of_Educational_Resources.prettyPrint();
        Get_download_URLs_Of_Educational_Resources.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/GetDownloadURLsForEducationalResources.json")))
                .body("resources.educational_resource_id",hasItems(equalTo(resource_id)),"resources.educational_resource_type",hasItems(hasToString(resources_educational_resource_type)));
    }

    @Given("User Send Invalid UserId To GetDownloadEducationalResources Request")
    public void Unauthorized_Student_for_download_EducationalResources(){
        PathParams.put("student_id", "123456786433");
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
    }
    @Then("Response Status Code Is 403 And Response Body Contains Error Message And No Links Returns")
    public void Validate_Unauthorized_Response(){
        test.Validate_Error_Messages(Get_download_URLs_Of_Educational_Resources , HttpStatus.SC_FORBIDDEN , "Unauthorized", 4031);
    }
    @Given("User Send SessionId That Student Doesn't Hae Access To")
    public void have_no_access_to_session(){
        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id);
        PathParams.put("session_id","123456789811");
    }

    @Then("Response Status Code Is 403 And Body Returns With Error Message That Student Have No Access")
    public void validate_have_no_access_response(){
        test.Validate_Error_Messages(Get_download_URLs_Of_Educational_Resources , HttpStatus.SC_FORBIDDEN , "Student does not have access to the resources of the requested session or class", 4036);
    }
}

