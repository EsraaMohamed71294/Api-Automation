package studentClasses;

import io.cucumber.java.en.Given;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TestBase {

	TestData data =new TestData();
	public static String access_token;

	public  String refresh_token = data.refresh_token;
	Map<String,Object> pathParams = new HashMap<String, Object>();

	@Given("Send {string} To Generate Access Token for user")
	public static String  generate_access_token(String refresh_token) {
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

	public Response sendRequest(String method, String endpoint) {
		String access_token = generate_access_token(refresh_token);

		RequestSpecification request = RestAssured.given()
				.pathParams(pathParams)
				.header("Content-Type", "application/json")
				.header("Authorization", access_token);

		switch (method.toUpperCase()) {
			case "POST":
				 return request.when().post(endpoint);
			case "GET":
				return request.when().get(endpoint);
			case "PUT":
				return request.when().put(endpoint);
			case "DELETE":
				return request.when().delete(endpoint);
			case "PATCH":
				 return request.when().patch(endpoint);
			default:
				throw new IllegalArgumentException("Unsupported HTTP method: " + method);
		}
	}

	public void Validate_Error_Messages (Response response,Integer statusCode , String error_message ,Integer error_id ) {
		response.prettyPrint();
		response.then()
				.statusCode(statusCode)
				.assertThat()
				.body("error_message" ,containsString(error_message) ,"error_id" ,equalTo(error_id) );
	}



}
