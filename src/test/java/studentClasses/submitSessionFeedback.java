package studentClasses;

import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class submitSessionFeedback {
    TestBase test = new TestBase();
    TestData data = new TestData();

    String user_token = data.refresh_token;
    String student_Id = data.student_Id;
    String class_Id = data.class_id_for_join_session;
    String session_id = data.session_id;
    String not_participate_session = data.expensive_session_id;
    String kickedOut_session = data.kickedOut_Session;
    Map<String,Object> pathParams = test.pathParams;
    public Response submit_session_feedback ;
    String valid_request_body = "{\"session_feedback\":1}";
    String Invalid_request_body = "{\"session_feedback\":5}";

    @When("Performing the Api of submit session feedback with valid score")
    public void submit_session_feedback() {
        submit_session_feedback =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/feedback",valid_request_body,user_token);
    }

    @Given("User Send feedback for session")
    public void successful_submission_of_feedback() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id",session_id);
    }

    @Then("The Response should contains status code 200 and message Feedback successfully submitted")
    public void Validate_Response_of_success_submission_feedback (){
        submit_session_feedback.prettyPrint();
        submit_session_feedback.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/submitSessionFeedback.json")))
                .body("feedback_score" ,  equalTo(1),"session_id",hasToString(session_id),"message",hasToString("Feedback successfully submitted."));
    }

    @When("Performing the Api of submit session feedback with invalid score")
    public void submit_session_feedback_With_invalid_score() {
        submit_session_feedback =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/feedback",Invalid_request_body,user_token);
    }

    @Then("The Response should contains status code 400 and message Invalid feedback score")
    public void Validate_Response_Invalid_submission_feedback (){
        Response submit_feedback = submit_session_feedback;
        test.Validate_Error_Messages(submit_feedback,HttpStatus.SC_BAD_REQUEST,"Invalid feedback score. Score must be 0 or 1.",4005);
    }

    @Given("User Send Invalid StudentId to submit feedback")
    public void unauthorized_student () {
        pathParams.put("student_id", "123456789987");
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id", session_id);
    }

    @Then("The Response submit feedback  with invalid user Should Contain Status Code 403 And Error Message Unauthorized")
    public void Validate_Response_for_unauthorized_student (){
        Response submit_feedback = submit_session_feedback;
        test.Validate_Error_Messages(submit_feedback,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

    @Given("User Send studentId kicked out from session to submit feedback")
    public void kickedOut_Student_submit_feedback () {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id", kickedOut_session);
    }

    @Then("The Response of invalid user Should Contain Status Code 403 And Error Message Unauthorized access")
    public void Validate_Response_kickedOut_Student_submit_feedback (){
        Response submit_feedback = submit_session_feedback;
        test.Validate_Error_Messages(submit_feedback,HttpStatus.SC_FORBIDDEN,"Unauthorized access. Student has been kicked-out from this session.",4039);
    }

    @Given("User Send studentId not participate into session to submit feedback")
    public void notParticipate_Student_submit_feedback () {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id", not_participate_session);
    }
    @Then("The Response of not participate student Should Contain Status Code 403 And Error Message Unauthorized access")
    public void Validate_Response_notParticipate_Student_submit_feedback (){
        Response submit_feedback = submit_session_feedback;
        test.Validate_Error_Messages(submit_feedback,HttpStatus.SC_FORBIDDEN,"Unauthorized access. Student did not participate in this session.",4037);
    }

}
