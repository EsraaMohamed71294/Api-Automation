package EducatorProfile;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import studentClasses.TestBase;

import java.io.File;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class GetEducator {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    CreateEducator educator =new CreateEducator();
    String Admin_token = data.Admin_Token;
    Map<String, Object> pathParams = test.pathParams;
    Long educatorID;
    Response Get_Educator;

    @When("Performing the Api of Get Educator")
    public void Get_Educator() {
        Get_Educator = test.sendRequest("GET", "/admin/educators/{educator_id}", null,Admin_token);
    }
    @Given("User Send valid educator Id to get educator data")
    public void user_send_valid_educatorId() {
        educator.Create_Educator();
        System.out.println(educatorID = educator.Educator_ID);
        pathParams.put("educator_id", educatorID);
    }
    @Then("I verify the appearance of status code 200 and Educator data returned successfully")
    public void Validate_Response_of_Get_Educator() {
        Get_Educator.prettyPrint();
        Get_Educator.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorProfileSchemas/GetEducator.json")))
                .body("educator_first_name", hasToString(educator.firstName),"educator_last_name",hasToString(educator.lastName),"educator_email",hasToString(educator.email));
    }
}
