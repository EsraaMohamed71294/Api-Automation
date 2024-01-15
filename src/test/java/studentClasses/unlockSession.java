package studentClasses;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class unlockSession {
    TestBase test = new TestBase();
    TestData data = new TestData();
    String user_token = data.refresh_token;
    String student_Id = data.student_Id;
    String class_id = data.class_Id;
    String session_id = data.session_id;
    Map<String,Object> pathParams = new HashMap<String, Object>();


    @When("Performing the Api of Unlock Session")
    public Response Unlock_Session() {
        String access_token = test.generate_access_token(user_token);
        RequestSpecification request = RestAssured.
                given()
                .pathParams(pathParams)
                .header("Content-Type", "application/json")
                .header("Authorization", access_token);
        Response response = request
                .when()
                .post("/students/{student_id}/classes/{class_id}/sessions/{session_id}/unlock");
        return response;
    }
    Response unlockSession = Unlock_Session();
    @Given("User Send Session Id to unlock session for user")
    public void unlock_session_for_user() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
        pathParams.put("session_id", session_id);
    }
    @Then("I verify the appearance of status code 200 and Session successfully unlocked")
    public void Validate_Response_of_unlocked_session () {
        Unlock_Session().prettyPrint();
        Unlock_Session().then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/Api_Automation/src/test/resources/Schemas/unlockSession.json")))
                .body("session_id" ,  equalTo(session_id) ,"message",containsString("Session successfully unlocked."),"message_id",equalTo(200), "student_id" ,equalTo(student_Id),"class_id",equalTo(class_id));
    }
    @Given("User Send unauthorized session id")
    public void unauthorized_user() {
        pathParams.put("student_id", "123456789987");
        pathParams.put("class_id", class_id);
        pathParams.put("session_id", session_id);
    }
    @Then("The Response of unlockSession Should Contain Status Code 403 And Error Message Unauthorized")
    public void Validate_Response_unlockSession_unauthorized_student (){
        test.Validate_Error_Messages(unlockSession,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
    @Given("student's wallet does not have sufficient wallet")
    public void insufficient_student_wallet() {
        pathParams.put("student_id", "");
        pathParams.put("class_id", class_id);
        pathParams.put("session_id", session_id);
    }
    @Then("The Response of unlockSession Should Contain Status Code 422 And Error Message insufficient student wallet balance")
    public void Validate_Response_unlockSession_insufficient_student_wallet (){
        test.Validate_Error_Messages(unlockSession,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot unlock the session. insufficient student wallet balance.",4228);
    }
    @Given("class does not allow pay per session")
    public void class_not_allow_pay_per_session() {
        pathParams.put("student_id", "");
        pathParams.put("class_id", "");
        pathParams.put("session_id", "");
    }
    @Then("The Response Should Contain Status Code 422 And Error Message pay per session not allowed")
    public void Validate_Response_class_not_allow_pay_per_session (){
        test.Validate_Error_Messages(unlockSession,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot unlock the session. pay per session not allowed for this class.",4227);
    }



}
