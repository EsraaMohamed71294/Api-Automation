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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class SubmitStudentLearningRecord {
    TestBase test = new TestBase();
    TestData data = new TestData();

    Database_Connection connect = new Database_Connection();
    String student_id = data.student_Id;
    public String class_id;
    public String session_id;
    String invalid_request_body = null;
    String refresh_Token = data.refresh_token;
    public String resource_id;
    String Student_learning_Record_id;
    Map<String,Object> PathParams = test.pathParams;
    Response Submit_Student_Learning_Record;
    String request_body = "{\"student_learning_record\":\"{\\\"Submit\\\":\\\"studentLearningRecord\\\"}\",\"student_learning_record_metadata\":\"{\\\"Updated\\\":\\\"StudentLearningRecord\\\"}\"}";

    public SubmitStudentLearningRecord() throws SQLException{
        get_data_of_submit_learning_Record();
    }
    public void get_data_of_submit_learning_Record()throws SQLException {
        ResultSet get_data_to_submit_learning_record = connect.connect_to_database("\n" +
                "select * from sessions_educational_resources ser join public.classes_subjects_sessions css \n" +
                "on ser.session_id = css.session_id  join classes_subjects cs on css.class_subject_id = cs.class_subject_id \n" +
                "join classes_students cs2 on cs2.class_id = cs.class_id join classes c on c.class_id = cs2.class_id join students_access_rights sar\n" +
                " on sar.student_id  = cs2.student_id and sar.student_access_right_class_id = cs2.class_id join educational_resources er \n" +
                "on er.educational_resource_id  = ser.educational_resource_id  join educational_resources_types ert on ert.educational_resource_type_id \n" +
                "= er.educational_resource_type_id  \n" +
                " where cs2.student_id ="+student_id);

            while(get_data_to_submit_learning_record.next()){
                session_id= get_data_to_submit_learning_record.getString("session_id");
                class_id= get_data_to_submit_learning_record.getString("class_id");
                resource_id = get_data_to_submit_learning_record.getString("educational_resource_id");
            }
    }
    public void get_data_of_assertion_submit_learning_Record()throws SQLException {
        ResultSet get_data_to_submit_learning_record = connect.connect_to_database("select * from sessions_educational_resources ser join public.classes_subjects_sessions css \n" +
                "on ser.session_id = css.session_id  join classes_subjects cs on css.class_subject_id = cs.class_subject_id \n" +
                "join classes_students cs2 on cs2.class_id = cs.class_id join classes c on c.class_id = cs2.class_id join students_access_rights sar\n" +
                "\t\t\t\ton sar.student_id  = cs2.student_id and sar.student_access_right_class_id = cs2.class_id join educational_resources er \n" +
                "\t\t\t\ton er.educational_resource_id  = ser.educational_resource_id  join educational_resources_types ert on ert.educational_resource_type_id \n" +
                "\t\t\t\t= er.educational_resource_type_id join public.student_learning_records slr on slr.session_educational_resource_id = ser.session_educational_resource_id  \n" +
                "\t\t\t\twhere cs2.student_id ="+student_id+" "+"and slr.student_learning_record_is_deleted = false");

        while(get_data_to_submit_learning_record.next()){
            Student_learning_Record_id= get_data_to_submit_learning_record.getString("student_learning_record_id");
        }
    }


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
    public void validate_student_learning_Record_response()throws SQLException{
        get_data_of_assertion_submit_learning_Record();
        Submit_Student_Learning_Record.prettyPrint();
        Submit_Student_Learning_Record.then().assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/SubmitStudentLearningRecord.json")))
                .body("student_learning_record_id",hasToString(Student_learning_Record_id));
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
                "\n" ,invalid_request_body,refresh_Token );
    }
    @Then("Response Code Is 400 And Body Have Clear Error Message")
    public void Validate_invalid_body_Request(){
        test.Validate_Error_Messages(Submit_Student_Learning_Record, HttpStatus.SC_BAD_REQUEST , "Invalid request. Please check the path parameters and request context for accuracy", 4002);
    }
}
