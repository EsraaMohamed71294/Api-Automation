package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import com.github.javafaker.Faker;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class CreateEducationalResource {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    Database_Connection Connect = new Database_Connection();
    Faker fakeDate =new Faker();
    String name = fakeDate.name().name();
    String cdn = fakeDate.name().name();
    Long fileTypeID = Long.valueOf(fakeDate.number().randomDigitNotZero());
    Long resourceTypeID = Long.valueOf(fakeDate.number().randomDigitNotZero());
    Long resourceId;
    Response Create_Educational_Resources;
    Response Invalid_Educational_Resources;
    Response unauthorized_admin;

    String valid_body = "{\"name\":\""+ name +"\",\"cdn\":\""+ cdn +"\",\"bucket\":\"test_bucket_1\",\"key\":\"test_key_1\"," +
            "\"md5\":\"kkk\",\"is_test\":true,\"file_type_id\":"+ fileTypeID +",\"resource_type_id\":"+ resourceTypeID +"}" ;

    String Invalid_body = "{\"name\":\"\",\"cdn\":\"\",\"bucket\":\"test_bucket_1\",\"key\":\"test_key_1\"," +
            "\"md5\":\"\",\"is_test\":true,\"file_type_id\":,\"resource_type_id\":}" ;

    @Given("Performing the Api of Create Educational Resources")
    public Long  Create_new_educational_resources() {
        Create_Educational_Resources = test.sendRequest("POST", "/admin/educational-resources", valid_body, data.Admin_Token);
        return resourceId;
    }

    @And("Getting educational resource from database")
    public void  get_educational_resource_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from educational_resources er where educational_resources_name = \'"+ name +"\' and educational_resource_cdn = \'"+ cdn +"\'");
        while (resultSet.next()) {

            resourceId = resultSet.getLong("educational_resource_id");
        }
    }

    @Then("I verify the appearance of status code 200 and educational resource created successfully")
    public void Validate_Response_of_create_Educational_resource() {
        Create_Educational_Resources.prettyPrint();
        Create_Educational_Resources.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/CreateEducationalResource.json")))
                .body("message", hasToString("Educational resource created successfully."),"resource_id",equalTo(resourceId));
    }

    @Given("Performing the Api of Create Educational Resources with invalid data")
    public void  new_educational_resources_InvalidData() {
        Invalid_Educational_Resources = test.sendRequest("POST", "/admin/educational-resources", Invalid_body, data.Admin_Token);
    }

    @Then("I verify the appearance of status code 400 and body data is not correct")
    public void Validate_Response_of_Educational_resource_notValid() {
        Response invalidBodyData = Invalid_Educational_Resources;
        test.Validate_Error_Messages(invalidBodyData,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

    @Given("Performing the Api of Create educational resource With invalid token")
    public void Create_resources_with_invalid_token() {
        unauthorized_admin = test.sendRequest("POST", "/admin/classes", valid_body,data.refresh_token_for_notActiveEducator);
    }

    @Then("I verify the appearance of status code 403 and admin is not authorized")
    public void Validate_Response_of_unauthorized_userId(){
        Response unauthorizedUser = unauthorized_admin;
        test.Validate_Error_Messages(unauthorizedUser,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
}
