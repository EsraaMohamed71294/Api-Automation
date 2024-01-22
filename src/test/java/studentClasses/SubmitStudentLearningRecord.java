package studentClasses;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class SubmitStudentLearningRecord {
    TestBase test = new TestBase();
    TestData data = new TestData();
    String student_id = data.student_Id;
    String class_id =   data.class_id_for_join_session;
    String session_id = data.session_id;
    String invalid_request_body = null;

    String refresh_Token = data.refresh_token;
    Long resource_id = Long.valueOf(data.resource_id);
    Integer Student_learning_Record_id = data.Student_Learning_Record_Id;
    Map<String,Object> PathParams = test.pathParams;
    Response Submit_Student_Learning_Record;
    String request_body = "{\"student_learning_record\":\"{\\\"Submit\\\":\\\"studentLearningRecord\\\"}\",\"student_learning_record_metadata\":\"{\\\"Updated\\\":\\\"StudentLearningRecord\\\"}\"}";
    @When("Performing The API Of SubmitStudentLearningRecord")
    public void submit_student_learning_Record_request(){
        Submit_Student_Learning_Record = test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/resources/{resource_id}/record\n" +
                "\n" , request_body,refresh_Token);
    }
    @Given("User Send Valid Data To SubmitStudentLearningRecord Request")
    public void submit_student_learning_record(){
        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
        PathParams.put("resource_id",resource_id);
    }
    @Then("Response Status Code Is 200 And Body Have StudentLearning RecordId")
    public void validate_student_learning_Record_response(){
        Submit_Student_Learning_Record.prettyPrint();
        Submit_Student_Learning_Record.then().assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/SubmitStudentLearningRecord.json")))
                .body("educational_resource_id",equalTo(Student_learning_Record_id));
    }
   @Given("User Send Invalid UserId To SubmitStudentLearningRecord Request")
    public void invalid_user_submit_student_learning_record(){
        PathParams.put("student_id", "109876547890");
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
        PathParams.put("resource_id",resource_id);
    }
    @Then("Response Status Code Of SubmitStudentLearningRecord Is 403 and Body Have Error Message")
    public void unauthorized_student_in_submit_learning_record(){
        test.Validate_Error_Messages(Submit_Student_Learning_Record, HttpStatus.SC_FORBIDDEN , "Unauthorized",4031);
    }
  @Given("User Send Invalid SessionId To SubmitStudentLearningRecord Request")
    public void invalid_session_id(){
        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id);
        PathParams.put("session_id","123456765432");
        PathParams.put("resource_id",resource_id);
    }
    @Then("Response Status Code Is 403 And Error Message Contains No Access To Session")
    public void validate_invalid_session_id(){
        test.Validate_Error_Messages(Submit_Student_Learning_Record , HttpStatus.SC_FORBIDDEN ,"Unauthorized access. Student does not have access to the resources of the requested session or class", 4036 );
    }
    @Given("User Send Valid Parameters And Empty Body")
    public void send_invalid_body(){
        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
        PathParams.put("resource_id",resource_id);
    }
    @When("Performing The API With Empty Body")
    public void Execute_Request_With_Empty_Body(){
        Submit_Student_Learning_Record = test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/resources/{resource_id}/record\n" +
                "\n" ,invalid_request_body,"" );
    }
    @Then("Response Code Is 400 And Body Have Clear Error Message")
    public void Validate_invalid_body_Request(){
        test.Validate_Error_Messages(Submit_Student_Learning_Record, HttpStatus.SC_BAD_REQUEST , "Invalid request. Please check the path parameters and request context for accuracy", 4002);
    }
}
