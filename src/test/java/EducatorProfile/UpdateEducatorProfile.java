package EducatorProfile;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import studentClasses.TestBase;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;


import static org.hamcrest.Matchers.hasToString;

public class UpdateEducatorProfile{
    Educator_TestData data = new Educator_TestData();
    TestBase test = new TestBase();
    GetEducatorProfile profile = new GetEducatorProfile();

    String Valid_body_request = "{\"educator_first_name\":\"Test\",\"educator_last_name\":\"Account\"}";
    String body_with_special_char = "{\"educator_first_name\":\"spec!@L\",\"educator_last_name\":\"Ch@r\"}";
    String Empty_fields_of_body = "{\"educator_first_name\":\"\",\"educator_last_name\":\"\"}";
    String Educator_refresh_token = data.refresh_token;
    public String educatorFirstName = profile.educatorFirstName ;
    public String educatorLastName = profile.educatorLastName; ;
    public Response Update_Educator_Profile;

    public Map<String, Object> pathParams = profile.pathParams;


    @When("Performing the Api of Update Educator Profile with valid data")
    public void Update_Educator_Profile() {
        Update_Educator_Profile = test.sendRequest("PATCH", "/educators/{educator_id}/profile", Valid_body_request,Educator_refresh_token);
//        profile.Sending_valid_EducatorId_to_api();
    }

    @And("validate data saved successfully into db")
    public void validate_educator_data_update_into_db() throws SQLException {
        profile.Get_Educator_info_from_Database();
        educatorFirstName.contains("Test") ;
        educatorLastName.contains("Account");
    }

    @Then("I verify the appearance of status code 200 and Educator data updated")
    public void Validate_Response_of_update_Educator_Profile_successfully() {
        Update_Educator_Profile.prettyPrint();
        Update_Educator_Profile.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/Api_Automation/src/test/resources/Schemas/EducatorProfileSchemas/UpdateEducatorProfile.json")))
                .body("message", hasToString("Profile updated successfully."));
    }

    @When("Performing the Api of Update Educator Profile with special char")
    public void Update_Educator_Profile_with_special_char() {
        Update_Educator_Profile = test.sendRequest("PATCH", "/educators/{educator_id}/profile", body_with_special_char,Educator_refresh_token);
    }

    @When("Performing the Api of Update Educator Profile with empty body")
    public void Update_Educator_Profile_with_empty_fields() {
        Update_Educator_Profile = test.sendRequest("PATCH", "/educators/{educator_id}/profile", Empty_fields_of_body,Educator_refresh_token);
    }

    @Then("I verify the appearance of status code 400 and Invalid request message")
    public void Validate_Response_of_update_Educator_with_EmptyFields() {
        Response Educator_With_Empty_Fields = Update_Educator_Profile;
        test.Validate_Error_Messages(Educator_With_Empty_Fields,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }


}
