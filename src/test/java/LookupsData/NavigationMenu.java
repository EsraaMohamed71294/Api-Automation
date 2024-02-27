package LookupsData;

import ParentAccount.Parent_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.Matchers.*;

public class NavigationMenu {

    TestBase test = new TestBase();
    Database_Connection Connect = new Database_Connection();
    Parent_TestData data = new Parent_TestData();
    Response Navigation_Menu;
    Long language_id ;
    String language_name;
    String language_iso_code;
    Integer language_order;
    Long country_id;
    String country_iso_code;
    String country_currency_iso_code;
    String country_localization_key;
    Integer stage_id;
    String stage_localization_key;
    String stage_color;
    String stage_url_text;
    Integer stage_order;



    @Given("Performing the Api of Navigation Menu")
    public void Navigation_Menu() {
        Navigation_Menu = test.sendRequest("GET", "/navigation-menu", null, data.Parent_refreshToken);
    }

    @And("Getting Countries ,stages and languages  from database")
    public void getCountries_Stages_Languages () throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("  select * from countries c \n" +
                " join stages s \n" +
                " on s.country_id = c.country_id \n" +
                " where c.country_iso_code = 'eg' and s.stage_order = 3");

        while (resultSet.next()) {
            country_id = resultSet.getLong("country_id");
            country_iso_code = resultSet.getString("country_iso_code");
            country_localization_key = resultSet.getString("country_localization_key");
            country_currency_iso_code = resultSet.getString("country_currency_iso_code");
            stage_id = resultSet.getInt("stage_id");
            stage_localization_key = resultSet.getString("stage_localization_key");
            stage_localization_key = resultSet.getString("stage_localization_key");
            stage_url_text = resultSet.getString("stage_url_text");
            stage_order = resultSet.getInt("stage_order");
        }

        ResultSet languages = Connect.connect_to_database(" select * from languages l where language_order =2");

        while (resultSet.next()) {
            language_id = resultSet.getLong("language_name");
            language_name = resultSet.getString("language_name");
            language_iso_code = resultSet.getString("language_iso_code");
            language_order = resultSet.getInt("language_order");
        }
    }

    @Then("I verify the appearance of status code 200 and Menu returned successfully")
    public void Validate_Response_of_Navigation_Menu() {
        Navigation_Menu.prettyPrint();
        Navigation_Menu.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/LookupsData/NavigationMenu.json")))
                .body("languages.language_id", equalTo(language_id),"languages.language_name",hasToString(language_name),
                        "languages.language_iso_code",hasToString(language_iso_code),"languages.language_order",equalTo(language_order))
                .body("countries.country_id", equalTo(country_id),"countries.country_iso_code",hasToString(country_iso_code),
                        "countries.country_currency_iso_code",hasToString(country_currency_iso_code),"countries.country_localization_key",hasToString(country_localization_key))
                .body("countries.stages.stage_id", equalTo(stage_id),"countries.stages.stage_localization_key",hasToString(stage_localization_key),
                        "countries.stages.stage_color",hasToString(stage_color),"countries.stages.stage_url_text",hasToString(stage_url_text),"countries.stages.stage_order",equalTo(stage_order));
    }

}
