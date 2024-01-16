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
    String Full_Capacity_Class = data.Full_Capacity_Class;
    String Archived_Class = data.Archived_Class;
    String amount_paid_for_class= data.amount_paid_for_class;
    String class_currency = data.class_currency;
    String valid_fullPayment_class = data.valid_fullPayment_class;
    String expensive_class = data.expensive_class;
    Map<String,Object> pathParams = test.pathParams;
    public Response pay_for_full_class ;

    @When("Performing the Api of pay_for_full_class")
    public void pay_for_full_class() {
        pay_for_full_class =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/pay-full");
    }

    @Given("User enrolled into fully paid class")
    public void successful_payment_for_full_class() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id",valid_fullPayment_class);
    }
    @Then("I verify the appearance of status code 200 and Full class payment successful.")
    public void Validate_Response_of_success_payment_fullClass () {
        pay_for_full_class.prettyPrint();
        pay_for_full_class.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/Api_Automation/src/test/resources/Schemas/pay_for_full_class.json")))
                .body("class_id" ,  equalTo(valid_fullPayment_class) ,"amount_paid",equalTo(amount_paid_for_class),"currency",equalTo(class_currency), "message" ,containsString("Full class payment successful."));
    }

    @Then("I verify the appearance of status code 400 and class already purchased")
    public void Validate_Response_already_purchased_Class () {
         Response PayForFullClass = pay_for_full_class;
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_BAD_REQUEST,"Class already purchased.",4004);
    }
    @Given("User Send unauthorized user id")
    public void unauthorized_user() {
        pathParams.put("student_id", "123456789987");
        pathParams.put("class_id", valid_fullPayment_class);
    }
    @Then("The Response Should Contain Status Code 403 And Error Message Unauthorized")
    public void Validate_Response_unauthorized_student (){
        Response PayForFullClass = pay_for_full_class;
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
    @Given("student's wallet does not have sufficient funds for full class")
    public void Insufficient_wallet_for_full_class() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", expensive_class);
    }
    @Then("The Response Should Contain Status Code 400 And Error Message Insufficient balance for full class")
    public void Validate_Response_Insufficient_wallet_for_full_class (){
        Response PayForFullClass = pay_for_full_class;
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_BAD_REQUEST,"Insufficient wallet balance for full class payment.",4008);
    }
    @Given("user try to enroll in class that not available or Archived")
    public void class_is_archived() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", Archived_Class);
    }
    @Then("The Response Should Contain Status Code 404 And Error Message Class not available.")
    public void Validate_Response_class_not_available(){
        Response PayForFullClass = pay_for_full_class;
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_NOT_FOUND,"Class not found or not eligible for display.",4046);
    }
    @Given("user try to enroll in class have full capacity")
    public void full_capacity_class() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", Full_Capacity_Class);
    }
    @Then("The Response Should Contain Status Code 400 And Error Message This class has reached full capacity.")
    public void Validate_Response_full_capacity_class(){
        Response PayForFullClass = pay_for_full_class;
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_BAD_REQUEST,"This class has reached full capacity, and no seats are currently open.",4006);
    }

}
