package EducatorProfile;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import studentClasses.Database_Connection;
import studentClasses.TestBase;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class GetClass {
    TestBase test = new TestBase();
    CreateClass Class = new CreateClass();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    Long classID;
    String classTitle;
    String classDescription;
    String metaClassID;
    String classOrder;
    String classPublicListing;
    String classPublicDelistDate;
    String classEnrollmentEndDate;
    String classArchiveDate;
    String payFullClassAllowed;
    String pay_per_session_allowed;
    String class_seats_limit;
    String class_seats_reserved;
    Response Get_Class;
    Response GetClass_Invalid_token;


    @When("Performing the Api of Get Class")
    public void Get_Educator() {
        Get_Class = test.sendRequest("GET", "/admin/classes/{class_id}", null,data.Admin_Token);
    }
    @Given("User Send valid Class Id to get class data")
    public void user_send_valid_classId() {
        Class.Create_Class_per_session();
        classID = Class.Class_ID;
        System.out.println(classID);
        pathParams.put("class_id", classID);
    }

    @And("Getting data of created class from database")
    public void getClassDetails () throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from classes c \n" +
                "join classes_subjects cs \n" +
                "on c.class_id = cs.class_id \n" +
                "join classes_educators ce \n" +
                "on c.class_id = ce.class_id " +
                "where c.class_id = "+ classID +"");

        while (resultSet.next()) {
            classTitle = resultSet.getString("class_title");
            classDescription = resultSet.getString("class_description");
            metaClassID = resultSet.getString("meta_class_id");
            classOrder = resultSet.getString("class_order");
            classPublicListing = resultSet.getString("class_public_listing_date");
            classPublicDelistDate = resultSet.getString("class_public_delist_date");
            classEnrollmentEndDate = resultSet.getString("class_enrollment_end_date");
            classArchiveDate = resultSet.getString("class_archive_date");
            payFullClassAllowed = resultSet.getString("pay_full_class_allowed");
            pay_per_session_allowed = resultSet.getString("pay_per_session_allowed");
            class_seats_limit = resultSet.getString("class_seats_limit");
            class_seats_reserved = resultSet.getString("class_seats_reserved");
            System.out.println("class Title: " + classTitle + "pay_per_session_allowed: " + pay_per_session_allowed + "class_seats_limit: " + class_seats_limit);
        }
    }

    @Then("I verify the appearance of status code 200 and class data returned successfully")
    public void Validate_Response_of_Get_Class() {
        Get_Class.prettyPrint();
        Get_Class.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorProfileSchemas/GetClass.json")))
                .body("class_id", equalTo(classID),"class_title",hasToString(classTitle),"class_description",hasToString(classDescription),"meta_class_id",hasToString(metaClassID),
                        "class_order",hasToString(classOrder),"class_public_listing_date",hasToString(classPublicListing),"class_public_delist_date",hasToString(classPublicDelistDate),
                        "class_enrollment_end_date",hasToString(classEnrollmentEndDate),"class_archive_date",hasToString(classArchiveDate),
                        "pay_full_class_allowed",hasToString(payFullClassAllowed),"pay_per_session_allowed",hasToString(pay_per_session_allowed),
                        "class_seats_limit",hasToString(class_seats_limit),"class_seats_reserved",hasToString(class_seats_reserved));
    }

    @Given("User Send Invalid Class Id to get class data")
    public void user_send_Invalid_classId() {
        pathParams.put("class_id", "123456789123456");
    }

    @Then("I verify the appearance of status code 400 and class is not valid")
    public void Validate_Response_of_Invalid_class() {
        Response Invalid_classID = Get_Class;
        test.Validate_Error_Messages(Invalid_classID,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

    @Given("User Send Class Id is not found")
    public void user_send_notFound_classId() {
        pathParams.put("class_id", "567890123456");
    }

    @Then("I verify the appearance of status code 400 and class is not found")
    public void Validate_Response_of_notFound_class() {
        Response notFound_classID = Get_Class;
        test.Validate_Error_Messages(notFound_classID,HttpStatus.SC_NOT_FOUND,"Class not found or not eligible for display.",4046);
    }

    @Given("User send invalid admin token to GetClasses")
    public void send_unauthorized_Admin(){
        GetClass_Invalid_token = test.sendRequest("GET", "/admin/classes/{class_id}", null, data.refresh_token_for_notActiveEducator);
    }
    @Then("I verify the appearance of status code 403 and Admin is unauthorized")
    public void Validate_Response_of_unauthorized_EducatorId() {
        Response unauthorizedAdmin = GetClass_Invalid_token;
        test.Validate_Error_Messages(unauthorizedAdmin,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
}
