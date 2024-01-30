package EducatorProfile;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import TestConfig.TestBase;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;


import static org.hamcrest.Matchers.hasToString;

public class UpdateEducatorProfile {
    Educator_TestData data = new Educator_TestData();
    TestBase test = new TestBase();
    GetEducatorProfile profile = new GetEducatorProfile();

    Map<String, Object> pathParams = test.pathParams;
    String Educator_Id = data.educator_id;
    String Valid_body_request = "{\"educator_first_name\":\"Test\",\"educator_last_name\":\"Account\"}";
    String body_with_special_char = "{\"educator_first_name\":\"spec!@L\",\"educator_last_name\":\"Ch@r\"}";
    String Empty_fields_of_body = "{\"educator_first_name\":\"\",\"educator_last_name\":\"\"}";
    String NotActive_Educator = data.notActive_educator;
    String Deleted_Educator = data.deleted_educator;
    String deleted_educator_token = data.refresh_token_for_deletedEducator;
    String notActive_educator_token = data.refresh_token_for_notActiveEducator;
    String Educator_refresh_token = data.refresh_token;
    public Response Update_Educator_Profile;
    public Response NotActive_Educator_token;
    public Response unauthorized_Educator;
    public Response Deleted_Educator_token;

    @When("Performing the Api of Update Educator Profile with valid data")
    public void Update_Educator_Profile() {
        Update_Educator_Profile = test.sendRequest("PATCH", "/educators/{educator_id}/profile", Valid_body_request,Educator_refresh_token);
    }
    @Given("User Send Invalid educator Id for update")
    public void user_send_invalid_educatorId() {pathParams.put("educator_id", "34325678622222");
    }

    @Then("I verify the appearance of status code 400 and Educator Id is  incorrect")
    public void Validate_Response_of_Invalid_EducatorId() {
        Response Educator_Profile = Update_Educator_Profile;
        test.Validate_Error_Messages(Educator_Profile,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }
    @And("validate data saved successfully into db")
    public void validate_educator_data_update_into_db() throws SQLException {
        profile.Get_Educator_info_from_Database();
        profile.educatorFirstName.contains("Test") ;
        profile.educatorLastName.contains("Account");
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

    @And("User Send valid educator Id for update")
    public void Sending_valid_EducatorId() {
        pathParams.put("educator_id", Educator_Id);
    }

    @Given("User Send unauthorized educator for update")
    public void user_send_unauthorized_educatorId() {pathParams.put("educator_id",Educator_Id);
    }

    @When("performing the api of update with invalid token")
    public void send_unauthorized_educator(){
        unauthorized_Educator = test.sendRequest("PATCH", "/educators/{educator_id}/profile", Valid_body_request, notActive_educator_token);
    }
    @Then("I verify the appearance of status code 403 and Educator unauthorized")
    public void Validate_Response_of_unauthorized_EducatorId() {
        Response unauthorizedEducator = unauthorized_Educator;
        test.Validate_Error_Messages(unauthorizedEducator,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
    @Given("User Send not active educator for update")
    public void user_send_notActive_educatorId() {pathParams.put("educator_id", NotActive_Educator);
    }
    @When("performing the api of update with notActive educator token")
    public void send_notActive_educator_token(){
        NotActive_Educator_token = test.sendRequest("PATCH", "/educators/{educator_id}/profile", Valid_body_request,notActive_educator_token);
    }
    @Then("I verify the appearance of status code 404 and Educator Id is deactivated")
    public void Validate_Response_of_notActive_EducatorId() {
        Response InActiveEducator = NotActive_Educator_token;
        test.Validate_Error_Messages(InActiveEducator,HttpStatus.SC_NOT_FOUND,"Educator with the specified ID does not exist or is not active.",40413);
    }
    @Given("User Send deleted educator for update")
    public void user_send_deleted_educatorId() {pathParams.put("educator_id", Deleted_Educator);
    }
    @When("performing the api of update with deleted educator token")
    public void send_deleted_educator_token(){
        Deleted_Educator_token = test.sendRequest("PATCH", "/educators/{educator_id}/profile", Valid_body_request,deleted_educator_token);
    }
    @Then("I verify the appearance of status code 404 and Educator Id deleted")
    public void Validate_Response_of_deleted_EducatorId() {
        Response DeletedEducator =Deleted_Educator_token;
        test.Validate_Error_Messages(DeletedEducator,HttpStatus.SC_NOT_FOUND,"Educator with the specified ID does not exist or is not active.",40413);
    }


}
