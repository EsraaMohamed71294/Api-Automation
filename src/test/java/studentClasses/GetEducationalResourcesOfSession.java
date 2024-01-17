package studentClasses;
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
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
public class GetEducationalResourcesOfSession {

    TestBase test = new TestBase();
    TestData data = new TestData();
    String student_id = data.student_Id;
    String class_id =   data.class_id_for_join_session;
    String session_id = data.session_id;
    String resource_id = data.resource_id;
    String user_token = data.refresh_token;

    String session_with_no_Educational_resources = data.expensive_session_id;
    Map<String,Object> PathParams = test.pathParams;

    public Response get_Educational_resource_of_Session;
    @When("Performing The Api Of GetEducationalResources")
    public void get_Educational_resource_of_Session(){
        get_Educational_resource_of_Session = test.sendRequest("GET" ,"/students/{student_id}/classes/{class_id}/sessions/{session_id}/resources" ,null);
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
                .body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/user/Api_Automation/src/test/resources/Schemas/GetEducationalResourcesOfSession.json")))
                .body("educational_resources.educational_resource_type", hasItems(hasToString("handouts")))
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
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_with_no_Educational_resources);
         }
    @Then("The Response Should Contains Status Code 404 And Message That No Educational resources Found")
    public void Validate_Response_For_NotFound_Response(){
        Response Educational_Resources_Response = get_Educational_resource_of_Session;
         test.Validate_Error_Messages(Educational_Resources_Response , HttpStatus.SC_NOT_FOUND ,"No educational resources found." , 4049 );
        }

}