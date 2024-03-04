package StudentClasses_Enhancement;

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

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasEntry;

public class GetEnrolledClasses2 {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    CreateSession sessionData = new CreateSession();
    public Long class_Id;
    public String class_title;
    public Long session_Id;
    Long Educator_Id;
    public String session_title;
    Database_Connection connect = new Database_Connection();
    String Student_refresh_token = data.Student_refresh_Token;
    Long student_id = data.student_Id;
    Map<String,Object> pathParams = test.pathParams;

    String class_payment_option_name ;
    Integer class_payment_option_id ;
    Integer class_seats_limit;
    Integer class_block_count ;
    Long educator_id;
    Long subject_id;
    Number class_subject_retail_price ;
    Number class_subject_discounted_price;
    Number class_subject_session_price ;
    Response Get_Enrolled_Classes;


    @Given("Create classes and session for student2")
    public void Create_new_Classes_Sessions() throws SQLException {
        sessionData.Create_Session();
        class_Id = sessionData.Class_ID;
        Educator_Id = sessionData.EducatorId;
        session_Id = sessionData.sessionId;
        Response Enroll_Student_Into_Class = test.sendRequest("POST", "/students/"+ student_id +"/classes/"+ class_Id +"/enroll", null,Student_refresh_token);
        Enroll_Student_Into_Class.prettyPrint();
    }
    @And("user send user id to get all upcoming sessions2")
    public void get_Enrolled_Classes_And_Upcoming_Sessions() {
        pathParams.put("studentId", student_id);
    }
    @When("Perform the api of Get_Enrolled_Classes2")
    public void Get_Enrolled_Classes(){
        Get_Enrolled_Classes =  test.sendRequest("GET", "/students/{studentId}/enrolled-classes", null,Student_refresh_token);
    }

