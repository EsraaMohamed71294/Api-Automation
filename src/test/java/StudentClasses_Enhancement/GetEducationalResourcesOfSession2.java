package StudentClasses_Enhancement;

import AdminArea.AssignEducationalResources;
import AdminArea.CreateSession;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import studentClasses.TestData;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasToString;

public class GetEducationalResourcesOfSession2 {
        TestBase test = new TestBase();
        Student_TestData data = new Student_TestData();
        Database_Connection connect = new Database_Connection();
        CreateSession session = new CreateSession();
        AssignEducationalResources sessionData = new AssignEducationalResources();
        Long student_id = data.student_Id;
        Long class_id;
        Long session_id;
        Long resource_id;
        String educational_resource_type;
        String student_refresh_token = data.Student_refresh_Token;
        String session_with_no_Educational_resources;
        String class_id_of_session_with_no_er;
        String class_title;
        String class_payment_option_name;
        Long educator_id;
        Long subject_id;
        Integer class_payment_option_id;
        String session_title;
        Integer class_seats_limit;
        Float class_subject_retail_price;
        Float class_subject_session_price;
        Integer educational_resource_type_id;
        String educational_resources_name;
        Long educational_resource_id;
        String educational_resource_md5;
        Integer file_type_id;
        Map<String,Object> PathParams = test.pathParams;
        public Response get_Educational_resource_of_Session;

