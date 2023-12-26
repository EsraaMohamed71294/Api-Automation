package NagwaClassesAPIs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.object.HasToString;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.devtools.v85.network.model.Request;

import java.io.File;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.hasItems;


public class GetClassSessionsForEnrolledStudentsAPI {

    public TestBase object = new TestBase();

    public String access_token1 = object.access_token;
    public String access_token3 = object.access_token2;
    public String student_id1 = object.student_id;

    public String access_token_2 = object.access_token2;

    public String student_idd2 = object.student_id2;

    public String class_id = object.class_id_contains_sessions;

    public String class_id2 = object.class_id_contains_nosessions;


    @Given("User Enter Valid Toke And Parmaters Contains ClassId Have Sessions")

    public RequestSpecification build_request() {

        RequestSpecBuilder builder4 = new RequestSpecBuilder();
        RequestSpecification request3;
        builder4.addHeader("Content-Type", "application/json");
        builder4.addHeader("Authorization", access_token1);
        builder4.addPathParam("studentId", student_id1);
        builder4.addPathParam("classId", class_id);
        request3 = builder4.build();
        return request3;
    }

    @Then("The User Should Get Response Code 200 And Body Contains Class Details And Sessions Of This Class And Correct")
    @Test
    public void validate_response() {
        Response respons2 = RestAssured.given().spec(build_request()).when().get("/students/{studentId}/classes/{classId}/sessions");
        respons2.prettyPrint();

        ValidatableResponse validateRespons3 = respons2.then().statusCode(HttpStatus.SC_OK).assertThat().body("class_id", hasToString("[270160879432]"), "classes_sessions[0].session_id[1]", hasToString("209195414546"))
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/user/IdeaProjects/NagwaClassesAPIs/src/test/java/NagwaClassesAPIs/schema2.json")));

    }
    @Given("User Enter Valid Token And Paramters Contains ClassId That Doesn't Have Any Sessions")

    public RequestSpecification build_request1(){
        RequestSpecBuilder builder5 = new RequestSpecBuilder();
        RequestSpecification request5;
        builder5.addHeader("Content-Type", "application/json");
        builder5.addHeader("Authorization",access_token1);
        builder5.addPathParam("studentId",student_id1);
        builder5.addPathParam("classId",class_id2);
        request5 = builder5.build();
        return request5;
    }

    @Then("User Should Get Class Details And Empty List Of Sessions")

    public void check_response(){
        Response res3 = RestAssured.given().spec(build_request1()).when().get("/students/{studentId}/classes/{classId}/sessions");
        res3.prettyPrint();
        ValidatableResponse validateRespons3 = res3.then().statusCode(HttpStatus.SC_OK).assertThat().body("class_id", hasToString("[459109472810]"), "classes_sessions[0].session_id[]", empty());

    }

    @Given("User Enter Invalid Toke And Valid Paramters")
    public RequestSpecification build_invalidrequest1(){
        RequestSpecBuilder builder6 = new RequestSpecBuilder();
        RequestSpecification request6;
        builder6.addHeader("Content-Type", "application/json");
        builder6.addHeader("Authorization","testtttttttt");
        builder6.addPathParam("studentId",student_id1);
        builder6.addPathParam("classId",class_id2);
        request6 = builder6.build();
        return request6;
    }
    @Then("User Should Get 401 Response And Error Message")

    public void check_unAuth_response(){

        Response response1 = RestAssured.given().spec(build_invalidrequest1()).when().get("/students/{studentId}/classes/{classId}/sessions");
        response1.prettyPrint();
        ValidatableResponse validate_response = response1.then().statusCode(HttpStatus.SC_UNAUTHORIZED).assertThat().body("message", equalTo("Unauthorized"));

    }

    @Given("User Enter Invalid Student Id And Valid ClassId")
    public RequestSpecification Invalid_student_id_andValid_classId(){
        RequestSpecBuilder builder7 = new RequestSpecBuilder();
        RequestSpecification request7;
        builder7.addHeader("Content-Type", "application/json");
        builder7.addHeader("Authorization",access_token_2);
        builder7.addPathParam("studentId",student_idd2);
        builder7.addPathParam("classId",class_id);
        request7 = builder7.build();
        return request7;

    }
    @Then("User Should Get 403 Response Code And Error Message In Response Body")
    public void check_response4(){

        Response response1 = RestAssured.given().spec(Invalid_student_id_andValid_classId()).when().get("/students/{studentId}/classes/{classId}/sessions");
        response1.prettyPrint();
        ValidatableResponse validate_response = response1.then().statusCode(HttpStatus.SC_FORBIDDEN).assertThat().body("error_id", hasToString("4035"));

    }

    @Given("User Enter Valid StudentId And Invalid ClassId")

    public RequestSpecification Invalid_class_id(){
        RequestSpecBuilder builder8 = new RequestSpecBuilder();
        RequestSpecification request8;
        builder8.addHeader("Content-Type", "application/json");
        builder8.addHeader("Authorization",access_token1);
        builder8.addPathParam("studentId",student_id1);
        builder8.addPathParam("classId","123456789000");
        request8 = builder8.build();
        return request8;
    }
    @Then("User  Should Get 404 Response Code And Error Message In Response Body")
    public void check_Response0(){

        Response response1 = RestAssured.given().spec(Invalid_class_id()).when().get("/students/{studentId}/classes/{classId}/sessions");
        response1.prettyPrint();
        ValidatableResponse validate_response = response1.then().statusCode(HttpStatus.SC_NOT_FOUND).assertThat().body("error_id", hasToString("4046"));
    }

}
