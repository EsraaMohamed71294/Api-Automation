package studentClasses;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.Map;

import static org.hamcrest.Matchers.*;

public class payForFullClass {
    TestBase test = new TestBase();
    TestData data = new TestData();
    String user_token = data.refresh_token;
    Database_Connection Connect = new Database_Connection();
    String student_Id = data.student_Id;
    String Full_Capacity_Class;
    String Archived_Class;
    Integer amount_paid_for_class;
    String class_currency;
    String valid_fullPayment_class;
    String expensive_class;

    String purchased_class;
    Map<String,Object> pathParams = test.pathParams;
    public Response pay_for_full_class ;

    public payForFullClass()throws SQLException{

        get_classes_data_from_database();
    }

    public void get_classes_data_from_database()throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from public.classes_students cs join classes c ON cs.class_id = c.class_id \n" +
                "join students s on cs.student_id = s.student_id join grades g\n" +
                "on s.grade_id = g.grade_id join stages s2 on g.stage_id = s2.stage_id join countries c2\n" +
                "on s2.country_id = c2.country_id\n" +
                "join public.students_wallets sw on cs.student_id = sw.student_id join\n" +
                "classes_subjects cs2 on cs.class_id = cs2.class_id left join students_access_rights sar \n" +
                "on sar.student_id = cs.student_id  \n" +
                "where cs.student_id ="+student_Id+" "+ "and \n" +
                " c.class_payment_option_id = 1 and cs2.class_subject_retail_price <= sw.student_wallet_balance and \n" +
                "cs.class_id not in \n" +
                "(select sar2.student_access_right_class_id from public.students_access_rights sar2 where sar2.student_id ="+student_Id+" "+")\n" +
                "and c.class_archive_date > now() and c.class_public_listing_date < now()");
        while (resultSet.next()) {
            valid_fullPayment_class = resultSet.getString("class_id");
            class_currency = resultSet.getString("country_currency_iso_code");
            if (resultSet.getString("class_subject_discounted_price") == null) {
                amount_paid_for_class = resultSet.getInt("class_subject_retail_price");
            } else {
                amount_paid_for_class = resultSet.getInt("class_subject_discounted_price");
            }
        }

        ResultSet resultSet_of_purchased_class = Connect.connect_to_database("    select * from public.classes_students cs join classes c ON cs.class_id = c.class_id \n" +
                " join students s on cs.student_id = s.student_id join grades g\n" +
                " on s.grade_id = g.grade_id join stages s2 on g.stage_id = s2.stage_id join countries c2\n" +
                " on s2.country_id = c2.country_id\n" +
                " join public.students_wallets sw on cs.student_id = sw.student_id join\n" +
                " classes_subjects cs2 on cs.class_id = cs2.class_id join students_access_rights sar \n" +
                " on sar.student_id = cs.student_id  and cs.class_id = sar.student_access_right_class_id  \n" +
                " where cs.student_id = 674143580350\n" +
                "and c.class_payment_option_id = 1 and cs2.class_subject_retail_price <=  sw.student_wallet_balance and cs.class_id in (select sar2.student_access_right_class_id from public.students_access_rights sar2 where sar2.student_id = 674143580350)\n" +
                " and c.class_archive_date > now() and c.class_public_listing_date < now()   ");
        while (resultSet_of_purchased_class.next()) {
            purchased_class = resultSet_of_purchased_class.getString("class_id");
        }

        ResultSet resultSet_of_complete_seats_class = Connect.connect_to_database("select * from public.classes_students cs join classes c ON cs.class_id = c.class_id \n" +
                " join students s on cs.student_id = s.student_id join grades g\n" +
                " on s.grade_id = g.grade_id join stages s2 on g.stage_id = s2.stage_id join countries c2\n" +
                " on s2.country_id = c2.country_id\n" +
                " join public.students_wallets sw on cs.student_id = sw.student_id join\n" +
                " classes_subjects cs2 on cs.class_id = cs2.class_id left join students_access_rights sar \n" +
                " on sar.student_id = cs.student_id  \n" +
                " where cs.student_id ="+student_Id+" "+"\n" +
                " and c.class_seats_limit = c.class_seats_reserved \n" +
                "and c.class_payment_option_id = 1 and cs2.class_subject_retail_price <= sw.student_wallet_balance and cs.class_id not in (select sar2.student_access_right_class_id from public.students_access_rights sar2 where sar2.student_id ="+student_Id+" "+") \n" +
                "   and c.class_archive_date > now() and c.class_public_listing_date < now()   \n" +
                " ");
        while (resultSet_of_complete_seats_class.next()) {
            Full_Capacity_Class = resultSet_of_complete_seats_class.getString("class_id");
        }

        ResultSet resultSet_of_archived_class = Connect.connect_to_database("select * from public.classes_students cs join classes c on cs.class_id = c.class_id \n" +
                "left join students_access_rights sar on cs.student_id = sar.student_id \n" +
                "where cs.student_id = 674143580350 and c.class_archive_date < now() and c.class_payment_option_id = 1 and cs.class_id not in\n" +
                "  (select sar2.student_access_right_class_id from public.students_access_rights sar2 where sar2.student_id = 674143580350) ");
        while (resultSet_of_archived_class.next()) {
            Archived_Class = resultSet_of_archived_class.getString("class_id");
        }
        ResultSet resultSet_of_expensive_class = Connect.connect_to_database("\n" +
                "    select * from public.classes_students cs join classes c ON cs.class_id = c.class_id \n" +
                " join students s on cs.student_id = s.student_id join grades g\n" +
                " on s.grade_id = g.grade_id join stages s2 on g.stage_id = s2.stage_id join countries c2\n" +
                " on s2.country_id = c2.country_id\n" +
                " join public.students_wallets sw on cs.student_id = sw.student_id join\n" +
                " classes_subjects cs2 on cs.class_id = cs2.class_id left join students_access_rights sar \n" +
                " on sar.student_id = cs.student_id  \n" +
                " where cs.student_id ="+student_Id+" "+"\n" +
                "and c.class_payment_option_id = 1 and cs2.class_subject_retail_price > sw.student_wallet_balance and cs.class_id not in (select sar2.student_access_right_class_id from public.students_access_rights sar2 where sar2.student_id = 674143580350)\n" +
                " and c.class_archive_date > now() and c.class_public_listing_date < now()   \n");
        while (resultSet_of_expensive_class.next()) {
            expensive_class = resultSet_of_expensive_class.getString("class_id");
        }

    }


    @When("Performing the Api of pay_for_full_class")
    public void pay_for_full_class() {
        pay_for_full_class =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/pay-full",null,user_token);
        System.out.println(valid_fullPayment_class +" " + Full_Capacity_Class);

    }

    @Given("User enrolled into fully paid class")
    public void successful_payment_for_full_class() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id",purchased_class);
    }
    @Then("I verify the appearance of status code 200 and Full class payment successful.")
    public void Validate_Response_of_success_payment_fullClass () {
        pay_for_full_class.prettyPrint();
        pay_for_full_class.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/payForFullClass.json")))
                .body("class_id" ,  hasToString(valid_fullPayment_class) ,"amount_paid",equalTo(amount_paid_for_class),"currency",hasToString(class_currency), "message" ,containsString("Full class payment successful."));
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
    @Given("user connect to database")
    public void get_data() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from public.classes c where c.class_id = 624185604856");
        while(resultSet.next()){
            String class_id = resultSet.getString("class_id");
		    System.out.println(class_id);

        }
    }

}