    @And("Get Enrolled Classes from database2")
    public void get_sessions_data() throws SQLException {
        ResultSet resultSet = connect.connect_to_database("select * from classes c \n" +
                "join classes_students cs \n" +
                "on c.class_id = cs.class_id \n" +
                "join class_payment_options cpo \n" +
                "on cpo.class_payment_option_id = c.class_payment_option_id \n" +
                "join classes_subjects cs2 \n" +
                "on cs2.class_id = cs.class_id \n" +
                "join classes_subjects_sessions css \n" +
                "on css.class_subject_id = cs2.class_subject_id \n" +
                "join sessions s \n" +
                "on s.session_id = css.session_id \n" +
                "where cs.student_id ="+ student_id +" and c.class_id ="+ class_Id +"\n");

        while(resultSet.next()){
            class_Id = resultSet.getLong("class_id");
            class_title = resultSet.getString("class_title");
            session_Id=  resultSet.getLong("session_id");
            session_title = resultSet.getString("session_title");
            class_payment_option_name = resultSet.getString("class_payment_option_name");
            class_payment_option_id = resultSet.getInt("class_payment_option_id");
            class_seats_limit = resultSet.getInt("class_seats_limit");
            class_block_count = resultSet.getInt("class_block_count");
            educator_id = resultSet.getLong("educator_id");
            subject_id = resultSet.getLong("subject_id");
            class_subject_retail_price = resultSet.getFloat("class_subject_retail_price");
            class_subject_discounted_price = resultSet.getFloat("class_subject_discounted_price");
            class_subject_session_price = resultSet.getFloat("class_subject_session_price");
        }
    }
    @Then("I Verify The appearance of status code 200 and all upcoming sessions2")
    public void Validate_Response_of_Upcoming_Sessions() {
        Get_Enrolled_Classes.prettyPrint();
        //        body("upcoming_sessions.findAll{it.class_id=="+ class_Id +"}", hasItem(allOf(hasEntry("class_title",class_title),
        //                hasEntry("session_title",session_title))))
        //                .body("classes.findAll{it.class_id=="+class_Id+"}" , hasItems(hasEntry("class_title",class_title)))
        //                .body("classes.findAll{it.class_id=="+ class_Id +"}",hasItem(hasEntry("class_title",class_title),
        //                        hasEntry("class_payment_option_name",class_payment_option_name),hasEntry("class_payment_option_id",class_payment_option_id),
        //                        hasEntry("class_seats_limit",class_seats_limit), hasEntry("class_block_count",null)))
        //                body("upcoming_sessions.findAll{it.class_id=="+ class_Id +"}", hasItem(allOf(hasEntry("class_title",class_title),
        //                hasEntry("session_title",session_title))))
        //                .body("classes.findAll{it.class_id=="+class_Id+"}" , hasItems(hasEntry("class_title",class_title)))
        //                .body("classes.findAll{it.class_id=="+ class_Id +"}",hasItem(hasEntry("class_title",class_title),
        //                        hasEntry("class_payment_option_name",class_payment_option_name),hasEntry("class_payment_option_id",class_payment_option_id),
        //                        hasEntry("class_seats_limit",class_seats_limit), hasEntry("class_block_count",null)))
        //                .body("classes.findAll{it.class_id=="+ class_Id +"}" , hasItems(hasEntry("class_title",class_title)
        //                , hasEntry("class_payment_option_name",class_payment_option_name), hasEntry("class_payment_option_id",class_payment_option_id), hasEntry("class_seats_limit",class_seats_limit), hasEntry("class_block_count",null)))
        //                .body("classes.findAll{it.class_id=="+ class_Id +"}", hasItem(
        //                        allOf(
        //                        hasEntry("class_id", class_Id),
        //                        hasEntry("class_title", class_title),
        //                        hasEntry("class_payment_option_name", class_payment_option_name),
        //                        hasEntry("class_payment_option_id", class_payment_option_id),
        //                        hasEntry("class_seats_limit", class_seats_limit),
        //                        hasEntry("class_block_count", null)
        //                )))
        Get_Enrolled_Classes.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
//                body("upcoming_sessions.findAll{it.class_id=="+ class_Id +"}", hasItem(allOf(hasEntry("class_title",class_title),
//                hasEntry("session_title",session_title))))
//                .body("classes.findAll{it.class_id=="+class_Id+"}" , hasItems(hasEntry("class_title",class_title)))

//                .body("classes.findAll{it.class_id=="+ class_Id +"}",hasItem(hasEntry("class_title",class_title),
//                        hasEntry("class_payment_option_name",class_payment_option_name),hasEntry("class_payment_option_id",class_payment_option_id),
//                        hasEntry("class_seats_limit",class_seats_limit), hasEntry("class_block_count",null)))

//                .body("classes.findAll{it.class_id=="+ class_Id +"}" , hasItems(hasEntry("class_title",class_title)
//                , hasEntry("class_payment_option_name",class_payment_option_name), hasEntry("class_payment_option_id",class_payment_option_id), hasEntry("class_seats_limit",class_seats_limit), hasEntry("class_block_count",null)))

//                .body("classes.findAll{it.class_id=="+ class_Id +"}", hasItem(
//                        allOf(
//                        hasEntry("class_id", class_Id),
//                        hasEntry("class_title", class_title),
//                        hasEntry("class_payment_option_name", class_payment_option_name),
//                        hasEntry("class_payment_option_id", class_payment_option_id),
//                        hasEntry("class_seats_limit", class_seats_limit),
//                        hasEntry("class_block_count", null)
//                )))



                .body("classes.findAll{it.class_id=="+ class_Id +"}", hasItem(
                        hasEntry("class_id", equalTo(class_Id))),
                        hasItem(hasEntry("class_title", hasToString(class_title))),
                        hasItem(hasEntry("class_payment_option_name", hasToString(class_payment_option_name))),
                        hasItem(hasEntry("class_payment_option_id", equalTo(class_payment_option_id))),
                        hasItem(hasEntry("class_seats_limit", equalTo(class_seats_limit))),
                        hasItem(hasEntry("class_block_count", equalTo(null))))


                .body("classes.class_educators.educator_id",hasItems(hasItem(equalTo(educator_id))))
                .body("classes.class_subjects.subject_id",hasItems(hasItem(equalTo(subject_id))),"classes.class_subjects[0].class_subject_retail_price[0]",equalTo(class_subject_retail_price),
                        "classes.class_subjects.class_subject_discounted_price",hasItems(hasItem(equalTo(null))),"classes.class_subjects[0].class_subject_session_price[0]",equalTo(class_subject_session_price))
                .body("upcoming_sessions.class_id",hasItem(equalTo(class_Id)),"upcoming_sessions.class_title",hasItem(hasToString(class_title)),
                        "upcoming_sessions.session_id",hasItem(equalTo(session_Id)),"upcoming_sessions.session_title",hasItem(hasToString(session_title)),"upcoming_sessions.educator_id",hasItem(equalTo(educator_id)))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClasses_Enhancement/GetStudentClasses2.json")));


    }

}
