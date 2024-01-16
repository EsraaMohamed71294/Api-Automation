package studentClasses;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import io.restassured.module.jsv.JsonSchemaValidator;
import java.io.File;


import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class JoinSession {
    TestBase test = new TestBase();
    TestData data = new TestData();
    String user_token = data.refresh_token;
    String student_Id = data.student_Id;
    String class_Id = data.class_Id;
    String session_id = data.session_id;
    String expensive_session_id = data.expensive_session_id;
    String class_id_for_join_session = data.class_id_for_join_session;
    String fully_Paid_class =data.fully_Paid_class;
    String fully_Paid_class_Session =data.fully_Paid_class_Session;

    String NotActive_student_Id =data.NotActive_student_Id;
    String ended_Session = data.ended_Session;
    String Not_Started_Session = data.Not_Started_Session;
    Map<String,Object> pathParams = test.pathParams;
    public Response joinSession ;
    @When("Performing the Api of Joining Session")
    public void Join_Session() {
        joinSession =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/join");
    }
    @Given("User Send The Post Request Of join session")
    public void join_session_In_Enrolled_Class() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id_for_join_session);
        pathParams.put("session_id",session_id);
    }
    @Then("The Response should contains status code 200 and correct session id")
    public void Validate_Response_of_session_In_Enrolled_Class (){
        joinSession.prettyPrint();
        joinSession.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/Api_Automation/src/test/resources/Schemas/JoinSession.json")))
                .body("session_id" ,  equalTo(session_id));
    }
    @Given("User Send Valid StudentId And ClassId That He Haven't Enrolled In")
    public void unauthorized_student () {
        pathParams.put("student_id", "123456789987");
        pathParams.put("class_id", class_id_for_join_session);
        pathParams.put("session_id", session_id);
    }
    @Then("The Response for join session Should Contain Status Code 403 And Error Message Unauthorized")
    public void Validate_Response_for_unauthorized_student (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
   @Given("Student Join Session IS not Exist")
    public void Session_Not_Found () {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id_for_join_session);
        pathParams.put("session_id", "123456789987");
    }
    @Then("The Response Should Contain Status Code 404 And Error Message That Session Doesnt Exist")
    public void Validate_Response_For_Not_Found_Session (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_NOT_FOUND,"session not found or not eligible for display.",4048);
    }
    @Given("User send class id that not exist")
    public void Class_Not_Found () {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", "123456789098");
        pathParams.put("session_id",session_id);
    }
    @Then("The Response Should Contain Status Code 404 And Error Message That Class Doesnt Exist")
    public void Validate_Response_For_Class_Not_Exist (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_NOT_FOUND,"Class not found or not eligible for display.",4046);
    }
    @Given("User Send InActive StudentId")
    public RequestSpecification Student_Not_Found_OR_NotActive () {
        String Not_Activate_Student_Refresh_Token = data.Not_Activate_Student_Refresh_Token;
        String Not_Activate_Student_access_token = test.generate_access_token(Not_Activate_Student_Refresh_Token);
        pathParams.put("student_id", "430192963192");
        pathParams.put("class_id", class_id_for_join_session);
        pathParams.put("session_id",session_id);
        RequestSpecification request = RestAssured.
                given()
                .pathParams(pathParams)
                .header("Content-Type", "application/json")
                .header("Authorization", Not_Activate_Student_access_token);
        return  request;
    }
    @Then("The Response Should Contain Status Code 403 And Error Message Student Is Deactivated")
    public void Validate_Response_For_Student_NotActive (){
        Response response = Student_Not_Found_OR_NotActive()
                .when()
                .post("/students/{student_id}/classes/{class_id}/sessions/{session_id}/join\n" + "\n");;

        response.prettyPrint();
        response.then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .assertThat()
                .body("error_message", containsString("Student with the specified ID does not exist or is not active.") ,"error_id", equalTo(4041));    }
    @Given("User Send Ended SessionId")
    public void Ended_Session () {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id",ended_Session);
    }
    @Then("The Response Should Contains Status Code 422 And Error Message Session Is Ended")
    public void Validate_Response_For_Ended_Session (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot join the session. session is ended.",4224);
    }
    @Given("User Send NotStarted SessionId")
    public void Not_Started_Session () {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id",Not_Started_Session);
    }
    @Then("The Response Should Contains Status Code 422 And Error Message Session Havent Started")
    public void Validate_Response_For_Not_Started_Session (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot join the session. session not started yet.",4227);
    }
    @Given("User Send KickedOut StudentId")
    public void Kicked_Out_Student_From_Session () {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id_for_join_session);
        pathParams.put("session_id","657894356765");
    }
    @Then("The Response Should Contains StatusCode 422 And Error Message Student Is KickedOut")
    public void Validate_Response_For_KickOut_Student (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot join the session. student kicked out from session.",4225);
    }
    @Given("User Send SessionId That Doesnt Related To Class Or Student")
    public void Session_Not_Related_To_Student () {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id_for_join_session);
        pathParams.put("session_id","209195414546");
    }
    @Then("The Response Should Contains Status Code 422 And Error Message Session Isnt Related To Class Or Student")
    public void Validate_Response_Session_Not_Related_To_Student (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot join the session. session not related to this class or this student",4223);
    }
    @Given("User Send ClassId That Doesnt Allow PayPerSession And SessionId That Doesnt Have AccessRight On")
    public void Pay_Per_Session_Not_Allowed () {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", fully_Paid_class);
        pathParams.put("session_id", fully_Paid_class_Session);
    }
    @Then("The Response Should Contains Status Code 422 And Error Message The Class Doesnt Allow PayPerSession")
    public void Validate_Response_For_Pay_Per_Session_Not_Allowed (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot join the session. pay per session not allowed for this class",4222);
    }
    @Given("User Send StudentId With InSufficient Balance")
    public void Insufficient_Student_Wallet () {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id_for_join_session);
        pathParams.put("session_id",expensive_session_id);
    }
    @Then("The Response Should Contains Status Code 422 And Error Message Student Wallet Is Insufficient")
    public void Validate_Response_For_Insufficient_Balance (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot join the session. insufficient student wallet balance.",4226);
    }
}
