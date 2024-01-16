package studentClasses;

import io.cucumber.java.en.Given;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
public class TestBase {

	TestData data =new TestData();
	// DataBase connection
	public final String host = "jdbc:postgresql://nagwa-classes-beta.cluster-c4iigfolsbo7.us-east-1.rds.amazonaws.com:5432/nagwa_classes";

	public final String user = "testing_readwrite";
	public final String password = "8yZ%`6!e?~0q6<MM?hHO";

	public static String access_token;

	public  String refresh_token = data.refresh_token;
	Map<String,Object> pathParams = new HashMap<String, Object>();
	public ResultSet resultSet;
	public Connection connection;
	public Statement statement;

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

	public Response sendRequest(String method, String endpoint,Object requestBody ) {
		String access_token = generate_access_token(refresh_token);
		RequestSpecification request = RestAssured.given()
				.header("Content-Type", "application/json")
				.header("Authorization", access_token);

		if (pathParams != null) {
			request.pathParams(pathParams);
		}

		switch (method.toUpperCase()) {
			case "POST":
				if (requestBody != null ) {
					return request.body(requestBody).when().post(endpoint);
				} else {
					return request.when().post(endpoint);
				}
			case "GET":
				return request.when().get(endpoint);
			case "PUT":
				if (requestBody != null ) {
					return request.body(requestBody).when().post(endpoint);
				} else {
					return request.when().put(endpoint);
				}
			case "DELETE":
				return request.when().delete(endpoint);
			case "PATCH":
				if (requestBody != null ) {
					return request.body(requestBody).when().post(endpoint);
				} else {
					return request.when().patch(endpoint);
				}
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

	public ResultSet connect_to_database (String query){
		try {
			 connection = DriverManager.getConnection(host, user, password);
			 statement = connection.createStatement();
			 resultSet = statement.executeQuery(query);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;

	}


}
