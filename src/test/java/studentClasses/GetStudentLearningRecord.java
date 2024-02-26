package studentClasses;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class GetStudentLearningRecord {

    TestBase test = new TestBase();
    TestData data = new TestData();
    String student_id = data.student_Id;
    String user_token = data.refresh_token;

    Database_Connection connect = new Database_Connection();

    String class_id;
    String session_id;
    String resource_id;
    String resource_id_with_deleted_learning_record;
    String archived_class_id;
    String Student_learning_Record_id;

    String session_educational_resource_id;
    Map<String,Object> PathParams = test.pathParams;
    public Response Get_Student_Learning_Record;

    public GetStudentLearningRecord()throws SQLException{
        get_data_of_get_learning_Record();
    }

    public void get_data_of_get_learning_Record()throws SQLException {
        ResultSet get_data_to_get_learning_record = connect.connect_to_database("select * from sessions_educational_resources ser join public.classes_subjects_sessions css  \n" +
                "on ser.session_id = css.session_id  join classes_subjects cs on css.class_subject_id = cs.class_subject_id  \n" +
                "join classes_students cs2 on cs2.class_id = cs.class_id join classes c on c.class_id = cs2.class_id join students_access_rights sar \n" +
                "on sar.student_id  = cs2.student_id and sar.student_access_right_class_id = cs2.class_id join educational_resources er  \n" +
                "on er.educational_resource_id  = ser.educational_resource_id  join educational_resources_types ert on ert.educational_resource_type_id  \n" +
                "= er.educational_resource_type_id join public.student_learning_records slr on slr.session_educational_resource_id = ser.session_educational_resource_id   \n" +
                "where cs2.student_id ="+student_id+" "+"and slr.student_learning_record_is_deleted = false\n");

        while(get_data_to_get_learning_record.next()){
            session_id= get_data_to_get_learning_record.getString("session_id");
            class_id= get_data_to_get_learning_record.getString("class_id");
            resource_id = get_data_to_get_learning_record.getString("educational_resource_id");
            Student_learning_Record_id= get_data_to_get_learning_record.getString("student_learning_record_id");
            session_educational_resource_id= get_data_to_get_learning_record.getString("session_educational_resource_id");
        }
        ResultSet get_data_to_resource_id_with_no_learning_Records = connect.connect_to_database("select * from sessions_educational_resources ser join public.classes_subjects_sessions css \n" +
                "on ser.session_id = css.session_id  join classes_subjects cs on css.class_subject_id = cs.class_subject_id\n" +
                "join classes_students cs2 on cs2.class_id = cs.class_id join classes c on c.class_id = cs2.class_id join students_access_rights sar\n" +
                "on sar.student_id  = cs2.student_id and sar.student_access_right_class_id = cs2.class_id join educational_resources er \n" +
                "on er.educational_resource_id  = ser.educational_resource_id  join educational_resources_types ert on ert.educational_resource_type_id \n" +
                "= er.educational_resource_type_id left join public.student_learning_records slr on slr.session_educational_resource_id = ser.session_educational_resource_id  \n" +
                "where cs2.student_id ="+student_id+" "+ "and slr.student_learning_record_id isnull or slr.student_learning_record_is_deleted = true ");

        while(get_data_to_resource_id_with_no_learning_Records.next()){
            resource_id_with_deleted_learning_record= get_data_to_resource_id_with_no_learning_Records.getString("educational_resource_id");
        }

        ResultSet get_archived_class = connect.connect_to_database("\n" +
                "select * from public.classes c where c.class_archive_date < now()");

        while(get_archived_class.next()){
            archived_class_id= get_archived_class.getString("class_id");
        }


    }
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
               .body("student_learning_record_id",hasToString(Student_learning_Record_id),"session_educational_resource_id", hasToString(session_educational_resource_id));
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
             System.out.println(archived_class_id);
       System.out.println(session_id);
       System.out.println(resource_id);
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
