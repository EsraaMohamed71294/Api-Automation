package studentClasses;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class pay_for_full_class {
    TestBase test = new TestBase();
    TestData data = new TestData();
    String user_token = data.refresh_token;
    String student_Id = data.student_Id;
    String class_id = data.class_Id;

    String session_id = data.session_id;
    String amount_paid;
    String currency;
    Map<String,Object> pathParams = new HashMap<String, Object>();

    @When("Performing the Api of pay_for_full_class")
    public Response pay_for_full_class() {
        String access_token = test.generate_access_token(user_token);
        RequestSpecification request = RestAssured.
                given()
                .pathParams(pathParams)
                .header("Content-Type", "application/json")
                .header("Authorization", access_token);
        Response response = request
                .when()
                .post("/students/{student_id}/classes/{class_id}/pay-full");
        return response;
    }

    Response PayForFullClass = pay_for_full_class();

    @Given("User enrolled into fully paid class")
    public void successful_payment_for_full_class() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
    }
    @Then("I verify the appearance of status code 200 and Full class payment successful.")
    public void Validate_Response_of_unlocked_session () {
        pay_for_full_class().prettyPrint();
        pay_for_full_class().then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/Api_Automation/src/test/resources/Schemas/pay_for_full_class.json")))
                .body("class_id" ,  equalTo(class_id) ,"amount_paid",equalTo(amount_paid),"currency",equalTo(currency), "message" ,containsString("Full class payment successful."));
    }
    @Given("User Send unauthorized user id")
    public void unauthorized_user() {
        pathParams.put("student_id", "123456789987");
        pathParams.put("class_id", class_id);
    }
    @Then("The Response Should Contain Status Code 403 And Error Message Unauthorized")
    public void Validate_Response_unauthorized_student (){
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
    @Given("student's wallet does not have sufficient funds")
    public void Insufficient_wallet_for_full_class() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
    }
    @Then("The Response Should Contain Status Code 400 And Error Message Insufficient balance")
    public void Validate_Response_Insufficient_wallet_for_full_class (){
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_BAD_REQUEST,"Insufficient wallet balance for full class payment.",4008);
    }
    @Given("user try to enroll in class that not available or not listed")
    public void class_not_available() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
    }
    @Then("The Response Should Contain Status Code 400 And Error Message Class not available.")
    public void Validate_Response_class_not_available(){
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_BAD_REQUEST,"Class not available for full payment.",4007);
    }
    @Given("user try to enroll in class have full capacity")
    public void full_capacity_class() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
    }
    @Then("The Response Should Contain Status Code 400 And Error Message This class has reached full capacity.")
    public void Validate_Response_full_capacity_class(){
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_BAD_REQUEST,"This class has reached full capacity, and no seats are currently open.",4006);
    }

}
