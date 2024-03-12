package studentClasses;

import TestConfig.TestBase;
import TestConfig.Database_Connection;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

//public class submitSessionFeedback {
//    TestBase test = new TestBase();
//    TestData data = new TestData();
//
//    String user_token = data.refresh_token;
//    String student_Id = data.student_Id;
//
//    Database_Connection connect = new Database_Connection();
//    String class_Id;
//
//    String class_Id_of_kicked_out;
//    String Class_id_of_session_no_participate;
//    String session_id = data.session_id;
//    String not_participate_session;
//    String kickedOut_session;
//    Map<String,Object> pathParams = test.pathParams;
//    public Response submit_session_feedback ;
//    String valid_request_body = "{\"session_feedback\":1}";
//    String Invalid_request_body = "{\"session_feedback\":5}";
//
//
//
//    public void get_data_of_leave_session() throws SQLException {
//        ResultSet resultSet_of_session_feed_back = connect.connect_to_database("select * from public.sessions_attendance_logs sal\n" +
//                "join classes_subjects_sessions css on css.session_id = sal.session_id\n" +
//                "join classes_subjects cs on cs.class_subject_id = css.class_subject_id where sal.student_id ="+student_Id+" "+"and sal.session_attendance_log_type not like '%leave%'\n" +
//                "and sal.session_attendance_log_type not like '%kicked%'\n");
//
//        while(resultSet_of_session_feed_back.next()){
//            class_Id = resultSet_of_session_feed_back.getString("class_id");
//            session_id = resultSet_of_session_feed_back.getString("session_id");
//        }
//        ResultSet resultSet_of_session_student_not_in = connect.connect_to_database("select * from public.classes_subjects_sessions css join classes_subjects cs on css.class_subject_id = cs.class_subject_id \n" +
//                "join classes_students cs2 on cs2.class_id = cs.class_id left join sessions_attendance_logs sal on sal.session_id = css.session_id \n" +
//                "where cs2.student_id ="+student_Id+" "+"and css.session_id not in (select session_id from public.sessions_attendance_logs sal2 where sal2.student_id ="+student_Id+")\n");
//        while(resultSet_of_session_student_not_in.next()){
//            Class_id_of_session_no_participate = resultSet_of_session_student_not_in.getString("class_id");
//            not_participate_session = resultSet_of_session_student_not_in.getString("session_id");
//        }
//        ResultSet resultSet_of_kicked_out = connect.connect_to_database("select * from public.sessions_attendance_logs sal\n" +
//                "join classes_subjects_sessions css on css.session_id = sal.session_id\n" +
//                "join classes_subjects cs on cs.class_subject_id = css.class_subject_id where sal.student_id ="+student_Id+" "+"\n" +
//                "and sal.session_attendance_log_type  like '%kicked%'");
//        while (resultSet_of_kicked_out.next()){
//            kickedOut_session= resultSet_of_kicked_out.getString("session_id");
//            class_Id_of_kicked_out = resultSet_of_kicked_out.getString("class_id");
//        }
//    }
//
//
//    @When("Performing the Api of submit session feedback with valid score")
//    public void submit_session_feedback() {
//        System.out.println(class_Id +" " + not_participate_session);
//        submit_session_feedback =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/feedback",valid_request_body,user_token);
//    }
//
//    @Given("User Send feedback for session")
//    public void successful_submission_of_feedback()throws SQLException{
//        get_data_of_leave_session();
//        System.out.println(class_Id +" " + not_participate_session);
//        pathParams.put("student_id", student_Id);
//        pathParams.put("class_id", class_Id);
//        pathParams.put("session_id",session_id);
//    }
//
//    @Then("The Response should contains status code 200 and message Feedback successfully submitted")
//    public void Validate_Response_of_success_submission_feedback (){
//        submit_session_feedback.prettyPrint();
//        submit_session_feedback.then()
//                .statusCode(HttpStatus.SC_OK)
//                .assertThat()
//                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/submitSessionFeedback.json")))
//                .body("feedback_score" ,  equalTo(1),"session_id",hasToString(session_id),"message",hasToString("Feedback successfully submitted."));
//    }
//
//    @When("Performing the Api of submit session feedback with invalid score")
//    public void submit_session_feedback_With_invalid_score() {
//        submit_session_feedback =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/feedback",Invalid_request_body,user_token);
//    }
//
//    @Then("The Response should contains status code 400 and message Invalid feedback score")
//    public void Validate_Response_Invalid_submission_feedback (){
//        Response submit_feedback = submit_session_feedback;
//        test.Validate_Error_Messages(submit_feedback,HttpStatus.SC_BAD_REQUEST,"Invalid feedback score. Score must be 0 or 1.",4005);
//    }
//
//    @Given("User Send Invalid StudentId to submit feedback")
//    public void unauthorized_student ()throws SQLException{
//        get_data_of_leave_session();
//        pathParams.put("student_id", "123456789987");
//        pathParams.put("class_id", class_Id);
//        pathParams.put("session_id", session_id);
//    }
//
//    @Then("The Response submit feedback  with invalid user Should Contain Status Code 403 And Error Message Unauthorized")
//    public void Validate_Response_for_unauthorized_student (){
//        Response submit_feedback = submit_session_feedback;
//        test.Validate_Error_Messages(submit_feedback,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
//    }
//
//    @Given("User Send studentId kicked out from session to submit feedback")
//    public void kickedOut_Student_submit_feedback () throws SQLException{
//        get_data_of_leave_session();
//        pathParams.put("student_id", student_Id);
//        pathParams.put("class_id", class_Id_of_kicked_out);
//        pathParams.put("session_id", kickedOut_session);
//    }
//
//    @Then("The Response of invalid user Should Contain Status Code 403 And Error Message Unauthorized access")
//    public void Validate_Response_kickedOut_Student_submit_feedback (){
//        Response submit_feedback = submit_session_feedback;
//        test.Validate_Error_Messages(submit_feedback,HttpStatus.SC_FORBIDDEN,"Unauthorized access. Student has been kicked-out from this session.",4039);
//    }
//
//    @Given("User Send studentId not participate into session to submit feedback")
//    public void notParticipate_Student_submit_feedback()throws SQLException {
//        get_data_of_leave_session();
//        pathParams.put("student_id", student_Id);
//        pathParams.put("class_id", Class_id_of_session_no_participate);
//        pathParams.put("session_id", not_participate_session);
//
//    }
//    @Then("The Response of not participate student Should Contain Status Code 403 And Error Message Unauthorized access")
//    public void Validate_Response_notParticipate_Student_submit_feedback (){
//        Response submit_feedback = submit_session_feedback;
//        test.Validate_Error_Messages(submit_feedback,HttpStatus.SC_FORBIDDEN,"Unauthorized access. Student did not participate in this session.",4037);
//    }
//
//}
