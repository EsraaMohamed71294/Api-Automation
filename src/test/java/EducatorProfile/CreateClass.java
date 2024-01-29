package EducatorProfile;

import com.github.javafaker.Faker;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import studentClasses.TestBase;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class CreateClass {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    CreateEducator educator = new CreateEducator();
    Faker fakeDate =new Faker();
    String classTitle = fakeDate.name().title();
    Long EducatorId;
    Long Class_ID;
    Response Create_class_per_session;
    Response unauthorized_user;
    Response Invalid_body_data;
    String valid_body = "{\"class_title\":\""+ classTitle +"\",\"meta_class_id\":123123123123,\"class_order\":1," +
            "\"class_description\":\"This class provides an introduction to programming concepts.\",\"class_public_listing_date\":\"2024-01-10T00:00:00Z\"," +
            "\"class_public_delist_date\":\"2028-02-10T00:00:00Z\",\"class_enrollment_end_date\":\"2025-02-05T23:59:59Z\",\"class_archive_date\":\"2026-03-01T00:00:00Z\"," +
            "\"pay_full_class_allowed\":false,\"pay_per_session_allowed\":true,\"class_seats_limit\":50,\"is_test_class\":true,\"subjects\":[{\"subject_id\":872198392582," +
            "\"class_subject_full_price\":2},{\"subject_id\":659167252305,\"class_subject_full_price\":1}],\"educators\":[]}" ;
    String invalid_body = "{\"class_title\":\"\",\"meta_class_id\":,\"class_order\":1," +
            "\"class_description\":\"This class provides an introduction to programming concepts.\",\"class_public_listing_date\":\"2024-01-10T00:00:00Z\"," +
            "\"class_public_delist_date\":\"\",\"class_enrollment_end_date\":\"\",\"class_archive_date\":\"\"," +
            "\"pay_full_class_allowed\":false,\"pay_per_session_allowed\":true,\"class_seats_limit\":50,\"is_test_class\":true,\"subjects\":[{\"subject_id\":872198392582," +
            "\"class_subject_full_price\":2},{\"subject_id\":659167252305,\"class_subject_full_price\":1}],\"educators\":[]}" ;



    @Given("Performing the Api of Create class full pay With valid data")
    public Long Create_Class_full_pay() {
        educator.Create_Educator();
        EducatorId = educator.Educator_ID;
        String body_for_pay_full_class = "{\"class_title\":\""+ classTitle +"\",\"meta_class_id\":123123123123,\"class_order\":1," +
                "\"class_description\":\"This class provides an introduction to programming concepts.\",\"class_public_listing_date\":\"2024-01-10T00:00:00Z\"," +
                "\"class_public_delist_date\":\"2028-02-10T00:00:00Z\",\"class_enrollment_end_date\":\"2025-02-05T23:59:59Z\",\"class_archive_date\":\"2026-03-01T00:00:00Z\"," +
                "\"pay_full_class_allowed\":true,\"pay_per_session_allowed\":false,\"class_seats_limit\":50,\"is_test_class\":true,\"subjects\":[{\"subject_id\":793174170262," +
                "\"class_subject_full_price\":2},{\"subject_id\":708151485254,\"class_subject_full_price\":1}],\"educators\":["+ EducatorId +"]}" ;

        Create_class_per_session = test.sendRequest("POST", "/admin/classes", body_for_pay_full_class, data.Admin_Token);
        return Class_ID = Create_class_per_session.then().extract().path("class_id");
    }

    @Given("Performing the Api of Create class per session With valid data")
    public Long Create_Class_per_session() {
        educator.Create_Educator();
        EducatorId = educator.Educator_ID;
        String body_for_per_session_class = "{\"class_title\":\""+ classTitle +"\",\"meta_class_id\":123123123123,\"class_order\":1," +
                "\"class_description\":\"This class provides an introduction to programming concepts.\",\"class_public_listing_date\":\"2024-01-10T00:00:00Z\"," +
                "\"class_public_delist_date\":\"2028-02-10T00:00:00Z\",\"class_enrollment_end_date\":\"2025-02-05T23:59:59Z\",\"class_archive_date\":\"2026-03-01T00:00:00Z\"," +
                "\"pay_full_class_allowed\":false,\"pay_per_session_allowed\":true,\"class_seats_limit\":50,\"is_test_class\":true,\"subjects\":[{\"subject_id\":793174170262," +
                "\"class_subject_full_price\":2},{\"subject_id\":708151485254,\"class_subject_full_price\":1}],\"educators\":["+ EducatorId +"]}" ;

        Create_class_per_session = test.sendRequest("POST", "/admin/classes", body_for_per_session_class, data.Admin_Token);
        return Class_ID = Create_class_per_session.then().extract().path("class_id");
    }

    @Then("I verify the appearance of status code 200 and class created successfully")
    public void Validate_Response_of_create_class_successfully() {
        Create_class_per_session.prettyPrint();
        Create_class_per_session.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorProfileSchemas/CreateClass.json")))
                .body("message", hasToString("Class created successfully."),"class_id",equalTo(Class_ID));
    }

    @Given("Performing the Api of Create class With invalid token")
    public void Create_class_with_invalid_token() {
        unauthorized_user = test.sendRequest("POST", "/admin/classes", valid_body,data.refresh_token_for_notActiveEducator);
    }

    @Then("I verify the appearance of status code 403 and user is not authorized")
    public void Validate_Response_of_unauthorized_userId(){
        Response unauthorizedUser = unauthorized_user;
        test.Validate_Error_Messages(unauthorizedUser,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

    @Given("Performing the Api of Create class With invalid body")
    public void Create_class_with_invalid_body() {
        Invalid_body_data = test.sendRequest("POST", "/admin/classes", invalid_body,data.refresh_token_for_notActiveEducator);
    }

    @Then("I verify the appearance of status code 400 and body incorrect")
    public void Validate_Response_of_unauthorized_EducatorId(){
        Response invalidBodyData = Invalid_body_data;
        test.Validate_Error_Messages(invalidBodyData,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }



}
