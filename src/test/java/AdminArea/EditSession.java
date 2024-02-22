//package AdminArea;
//
//import EducatorProfile.Educator_TestData;
//import TestConfig.Database_Connection;
//import TestConfig.TestBase;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import io.restassured.response.Response;
//import org.apache.http.HttpStatus;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Map;
//import static org.hamcrest.Matchers.*;
//
//
//public class EditSession {
//    TestBase test = new TestBase();
//    Educator_TestData data = new Educator_TestData();
//    CreateEducator educator_data = new CreateEducator();
//    CreateSession session_data = new CreateSession();
//
//    Long new_educator_id;
//    Database_Connection connect = new Database_Connection();
//    public Map<String, Object> pathParams = test.pathParams;
//    Response Edit_Session;
//    Long not_exist_educatorId;
//    Long not_exist_session_id;
//    String valid_body;
//  @Given("Admin Send Valid SessionId And Body To EditSession API")
//    public void send_valid_data()throws SQLException {
//      session_data.Create_Session();
//      educator_data.Create_Educator();
//      pathParams.put("session_id",session_data.sessionId);
//      valid_body= "{\"educator_id\":"+educator_data.Educator_ID+"}";
//  }
//  @When("Performing The API of EditSession")
//    public void execute_edit_session_request(){
//      Edit_Session = test.sendRequest("PATCH","/admin/sessions/{session_id}",valid_body,data.Admin_Token);
//  }
//  @Then("Response code of EditSession is 200 and body returns with success message")
//    public void validate_success_response()throws SQLException{
//      Edit_Session.prettyPrint();
//
//      ResultSet resultSet = connect.connect_to_database("select s.educator_id from public.sessions s where s.session_id = "+session_data.sessionId);
//      while (resultSet.next()){
//          new_educator_id = resultSet.getLong("educator_id");
//      }
//      System.out.println(new_educator_id);
//      Edit_Session.then().assertThat()
//              .statusCode(HttpStatus.SC_OK)
//              .body("message",containsString("session updated successfully."));
//      assertEquals(educator_data.Educator_ID,new_educator_id);
//  }
//
//
//}
