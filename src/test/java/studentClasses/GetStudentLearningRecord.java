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

public class GetStudentLearningRecord {

    TestBase test = new TestBase();
    TestData data = new TestData();
    String student_id = data.student_Id;
    String user_token = data.refresh_token;

    String class_id =   data.class_id_for_join_session;
    String session_id = data.session_id;
    String resource_id = data.resource_id;
    String resource_id_with_deleted_learning_record = data.resource_id_with_Deleted_student_learning_Record;
    String archived_class_id = data.Archived_Class;
    Integer Student_learning_Record_id = data.Student_Learning_Record_Id;
    Map<String,Object> PathParams = test.pathParams;
    public Response Get_Student_Learning_Record;
    @When("Performing The API Of GetStudentLearningRecord")
    public void get_student_learning_record(){
       Get_Student_Learning_Record = test.sendRequest("GET" , "/students/{student_id}/classes/{class_id}/sessions/{session_id}/resources/{resource_id}/record" , null,user_token);
   }
   @Given("User Send Valid Parameters To GetStudentLearningRecords API")
   public void send_valid_data(){
       PathParams.put("student_id", student_id);
       PathParams.put("class_id",class_id);
       PathParams.put("session_id",session_id);
       PathParams.put("resource_id",resource_id);
   }
   @Then("Response Status Code Is 200 And Response Body Contains Student Learning Record")
   public void validate_get_learning_record_response(){
       Get_Student_Learning_Record.prettyPrint();
       Get_Student_Learning_Record.then()
               .statusCode(HttpStatus.SC_OK)
               .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/GetStudentLearningRecord.json")))
               .body("student_learning_record_id",equalTo(Student_learning_Record_id),"session_educational_resource_id", equalTo(16));
   }
   @Given("User Send Invalid UserId To GetStudentLearning Record API")
   public void get_learning_Record_unAuthorized_student(){
       PathParams.put("student_id", "123456654321");
       PathParams.put("class_id",class_id);
       PathParams.put("session_id",session_id);
       PathParams.put("resource_id",resource_id);
   }
    @Then("Response Status Code Is 403 And Body Returns Withe Error Message With No Learning Records")
    public void validate_unauthorized_response(){
        test.Validate_Error_Messages(Get_Student_Learning_Record ,HttpStatus.SC_FORBIDDEN , "Unauthorized" , 4031 );
    }
    @Given("User Send Invalid SessionId To GetStudentLearningRecord API")
    public void got_no_access_to_session(){
       PathParams.put("student_id", student_id);
       PathParams.put("class_id",class_id);
       PathParams.put("session_id","132123456765");
       PathParams.put("resource_id",resource_id);
         }
   @Then("Response Status Code Is 404 And Body Contains Have No Access To Session Error Message")
   public void validate_got_no_access_to_session_response(){
        test.Validate_Error_Messages(Get_Student_Learning_Record , HttpStatus.SC_FORBIDDEN , "Student does not have access to the resources of the requested session or class" ,4036);
   }
   @Given("User Send Archived ClassId To GetStudentLearningRecord API")
    public void class_not_found(){
           PathParams.put("student_id", student_id);
           PathParams.put("class_id",archived_class_id);
           PathParams.put("session_id",session_id);
           PathParams.put("resource_id",resource_id);
   }
   @Then("Response Status Code Is 404 And Body Contains Class Not Found")
   public void validate_class_not_found_response(){
        test.Validate_Error_Messages(Get_Student_Learning_Record , HttpStatus.SC_NOT_FOUND ,"Class not found or not eligible for display", 4046 );
   }
    @Given("User send parameters of deleted LearningRecord")
    public void learning_record_not_found(){
       PathParams.put("student_id", student_id);
       PathParams.put("class_id",class_id);
       PathParams.put("session_id",session_id);
       PathParams.put("resource_id",resource_id_with_deleted_learning_record);
   }
   @Then("Response Status Code Is 404 And Body Have Deleted LearningRecord Message")
   public void validate_deleted_learning_record_response(){
        test.Validate_Error_Messages(Get_Student_Learning_Record , HttpStatus.SC_NOT_FOUND , "Learning record not found or has been deleted" , 40411);
   }

}
