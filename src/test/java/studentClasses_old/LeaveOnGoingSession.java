package studentClasses_old;


import static org.hamcrest.Matchers.hasToString;

//public class LeaveOnGoingSession {
//    TestBase test = new TestBase();
//    TestData data = new TestData();
//    String user_token = data.refresh_token;
//
////    JoinSession2 joinSession_Again = new JoinSession2();
//    Database_Connection connect = new Database_Connection();
//    String student_Id = data.student_Id;
//    String class_Id;
//    String session_id;
//    Response join_session_again;
//    String session_id_to_join;
//    String class_id_of_session_to_join;
//    String session_student_not_participate_in;
//    String class_of_session_student_not_participate_in;
//    Map<String,Object> pathParams = test.pathParams;
//
//    public Response Leave_onGoing_session ;
//
//    public void get_data_of_leave_session() throws SQLException {
//        ResultSet resultSet_of_leaving_session = connect.connect_to_database("select * from public.sessions_attendance_logs sal\n" +
//                "join classes_subjects_sessions css on css.session_id = sal.session_id\n" +
//                "join classes_subjects cs on cs.class_subject_id = css.class_subject_id where sal.student_id =" + student_Id + " " + "and sal.session_attendance_log_type not like '%leave%'\n" +
//                "and sal.session_attendance_log_type not like '%kicked%'\n");
//
//        while (resultSet_of_leaving_session.next()) {
//            class_Id = resultSet_of_leaving_session.getString("class_id");
//            session_id = resultSet_of_leaving_session.getString("session_id");
//        }
//        ResultSet resultSet_of_session_student_not_in = connect.connect_to_database("select * from public.classes_subjects_sessions css join classes_subjects cs on css.class_subject_id = cs.class_subject_id \n" +
//                "join classes_students cs2 on cs2.class_id = cs.class_id left join sessions_attendance_logs sal on sal.session_id = css.session_id \n" +
//                "where cs2.student_id ="+student_Id+" "+"and css.session_id not in (select session_id from public.sessions_attendance_logs sal2 where sal2.student_id ="+student_Id+")\n");
//        while(resultSet_of_session_student_not_in.next()){
//            class_of_session_student_not_participate_in = resultSet_of_session_student_not_in.getString("class_id");
//            session_student_not_participate_in = resultSet_of_session_student_not_in.getString("session_id");
//        }
//        ResultSet resultSet_to_join_session_again = connect.connect_to_database("select * from public.sessions_attendance_logs sal \n" +
//                "join classes_subjects_sessions css on css.session_id = sal.session_id \n" +
//                "join classes_subjects cs on cs.class_subject_id = css.class_subject_id \n" +
//                "where sal.student_id ="+student_Id+" "+"and sal.session_id not in (select session_id from public.sessions_attendance_logs sal2\n" +
//                "where sal2.session_attendance_log_type like '%kicked%') ");
//
//        while (resultSet_to_join_session_again.next()){
//            class_id_of_session_to_join = resultSet_to_join_session_again.getString("class_id");
//            session_id_to_join = resultSet_to_join_session_again.getString("session_id");
//        }
//    }
//
//    @When("Performing the Api leave on going session")
//    public void leaveOngoingSession() {
//        Leave_onGoing_session =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/leave",null,user_token);
//    }
//
//    @Given("User left the session")
//    public void successful_submission_of_feedback() throws SQLException{
//        get_data_of_leave_session();
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
//    @Given("User send session_id that he isn't part of")
//    public void send_session_student_not_participate()throws SQLException{
//        get_data_of_leave_session();
//        pathParams.put("student_id", student_Id);
//        pathParams.put("class_id", class_of_session_student_not_participate_in);
//        pathParams.put("session_id",session_student_not_participate_in);
//    }
//    @Then("I verify status code 404 and message student is not currently part of the session")
//    public void Validate_Response_of_student_is_not_currently_part_of_session (){
//        Response Leave_Session = Leave_onGoing_session;
//        test.Validate_Error_Messages(Leave_Session,HttpStatus.SC_NOT_FOUND,"Session not found or student is not currently part of the session.",40412);
//    }
//
//    @Given("User Send Invalid StudentId to leave session")
//    public void unauthorized_student ()throws SQLException {
//        get_data_of_leave_session();
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
//    @Given("user send the same sessionId and class_id that he left from")
//    public void Join_session_again ()throws SQLException{
//        get_data_of_leave_session();
//        pathParams.put("student_id", student_Id);
//        pathParams.put("class_id", class_id_of_session_to_join);
//        pathParams.put("session_id", session_id_to_join);
//    }
//    @When("Performing the Api of Joining Session Again")
//    public void join_session_again(){
//        join_session_again =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/join",null,user_token);
//        join_session_again.prettyPrint();
//    }
//
//}
