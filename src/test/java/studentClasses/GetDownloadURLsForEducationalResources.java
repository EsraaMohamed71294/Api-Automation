package studentClasses;

import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.Map;

public class GetDownloadURLsForEducationalResources {
    TestBase test = new TestBase();
    TestData data = new TestData();
    String student_id = data.student_Id;
    String class_id =   data.class_id_for_join_session;
    String session_id = data.session_id;
    String resource_id = data.resource_id;
    String user_token = data.refresh_token;

    Map<String,Object> PathParams = test.pathParams;
    Response Get_download_URLs_Of_Educational_Resources;
    String request_body = "{\"educational_resources_ids\":[123456543221,832798379233,389879392938]}";

   @When("Performing The APi Of GetDownload URLs Of Educational Resources")
      public void Get_Download_URLs(){
       Get_download_URLs_Of_Educational_Resources = test.sendRequest("POST","/students/{student_id}/classes/{class_id}/sessions/{session_id}/download-resources",request_body,user_token);
       }
    @Given("User Send Valid Parameters To GetDownloadURLs Request")
    public void Get_Download_URLs_Of_Educational_Resources() {
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
                .body("resources.educational_resource_id",hasItems(hasToString(resource_id)),"resources.educational_resource_type",hasItems(hasToString("handouts")));
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
