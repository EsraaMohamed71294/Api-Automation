package EducatorProfile;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import studentClasses.Database_Connection;
import studentClasses.TestBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class GetClass {
    TestBase test = new TestBase();
    CreateClass Class = new CreateClass();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    Long classID;
    Response Get_Class;


    @When("Performing the Api of Get Class")
    public void Get_Educator() {
        Get_Class = test.sendRequest("GET", "/admin/classes/{class_id}", null,data.Admin_Token);
    }
    @Given("User Send valid Class Id to get class data")
    public void user_send_valid_educatorId() {
        Class.Create_Class_per_session();
        classID = Class.Class_ID;
        pathParams.put("class_id", classID);
    }

    @And("Getting data of created class from database")
    public void getClassDetails () throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select  select * from classes c \n" +
                "join classes_subjects cs \n" +
                "on c.class_id = cs.class_id \n" +
                "join classes_educators ce \n" +
                "on c.class_id = ce.class_id " +
                "where c.class_id = "+ classID +" ");

        while (resultSet.next()) {

            //class_titl,class_description,meta_class_id,class_order,class_public_listing_date,class_public_delist_date,class_enrollment_end_date,class_archive_date,pay_full_class_allowed,pay_per_session_allowed,class_seats_limit,class_seats_reserved, from classes c
            classTitle = resultSet.getString("educator_first_name");
            classDescription = resultSet.getString("educator_last_name");
            educatorEmail = resultSet.getString("educator_email");
            System.out.println("Educator_first_name: " + educatorFirstName + "Educator_last_name: " + educatorLastName + "Educator_email: " + educatorEmail);
        }
    }
}
