package LookupsData;

import AdminArea.CreateSession;
import ParentAccount.Parent_TestData;
import StudentProfile.CreateStudent;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class ListSubjectByGrade {
    Database_Connection Connect = new Database_Connection();
    TestBase test = new TestBase();
    Parent_TestData data = new Parent_TestData();
    CreateStudent student = new CreateStudent();
    Response List_Subject_ByGrade;
    Long grade_id ;
    String grade_url_text;
    String grade_title;
    String grade_localization_key;
    Integer grade_order;
    Map<String, Object> pathParams = test.pathParams;

    @Given("Performing the Api of List Subject by Grade")
    public void List_Subject_ByGrade() throws SQLException {
        student.get_grade_from_database ();
        grade_id = student.Grade_ID;
        pathParams.put("grade_id",grade_id);
        List_Subject_ByGrade = test.sendRequest("GET", "/grades/{grade_id}/subjects", null, data.Parent_refreshToken);
    }

    @Then("I verify the appearance of status code 200 and Subjects returned successfully")
    public void Validate_Response_of_List_Subjects() {
        List_Subject_ByGrade.prettyPrint();
        List_Subject_ByGrade.then()
                .statusCode(HttpStatus.SC_OK);
//                .assertThat()
//                .body(JsonSchemaValidator.matchesJsonSchema(new File("")))
//                .body("[0].grade_id", equalTo(grade_id),"[0].grade_url_text",hasToString(grade_url_text),
//                        "[0].grade_title",hasToString(grade_title),"[0].grade_localization_key",hasToString(grade_localization_key),
//                        "[0].grade_order",equalTo(grade_order));
    }


}
