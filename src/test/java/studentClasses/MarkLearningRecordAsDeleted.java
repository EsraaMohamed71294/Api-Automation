package studentClasses;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class MarkLearningRecordAsDeleted {
    TestBase test = new TestBase();
    TestData data = new TestData();
    String student_id = data.student_Id;
    String class_id = data.class_id_for_join_session;
    String session_id = data.session_id;

    String refresh_Token =data.refresh_token;
    Database_Connection Connect = new Database_Connection();
    String archived_class = data.Archived_Class;
    Integer newStudentLearningRecordId;
    Integer  studentLearningRecordId;
    Map<String, Object> PathParams = test.pathParams;
    Response Mark_Student_Learning_Record_As_Deleted;
    public Integer get_data() {
        ResultSet resultSet = Connect.connect_to_database("select * from public.student_learning_records slr " +
                "join sessions_educational_resources ser " +
                "on slr.session_educational_resource_id = slr.session_educational_resource_id \n" +
                "where slr.student_id = 123456789011 and educational_resource_id = 123456543221 and slr.student_learning_record_is_deleted = false and ser.session_id = 123451234566\n");
        try {
            while (resultSet.next()) {
                newStudentLearningRecordId = resultSet.getInt("student_learning_record_id");
                if (newStudentLearningRecordId != null) {
                    studentLearningRecordId = newStudentLearningRecordId;
                    return studentLearningRecordId;
                }else{
                    return  newStudentLearningRecordId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    @When("Performing The API Of Mark Learning Record As Deleted API")
    public void perform_mark_learning_record_as_deleted() {
        Mark_Student_Learning_Record_As_Deleted = test.sendRequest("DELETE", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/records/{record_id}", null,refresh_Token);
    }
    @Given("User Send Valid Data To MarkLearningRecordAsDeleted API")
    public void delete_student_learning_record() {
        PathParams.put("student_id", student_id);
        PathParams.put("class_id", class_id);
        PathParams.put("session_id", session_id);
        PathParams.put("record_id", get_data().toString());
    }
    @Then("Response Status Code Is 200 And Body Contains Learning Record Is Deleted Successfully")
    public void validate_mark_learning_Record_response() {
        Mark_Student_Learning_Record_As_Deleted.prettyPrint();
        Mark_Student_Learning_Record_As_Deleted.then()
                .statusCode(HttpStatus.SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/MarkLearningRecordAsDeleted.json")))
                .body("message", containsString("Learning record successfully marked as deleted"))
                .body("record_id", equalTo((studentLearningRecordId)));
    }
    @Given("User Send Invalid Student Id To Mark Learning Record Request")
    public void mark_learning_record_unAuthorizedStudent() {
        PathParams.put("student_id", "134565433123");
        PathParams.put("class_id", class_id);
        PathParams.put("session_id", session_id);
        PathParams.put("record_id", get_data().toString());
    }
    @Then("Status Code Of Mark Request Is 403 And Body Have Error Message")
    public void validate_mark_learning_Record_unauthorized() {
        test.Validate_Error_Messages(Mark_Student_Learning_Record_As_Deleted, HttpStatus.SC_FORBIDDEN, "Unauthorized", 4031);
    }
    @Given("User Send Archived ClassId To Mark Learning Record As Deleted API")
    public void mark_learning_Record_invalid_class() {
        PathParams.put("student_id", student_id);
        PathParams.put("class_id", archived_class);
        PathParams.put("session_id", session_id);
        PathParams.put("record_id", get_data().toString());
    }
    @Then("Response Code is 404 And Body Have Class Not Found Message")
    public void validate_mark_learning_record_invalid_classId() {
        test.Validate_Error_Messages(Mark_Student_Learning_Record_As_Deleted, HttpStatus.SC_NOT_FOUND, "Class not found or not eligible for display", 4046);
    }
    @Given("Send Deleted Learning Record")
    public void mark_learning_Record_marked_as_deleted() {
        PathParams.put("student_id", student_id);
        PathParams.put("class_id", class_id);
        PathParams.put("session_id", session_id);
        PathParams.put("record_id", get_data().toString());
    }
    @Then("Response Code Is 404 And Body Have Learning Record Is Deleted Message")
    public void validate_deleted_learning_record() {
        test.Validate_Error_Messages(Mark_Student_Learning_Record_As_Deleted, HttpStatus.SC_NOT_FOUND, "Learning record not found or has been deleted.", 40411);
    }
}
