package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.TestBase;
import com.github.javafaker.Faker;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;

public class EditClass {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    CreateEducator educator = new CreateEducator();
    Faker fakeDate =new Faker();
    String classTitle = fakeDate.name().title();
    Long EducatorId;
    Long Class_ID;
    Response unauthorized_user;
    Response Invalid_body_data;
    Response Edit_class;
    String body_for_pay_full_class ;
    String invalid_body ="{\"class_title\":\""+ classTitle +"\",\"meta_class_id\":123123123123,\"class_order\":1," +
            "\"class_description\":\"This class provides an introduction to programming concepts.\",\"class_public_listing_date\":\"2024-01-10T00:00:00Z\"," +
            "\"class_public_delist_date\":\"2024-02-10T00:00:00Z\",\"class_enrollment_end_date\":\"2024-02-05T23:59:59Z\",\"class_archive_date\":\"2024-10-01T00:00:00Z\"," +
            "\"class_payment_option_id\":1,\"class_block_count\":2,\"class_seats_limit\":50,\"is_test_class\":true," +
            "\"subjects\":[{\"subject_id\":793174170262,\"class_subject_retail_price\":30,\"class_subject_discounted_price\":30,\"class_subject_session_price\":10," +
            "\"blocks\":[]},{\"subject_id\":787192832597,\"class_subject_retail_price\":30,\"class_subject_discounted_price\":20,\"class_subject_session_price\":10," +
            "\"blocks\":[]}],\"educators\":[{\"educator_id\":"+ EducatorId +",\"educator_order\":20}]}" ;



    @Given("Performing the Api of Edit class full pay With valid data")
    public Long Edit_Class_full_pay() {
        educator.Create_Educator();
        EducatorId = educator.Educator_ID;
        System.out.println("educator " +EducatorId);
        System.out.println("class" +classTitle);
        body_for_pay_full_class = "{\"class_title\":\""+ classTitle +"\",\"meta_class_id\":123123123123,\"class_order\":1," +
                "\"class_description\":\"This class provides an introduction to programming concepts.\",\"class_public_listing_date\":\"2024-01-10T00:00:00Z\"," +
                "\"class_public_delist_date\":\"2024-02-10T00:00:00Z\",\"class_enrollment_end_date\":\"2024-02-05T23:59:59Z\",\"class_archive_date\":\"2024-10-01T00:00:00Z\"," +
                "\"class_payment_option_id\":1,\"class_block_count\":0,\"class_seats_limit\":50,\"is_test_class\":true," +
                "\"subjects\":[{\"subject_id\":793174170262,\"class_subject_retail_price\":30,\"class_subject_discounted_price\":30,\"class_subject_session_price\":10," +
                "\"blocks\":[]},{\"subject_id\":787192832597,\"class_subject_retail_price\":30,\"class_subject_discounted_price\":20,\"class_subject_session_price\":10," +
                "\"blocks\":[]}],\"educators\":[{\"educator_id\":"+ EducatorId +",\"educator_order\":20}]}" ;

        Edit_class = test.sendRequest("POST", "/admin/classes", body_for_pay_full_class, data.Admin_Token);
        return Class_ID = Edit_class.then().extract().path("class_id");
    }
}
