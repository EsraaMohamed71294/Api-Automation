package studentClasses;

import io.cucumber.java.en.Given;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TestBase {
	public static String access_token;

	@Given("Send {string} To Generate Access Token for user")
	public static String  generate_access_token( String refresh_token ) {
		RestAssured.baseURI ="https://aevkujc65i.execute-api.us-east-1.amazonaws.com";
		RestAssured.basePath ="/beta/v1";
		RequestSpecification request = 
			RestAssured.given()	   
			.header("Content-Type","application/json")
			.header("Authorization",refresh_token);

			Response response = request.when()
			.post("/token/refresh");

		return access_token = response.then().extract().path("access_token");
	}
	public void Validate_Error_Messages (Response response,Integer statusCode , String error_message ,Integer error_id ) {
		response.prettyPrint();
		response.then()
				.statusCode(statusCode)
				.assertThat()
				.body("error_message" ,containsString(error_message) ,"error_id" ,equalTo(error_id) );
	}



}
