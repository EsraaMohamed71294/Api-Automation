//package studentClasses;
//
//
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import io.restassured.module.jsv.JsonSchemaValidator;
//import io.restassured.response.Response;
//import org.apache.http.HttpStatus;
//import org.junit.jupiter.api.Test;
//
//
//import java.io.File;
//import java.util.Map;
//
//import static org.hamcrest.Matchers.hasToString;
//
//public class LeaveOnGoingSession {
//    TestBase test = new TestBase();
//    TestData data = new TestData();
//    String user_token = data.refresh_token;
//    String student_Id = data.student_Id;
//    String class_Id = data.class_id_for_join_session;
//    String session_id = data.session_id;
//    JoinSession session = new JoinSession();
//    Map<String,Object> pathParams = test.pathParams;
//    public Response Leave_onGoing_session ;
//
//    @When("Performing the Api leave on going session")
//    public void leaveOngoingSession() {
//        Leave_onGoing_session =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/leave",null,user_token);
//    }
//
//    @Given("User left the session")
//    public void successful_submission_of_feedback() {
//        pathParams.put("student_id", student_Id);
//        pathParams.put("class_id", class_Id);
//        pathParams.put("session_id",session_id);
//    }
//
//    @Then("The Response should contains status code 200 and message Successfully left the session")
//    public void Validate_Response_of_success_submission_feedback (){
//        Leave_onGoing_session.prettyPrint();
//        Leave_onGoing_session.then()
//                .statusCode(HttpStatus.SC_OK)
//                .assertThat()
//                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/LeaveOnGoingSession.json")))
//                .body("student_id" ,  hasToString(student_Id),"session_id",hasToString(session_id),"message",hasToString("Successfully left the session."));
//    }
//
//    @Then("I verify status code 404 and message student is not currently part of the session")
//    public void Validate_Response_of_student_is_not_currently_part_of_session (){
//        Response Leave_Session = Leave_onGoing_session;
//        test.Validate_Error_Messages(Leave_Session,HttpStatus.SC_NOT_FOUND,"Session not found or student is not currently part of the session.",40412);
//    }
//
//    @Given("User Send Invalid StudentId to leave session")
//    public void unauthorized_student () {
//        pathParams.put("student_id", "123456789987");
//        pathParams.put("class_id", class_Id);
//        pathParams.put("session_id", session_id);
//    }
//
//    @Then("I verify Status Code 403 And Error Message user unauthorized")
//    public void Validate_Response_for_unauthorized_student (){
//        Response Leave_Session = Leave_onGoing_session;
//        test.Validate_Error_Messages(Leave_Session,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
//    }
//    @Given("user join the session again")
//    public void Join_session_again (){
//            session.join_session_In_Enrolled_Class();
//            session.Join_Session();
//    }
//
//}
