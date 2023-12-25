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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;


import static org.hamcrest.Matchers.*;


public class TestGetEnrolledClassesAPi {


    public static String access_token;
    public  static  String student_id = "203183438234";

    public TestBase object = new TestBase();

   public String access_token1 = object.access_token;
   public  String student_id1 = object.student_id;

   public  String access_token_2 = object.access_token2;

   public  String student_idd2 = object.student_id2;


   @Given("User Send Vaild Token & StudentId That Enrolled In Classes To The APi")
   @Test
public RequestSpecification build_valid_request(){

    RequestSpecBuilder builder2 = new RequestSpecBuilder();
    RequestSpecification request1;
    builder2.addHeader("Content-Type", "application/json");
    builder2.addHeader("Authorization", access_token1);
    builder2.addPathParam("studentId", student_id1);
    request1 = builder2.build();
    return request1;
}


       @Then("User Should Get All The Classes And Upcoming Sessions Of This Student And The Schema Of Body Is Correct")
       @Test
        public void get_response(){
           Response response1 = RestAssured.given().spec(build_valid_request()).when().get("/students/{studentId}/enrolled-classes");
           response1.prettyPrint();
           ValidatableResponse val1 = response1.then().log().all();
           ValidatableResponse validate_response = response1.then().statusCode(HttpStatus.SC_OK).assertThat().body("classes[1].class_id", hasToString("216137850656"),
                  "classes[1].class_title", hasToString("العلوم"))
 .assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/user/IdeaProjects/NagwaClassesAPIs/src/test/java/NagwaClassesAPIs/schema.json")));

   }
    @Given("User Enter Valid Token & Student Id That Doesn't Enroll In Any Class")

public RequestSpecification send_valid_paramters_for_student_with_noEnrollment(){

    RequestSpecBuilder builder2 = new RequestSpecBuilder();
    RequestSpecification request4;
    builder2.addHeader("Content-Type", "application/json");
    builder2.addHeader("Authorization", access_token_2);
    builder2.addPathParam("studentId", student_idd2);
    request4 = builder2.build();
    return request4;

}
@Then("User Should Get Response Code 200 And Body Contains Empty List")

public void checking_that_respones_return_with_emptyList(){
    Response response1 = RestAssured.given().spec(send_valid_paramters_for_student_with_noEnrollment()).when().get("/students/{studentId}/enrolled-classes");
    ValidatableResponse    val3 =  response1.then().statusCode(HttpStatus.SC_OK).assertThat().body("classes", empty(),
            "upcoming_sessions", empty());
}

    @DisplayName("Invalid Scenario - UNAuthorized")
    @Given("User Enter Invalid Token In The Request Headers")
    @Test
    public RequestSpecification Invalid_Token_Paramter() {


        RequestSpecBuilder builder2 = new RequestSpecBuilder();
        RequestSpecification request2;
        builder2.addHeader("Content-Type","application/json");
        builder2.addHeader("Authorization", "UNauthoirzedinResponseBody");
        request2 = builder2.build();
      return request2;

    }
    @Then("User Should GET 401 Response And UnAuthorized Message")
    public void validate_invalidToken_response(){

        Response response1 = RestAssured.given().spec(Invalid_Token_Paramter()).when().get("/students/"+student_id1+"/enrolled-classes");
        response1.prettyPrint();
        ValidatableResponse validate_response = response1.then().statusCode(HttpStatus.SC_UNAUTHORIZED).assertThat().body("message", equalTo("Unauthorized"));
    }

        @DisplayName("Invalid Scenario - Wrong Parameter")
        @Given("User Enter Invalid Paramter In The API")
        @Test
    public RequestSpecification Invalid_parameter(){
        RequestSpecBuilder builder2 = new RequestSpecBuilder();
        RequestSpecification request3;
        builder2.addHeader("Content-Type","application/json");
        builder2.addHeader("Authorization", access_token1);
        request3 = builder2.build();
        return  request3;
    }
    @Test
    @Then("User Should Get 400 Response Code And Body Contains Error Message")
    public void check_response_code_and_body(){

        Response response1 = RestAssured.given().spec(Invalid_parameter()).when().get("/students/ /enrolled-classes");
        response1.prettyPrint();
        ValidatableResponse val1 = response1.then().log().all();
        ValidatableResponse validate_response = response1.then().statusCode(HttpStatus.SC_BAD_REQUEST).assertThat().body("error_message", containsString("Invalid request") ,
                "error_id", equalTo(4002));



    }


}


//           List<Map<String, ?>> saving_classes_of_response = validate_response.extract().path("classes");
//           List<Map<String, ?>> saving_sessions_of_response = validate_response.extract().path("upcoming_sessions");
//