    @And("get educational resource data of session from database")
        public void get_data_of_session_Educational_resource_from_database() throws SQLException {
            ResultSet resultSet = connect.connect_to_database("select * from classes c \n" +
                    "join classes_subjects cs \n" +
                    "on c.class_id = cs.class_id \n" +
                    "join class_payment_options cpo \n" +
                    "on cpo.class_payment_option_id = c.class_payment_option_id \n" +
                    "join classes_subjects_sessions css \n" +
                    "on css.class_subject_id = cs.class_subject_id \n" +
                    "join sessions s \n" +
                    "on css.session_id = s.session_id \n" +
                    "join sessions_educational_resources ser \n" +
                    "on ser.session_id = s.session_id \n" +
                    "join educational_resources er \n" +
                    "on er.educational_resource_id = ser.educational_resource_id \n" +
                    "where s.session_id ="+ session_id +"");

            while(resultSet.next()){
                class_id = resultSet.getLong("class_id");
                class_title = resultSet.getString("class_title");
                session_id = resultSet.getLong("session_id");
                class_payment_option_id = resultSet.getInt("class_payment_option_id");
                class_payment_option_name = resultSet.getString("class_payment_option_name");
                educator_id= resultSet.getLong("educator_id");
                session_title = resultSet.getString("session_title");
                class_seats_limit = resultSet.getInt("class_seats_limit");
                subject_id = resultSet.getLong("subject_id");
                class_subject_retail_price = resultSet.getFloat("class_subject_retail_price");
                class_subject_session_price = resultSet.getFloat("class_subject_session_price");
                educational_resource_type_id = resultSet.getInt("educational_resource_type_id");
                educational_resources_name = resultSet.getString("educational_resources_name");
                educational_resource_id = resultSet.getLong("educational_resource_id");
                educational_resource_md5 = resultSet.getString("educational_resource_md5");
                file_type_id = resultSet.getInt("file_type_id");
            }

//            ResultSet resultSet_of_session_with_no_educational_Resources = connect.connect_to_database("select * from  public.classes_subjects_sessions css join classes_subjects cs on css.class_subject_id = cs.class_subject_id  \n" +
//                    "join classes_students cs2 on cs2.class_id = cs.class_id join classes c on c.class_id = cs2.class_id left join  \n" +
//                    "sessions_educational_resources ser on ser.session_id  = css.session_id  join\n" +
//                    " \t\t\t\tpublic.students_access_rights sar on sar.student_access_right_session_id = ser.session_id or sar.student_access_right_class_id = cs2.class_id \n" +
//                    " \t\t\t\tjoin sessions s on s.session_id = css.session_id  \n" +
//                    " \t\t\t\twhere cs2.student_id ="+student_id+" "+"and s.session_ended_date notnull\n" +
//                    " \t\t\t\tand ser.session_educational_resource_id is null \n" +
//                    " \t\t\t\t\n" +
//                    " \t\t\t\t");

//            while(resultSet_of_session_with_no_educational_Resources.next()) {
//                session_with_no_Educational_resources = resultSet_of_session_with_no_educational_Resources.getString("session_id");
//                class_id_of_session_with_no_er = resultSet_of_session_with_no_educational_Resources.getString("class_id");
//            }
        }
    @Given("User Send Valid Parameters To The Request")
    public void get_EducationalResource() throws InterruptedException, SQLException {
        sessionData.Create_new_Assign_resources();
        class_id = sessionData.Class_Id;
        session_id = sessionData.SessionID;
        resource_id = sessionData.ResourceId;
        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
    }
    @When("Performing The Api Of GetEducationalResources")
    public void get_Educational_resource_of_Session(){
        get_Educational_resource_of_Session = test.sendRequest("GET" ,"/students/{student_id}/classes/{class_id}/sessions/{session_id}/resources" ,null,student_refresh_token);
     }
    @Then("The Response Should Contain Status Code 200 And The Educational Resources Of The Session")
    public void Validate_Response_Of_Get_Educational_Resources(){
         get_Educational_resource_of_Session.prettyPrint();
         get_Educational_resource_of_Session.then()
                    .statusCode(HttpStatus.SC_OK)
                    .assertThat()
//
//                    .body("educational_resources.educational_resource_type", hasItems(hasToString(educational_resource_type)))
//                    .body("class_id", equalTo(class_id),"session_id" , equalTo(session_id),
//                            "educational_resources.educational_resources.educational_resource_id", hasItems(hasItem(equalTo(resource_id))))

                .body("class_id",equalTo(class_id),"class_title",hasToString(class_title),
                "class_payment_option_name",hasToString(class_payment_option_name),"sessions_count",equalTo(1),
                "class_payment_option_id",equalTo(class_payment_option_id), "class_seats_limit",equalTo(class_seats_limit),
                "class_block_count",equalTo(null),"educator_id",equalTo(educator_id),"session_id",equalTo(session_id),"session_title",hasToString(session_title))
                .body("subjects.subject_id",hasItem(equalTo(subject_id)),"subjects.class_subject_retail_price",hasItem(equalTo(class_subject_retail_price)),
                        "subjects.class_subject_discounted_price",hasItem(equalTo(null)),"subjects.class_subject_session_price",hasItem(equalTo(class_subject_session_price)))
                .body("educational_resources.educational_resource_type_id", hasItems(equalTo(educational_resource_type_id)))
                 .body("educational_resources.educational_resources.educational_resource_id", hasItems(hasItem(equalTo(educational_resource_id))),
                         "educational_resources.educational_resources.educational_resource_name", hasItems(hasItem(hasToString(educational_resources_name))),
                         "educational_resources.educational_resources.educational_resource_md5", hasItems(hasItem(hasToString(educational_resource_md5))))
                 .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/GetEducationalResourcesOfSession.json")));
    }
        @Given("User Send Invalid UserId In The Request")
        public void unAuthorized_User(){
            PathParams.put("student_id", "123456754223");
            PathParams.put("class_id",class_id);
            PathParams.put("session_id",session_id);
        }
        @Then("The Response Should Contain Status Code 403 And Error Message")
        public void Validate_Response_Of_Unauthorized_User(){
            Response Educational_Resources_Response = get_Educational_resource_of_Session;
            test.Validate_Error_Messages(Educational_Resources_Response, HttpStatus.SC_FORBIDDEN,"Unauthorized" , 4031);
        }
        @Given("User Send SessionId That Doesn't Have Educational Resources")
        public void No_educational_resource_found(){
            PathParams.put("student_id", student_id);
            PathParams.put("class_id",class_id_of_session_with_no_er);
            PathParams.put("session_id",session_with_no_Educational_resources);
        }
        @Then("The Response Should Contains Status Code 404 And Message That No Educational resources Found")
        public void Validate_Response_For_NotFound_Response(){
            Response Educational_Resources_Response = get_Educational_resource_of_Session;
            test.Validate_Error_Messages(Educational_Resources_Response , HttpStatus.SC_NOT_FOUND ,"Session not found or not eligible for display." , 4048 );
        }

    }
