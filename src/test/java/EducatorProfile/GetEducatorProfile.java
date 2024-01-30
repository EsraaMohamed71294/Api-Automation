package EducatorProfile;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import TestConfig.Database_Connection;
import TestConfig.TestBase;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.hasToString;
public class GetEducatorProfile {
    Educator_TestData data = new Educator_TestData();
    TestBase test = new TestBase();
    Database_Connection Connect = new Database_Connection();
    public String Educator_Id = data.educator_id;
    public  String NotActive_Educator = data.notActive_educator;
    public String Deleted_Educator = data.deleted_educator;
    public  String deleted_educator_token = data.refresh_token_for_deletedEducator;
     public String notActive_educator_token = data.refresh_token_for_notActiveEducator;
    public String Educator_refresh_token = data.refresh_token;
    public String educatorFirstName ;
    public String educatorLastName ;
    public String educatorEmail;
    public Response Get_Educator_Profile;
    public Response Deleted_Educator_token;
    public Response NotActive_Educator_token;
    public Response unauthorized_Educator;

    public Map<String, Object> pathParams = test.pathParams;


    public void get_educator_data_from_database() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select  educator_first_name, educator_last_name, educator_email from public.educators where educator_id =343256786543");

        while (resultSet.next()) {
            educatorFirstName = resultSet.getString("educator_first_name");
            educatorLastName = resultSet.getString("educator_last_name");
            educatorEmail = resultSet.getString("educator_email");
            System.out.println("Educator_first_name: " + educatorFirstName + "Educator_last_name: " + educatorLastName + "Educator_email: " + educatorEmail);
        }
    }

    public String getEducatorFirstName() {
        return educatorFirstName;
    }
    public String getEducatorLastName() {
        return educatorLastName;
    }
    public String getEducatorEmail() {
        return educatorEmail;
    }

    @Given("Getting the Educator Data From Database")
    public void Get_Educator_info_from_Database() throws SQLException {
        get_educator_data_from_database();
        educatorFirstName = getEducatorFirstName();
        educatorLastName = getEducatorLastName();
        educatorEmail = getEducatorEmail();
    }
    @When("Performing the Api of Get Educator Profile")
    public void Get_Educator_Profile() {
        Get_Educator_Profile = test.sendRequest("GET", "/educators/{educator_id}/profile", null,Educator_refresh_token);
    }
    @Given("User Send valid educator Id")
    public void Sending_valid_EducatorId() {
        pathParams.put("educator_id",Educator_Id);
    }
    @Then("I verify the appearance of status code 200 and Educator data returned")
    public void Validate_Response_of_Getting_Educator_Profile() {
        Get_Educator_Profile.prettyPrint();
        Get_Educator_Profile.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("/Users/esraamohamed/Api_Automation/src/test/resources/Schemas/EducatorProfileSchemas/EducatorProfile.json")))
                .body("educator_first_name", hasToString(educatorFirstName), "educator_last_name", hasToString(educatorLastName), "educator_email", hasToString(educatorEmail));
    }
    @Given("User Send Invalid educator Id")
    public void user_send_invalid_educatorId()
    {pathParams.put("educator_id", "34325678622222");
    }
    @Then("I verify the appearance of status code 400 and Educator Id not correct")
    public void Validate_Response_of_Invalid_EducatorId() {
        Response Educator_Profile = Get_Educator_Profile;
        test.Validate_Error_Messages(Educator_Profile,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }
    @Given("User Send not active educator")
    public void user_send_notActive_educatorId() {pathParams.put("educator_id", NotActive_Educator);
    }
    @When("performing the api with notActive educator token")
    public void send_notActive_educator_token(){
        NotActive_Educator_token = test.sendRequest("GET", "/educators/{educator_id}/profile", null,notActive_educator_token);
    }
    @Then("I verify the appearance of status code 404 and Educator Id is not active")
    public void Validate_Response_of_notActive_EducatorId() {
         Response InActiveEducator = NotActive_Educator_token;
         test.Validate_Error_Messages(InActiveEducator,HttpStatus.SC_NOT_FOUND,"Educator with the specified ID does not exist or is not active.",40413);
    }
    @Given("User Send deleted educator")
    public void user_send_deleted_educatorId() {pathParams.put("educator_id", Deleted_Educator);
    }
    @When("performing the api with deleted educator token")
    public void send_deleted_educator_token(){
        Deleted_Educator_token = test.sendRequest("GET", "/educators/{educator_id}/profile", null,deleted_educator_token);
    }
    @Then("I verify the appearance of status code 404 and Educator Id is deleted")
    public void Validate_Response_of_deleted_EducatorId() {
        Response DeletedEducator =Deleted_Educator_token;
        test.Validate_Error_Messages(DeletedEducator,HttpStatus.SC_NOT_FOUND,"Educator with the specified ID does not exist or is not active.",40413);
    }
    @Given("User Send unauthorized educator")
    public void user_send_unauthorized_educatorId() {pathParams.put("educator_id",Educator_Id);
    }
    @When("performing the api with invalid token")
    public void send_unauthorized_educator(){
        unauthorized_Educator = test.sendRequest("GET", "/educators/{educator_id}/profile", null, notActive_educator_token);
    }
    @Then("I verify the appearance of status code 403 and Educator is unauthorized")
    public void Validate_Response_of_unauthorized_EducatorId() {
        Response unauthorizedEducator = unauthorized_Educator;
        test.Validate_Error_Messages(unauthorizedEducator,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

}
