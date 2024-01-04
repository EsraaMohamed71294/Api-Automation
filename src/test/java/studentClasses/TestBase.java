package studentClasses;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestBase {

	public static String access_token;
	public  static  String student_Id = "123456789011";
	public  static  String class_Id = "270160879432";
	public  static  String class_Title = "Mathematics";
	public  static  String refresh_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6IjEyMzQ1Njc4OTAxMSIsInJvbGUiOiJzdHVkZW50In0sImV4cCI6MTcxNDU1NDA5Ni41MzM2NjQsInR5cGUiOiJyZWZyZXNoIiwianRpIjoiZDNjYjdmMzQ5OTZiNDljN2FmYzEzYmU2ZTI4ZTJiOTgifQ.ACMZH37FekpeR89hkZrVLfq1abvkpm12I01YJdZL1Ek";

	//DataBase connection 
	public final String host = "jdbc:postgresql://nagwa-classes-beta.cluster-c4iigfolsbo7.us-east-1.rds.amazonaws.com:5432/nagwa_classes";
    public final String user = "testing_readwrite";
    public final String password = "8yZ%`6!e?~0q6<MM?hHO";
	
	@DisplayName("Generate Token")
	@BeforeAll
	public static void generate_access_token() {
		RestAssured.baseURI ="https://aevkujc65i.execute-api.us-east-1.amazonaws.com";
		RestAssured.basePath ="/beta/v1";
		RequestSpecification request = 
			RestAssured.given()	   
			.header("Content-Type","application/json")
			.header("Authorization",refresh_token);

			Response response = request.when()
			.post("/token/refresh");
						
		    access_token = response.then().extract().path("access_token");

  
	}
}
