//package studentClasses;
//
//import TestConfig.Database_Connection;
//import TestConfig.TestBase;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import io.restassured.module.jsv.JsonSchemaValidator;
//import io.restassured.response.Response;
//import org.apache.http.HttpStatus;
//
//import java.io.File;
//import java.net.ConnectException;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Map;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.equalTo;
//
//public class unlockSession {
//    TestBase test = new TestBase();
//    TestData data = new TestData();
//    String user_token = data.refresh_token;
//    String student_Id = data.student_Id;
//    Database_Connection Connect = new Database_Connection();
//    String locked_session;
//
//    String unlockedSession;
//
//    String class_id_of_unlocked_session;
//    String fully_Paid_class;
//    String fully_Paid_class_Session;
//    String expensive_session_id;
//    String class_id_for_join_session;
//
//    String Class_Id_Of_Expensive_session;
//    Map<String, Object> pathParams = test.pathParams;
//    public Response Unlock_Session;
//
//    public unlockSession() throws SQLException {
//        get_classes_data_from_database();
//    }
//
//    public void get_classes_data_from_database() throws SQLException {
//        ResultSet resultSet = Connect.connect_to_database("select * from public.classes_subjects_sessions css join sessions s ON  s.session_id = css.session_id join public.classes_subjects cs on  \n" +
//                "cs.class_subject_id = css.class_subject_id join classes c on c.class_id = cs.class_id join classes_students cs2 on cs2.class_id   \n" +
//                "= c.class_id   \n" +
//                "left join students_access_rights sar    \n" +
//                "on sar.student_id = cs2.student_id   \n" +
//                "join students_wallets sw on sw.student_id = cs2.student_id\n" +
//                "where cs2.student_id ="+student_Id+" "+"and c.class_payment_option_id =3 and   \n" +
//                "css.session_id not in      \n" +
//                "(select sar2.student_access_right_session_id from public.students_access_rights sar2 where sar2.student_id ="+student_Id+" "+"and sar2.student_access_right_session_id notnull)   \n" +
//                "and c.class_archive_date > now() and c.class_public_listing_date < now() and cs.class_subject_session_price < sw.student_wallet_balance  ");
//        while (resultSet.next()) {
//            class_id_for_join_session = resultSet.getString("class_id");
//             locked_session  =resultSet.getString("session_id");
//
//        }
//
//        ResultSet resultSet_of_unlocked_session = Connect.connect_to_database("\n" +
//                "select sar.student_access_right_session_id , sar.student_access_right_class_id from public.classes_subjects_sessions css join sessions s ON  s.session_id = css.session_id join public.classes_subjects cs on\n" +
//                "cs.class_subject_id = css.class_subject_id join classes c on c.class_id = cs.class_id join classes_students cs2 on cs2.class_id \n" +
//                "= c.class_id \n" +
//                " join students_access_rights sar  \n" +
//                " on sar.student_id = cs2.student_id \n" +
//                "where cs2.student_id =" + student_Id + " " + "and c.class_payment_option_id =3 and \n" +
//                "c.class_archive_date > now() and c.class_public_listing_date < now()     \n" +
//                "");
//        while (resultSet_of_unlocked_session.next()) {
//            unlockedSession = resultSet_of_unlocked_session.getString("student_access_right_session_id");
//            class_id_of_unlocked_session = resultSet_of_unlocked_session.getString("student_access_right_class_id");
//        }
////
//        ResultSet resultSet_of_expensive_session = Connect.connect_to_database(" \n" +
//                "select * from public.sessions s join classes_subjects_sessions css on s.session_id = css.session_id \n" +
//                "join classes_subjects cs on cs.class_subject_id = css.class_subject_id join classes_students cs2 on cs2.class_id \n" +
//                "= cs.class_id inner join subjects s2 on s2.subject_id = cs.subject_id join students s3 on s3.grade_id = s2.grade_id \n" +
//                "join students_wallets sw on sw.student_id  = cs2.student_id  join classes c on c.class_id = cs.class_id left join students_access_rights sar  \n" +
//                "on sar.student_id = cs2.student_id        \n" +
//                "where cs2.student_id =" + student_Id + " " + "\n" +
//                "and \n" +
//                "c.class_payment_option_id = 3 and cs.class_subject_session_price  > sw.student_wallet_balance \n" +
//                "and \n" +
//                "  s.session_id not in  \n" +
//                "(select sar2.student_access_right_class_id from public.students_access_rights sar2 where sar2.student_id =" + student_Id + " " + ") \n" +
//                " limit 10");
//        while (resultSet_of_expensive_session.next()) {
//            expensive_session_id = resultSet_of_expensive_session.getString("session_id");
//            Class_Id_Of_Expensive_session = resultSet_of_expensive_session.getString("class_id");
//        }
//
//        ResultSet resultSet_of_fully_paid_class = Connect.connect_to_database("    \n" +
//                "select * from public.sessions s join classes_subjects_sessions css on s.session_id = css.session_id \n" +
//                "join classes_subjects cs on cs.class_subject_id = css.class_subject_id join classes_students cs2 on cs2.class_id \n" +
//                " = cs.class_id inner join subjects s2 on s2.subject_id = cs.subject_id join students s3 on s3.grade_id = s2.grade_id \n" +
//                "  join students_wallets sw on sw.student_id  = cs2.student_id  join classes c on c.class_id = cs.class_id left join students_access_rights sar  \n" +
//                "on sar.student_id = cs2.student_id \n" +
//                " where cs2.student_id =" + student_Id + " " + "\n" +
//                " and \n" +
//                " c.class_payment_option_id = 1 \n" +
//                " and \n" +
//                " s.session_id not in  \n" +
//                " (select sar2.student_access_right_class_id from public.students_access_rights sar2 where sar2.student_id =" + student_Id + " " + ") \n" +
//                " limit 10\n" +
//                "");
//        while (resultSet_of_fully_paid_class.next()) {
//            fully_Paid_class = resultSet_of_fully_paid_class.getString("class_id");
//            fully_Paid_class_Session = resultSet_of_fully_paid_class.getString("session_id");
//        }
//
//
//    }
//
//    @When("Performing the Api of Unlock Session")
//    public void Unlock_Session() {
//        System.out.println(locked_session + "  " + class_id_for_join_session);
//        Unlock_Session = test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/unlock", null, user_token);
//    }
//
//    @Given("User Send Session Id to unlock session for user")
//    public void unlock_session_for_user() {
//
//        pathParams.put("student_id", student_Id);
//        pathParams.put("class_id", class_id_for_join_session);
//        pathParams.put("session_id", locked_session);
//    }
//
//    @Then("I verify the appearance of status code 201 and Session successfully unlocked")
//    public void Validate_Response_of_unlocked_session() {
//        Unlock_Session.prettyPrint();
//        Unlock_Session.then()
//                .statusCode(HttpStatus.SC_CREATED)
//                .assertThat()
//                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/unlockSession.json")))
//                .body("session_id", equalTo(locked_session), "message", containsString("Session successfully unlocked."), "message_id", equalTo(201), "student_id", equalTo(student_Id), "class_id", equalTo(class_id_for_join_session));
//    }
//
//    @Given("User Send Session Id already unlocked to unlock session for user")
//    public void send_already_unlocked_session() {
//        pathParams.put("student_id", student_Id);
//        pathParams.put("class_id", class_id_of_unlocked_session);
//        pathParams.put("session_id", unlockedSession);
//    }
//
//    @Then("I verify the appearance of status code 200 and Session already unlocked")
//    public void Validate_Response_of_Session_already_unlocked() {
//        Unlock_Session.prettyPrint();
//        Unlock_Session.then()
//                .statusCode(HttpStatus.SC_OK)
//                .assertThat()
//                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/unlockSession.json")))
//                .body("session_id", equalTo(unlockedSession), "message", containsString("Session already unlocked."), "message_id", equalTo(200), "student_id", equalTo(student_Id), "class_id", equalTo(class_id_of_unlocked_session));
//    }
//
//    @Given("User Send unauthorized session id")
//    public void unauthorized_user() {
//        pathParams.put("student_id", "123456789987");
//        pathParams.put("class_id", class_id_for_join_session);
//        pathParams.put("session_id", locked_session);
//    }
//
//    @Then("The Response of unlockSession Should Contain Status Code 403 And Error Message Unauthorized")
//    public void Validate_Response_unlockSession_unauthorized_student() {
//        Response unlockSession = Unlock_Session;
//        test.Validate_Error_Messages(unlockSession, HttpStatus.SC_FORBIDDEN, "Unauthorized", 4031);
//    }
//
//    @Given("student's wallet does not have sufficient wallet for unlock session")
//    public void insufficient_student_wallet_unlockSession() {
//        pathParams.put("student_id", student_Id);
//        pathParams.put("class_id", Class_Id_Of_Expensive_session);
//        pathParams.put("session_id", expensive_session_id);
//    }
//
//    @Then("The Response of unlockSession Should Contain Status Code 422 And Error Message insufficient student wallet balance")
//    public void Validate_Response_unlockSession_insufficient_student_wallet() {
//        Response unlockSession = Unlock_Session;
//        test.Validate_Error_Messages(unlockSession, HttpStatus.SC_UNPROCESSABLE_ENTITY, "Cannot unlock the session. insufficient student wallet balance.", 4228);
//    }
//
//    @Given("class does not allow pay per session")
//    public void class_not_allow_pay_per_session() {
//        pathParams.put("student_id", student_Id);
//        pathParams.put("class_id", fully_Paid_class);
//        pathParams.put("session_id", fully_Paid_class_Session);
//    }
//
//    @Then("The Response Should Contain Status Code 422 And Error Message pay per session not allowed")
//    public void Validate_Response_class_not_allow_pay_per_session() {
//        Response unlockSession = Unlock_Session;
//        test.Validate_Error_Messages(unlockSession, HttpStatus.SC_UNPROCESSABLE_ENTITY, "Cannot unlock the session. pay per session not allowed for this class.", 4227);
//    }
//
//    @Given("delete the access Right From The Student To This Session")
//    public void delete_access_right()throws SQLException {
//        ResultSet resultSet_delete_Access_right = Connect.connect_to_database("delete from students_access_rights where student_id =" + student_Id + " " + "\n" +
//                "and student_access_right_session_id =" + locked_session + " " + "and student_access_right_class_id =" + class_id_for_join_session + "");
//        System.out.println("UnlockSessionAgain");
//    }
//}
