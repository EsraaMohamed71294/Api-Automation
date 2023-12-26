package NagwaClassesAPIs;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeTest;

public class TestBase {

    public static String access_token;
    public static String access_token2;
    public  static  String student_id = "203183438234";
    public static  String student_id2 = "209105216186";


    public  static  String class_id_contains_sessions = "270160879432";

    public static String class_id_contains_nosessions = "459109472810";


    @Given("Generate Token For The Student")
    @BeforeAll
    public static void generate_access_token(){

        RestAssured.baseURI = "https://aevkujc65i.execute-api.us-east-1.amazonaws.com/beta/v1";
        RequestSpecBuilder builder = new RequestSpecBuilder();
        RequestSpecification spec2;
        builder.addHeader("Content-Type","application/json");
        builder.addHeader("Authorization","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6MjAzMTgzNDM4MjM0LCJyb2xlIjoic3R1ZGVudCJ9LCJleHAiOjE3MTI4MTcxNDEuNzQ0NjczLCJ0eXBlIjoicmVmcmVzaCIsImp0aSI6IjE5NGY5YzdmNTQwZjQyZWZiNTUwMTRlZDRkZGNjNDUzIn0.MLMVeHA7MdRAVNO3vF32h0p7apPVbwbD1DWKw7CUGa8");
        spec2 = builder.build();
        Response response = RestAssured.given().spec(spec2).when().post("/token/refresh");
        access_token = response.then().extract().path("access_token");


    }

    @And("Generate Token For The Another Student")
    @BeforeAll
    public static void generate_access_for_another_student(){

        RestAssured.baseURI = "https://aevkujc65i.execute-api.us-east-1.amazonaws.com/beta/v1";
        RequestSpecBuilder builder3 = new RequestSpecBuilder();
        RequestSpecification spec2;
        builder3.addHeader("Content-Type","application/json");
        builder3.addHeader("Authorization","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6IjIwOTEwNTIxNjE4NiIsInJvbGUiOiJzdHVkZW50In0sImV4cCI6MTcxMzE2NTI2OC44ODMzNjYsInR5cGUiOiJyZWZyZXNoIiwianRpIjoiMWI2NDRmNzZkNTc0NDg5Njk1NzUzMWU1MThjZjk2NzYifQ.MCCBVyZOYVmhUfRJ0Bw5szlQnP62l6Hoq85Yw0rmA7k");
        spec2 = builder3.build();
        Response response2 = RestAssured.given().spec(spec2).when().post("/token/refresh");
        access_token2 = response2.then().extract().path("access_token");


    }



}
