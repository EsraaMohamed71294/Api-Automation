package TestReport;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


    @CucumberOptions(features = "src/test/resources/Features/EducatorProfileFeatures"
                    ,glue = "EducatorProfile"
                    ,plugin = {"pretty", "html:target/TestReport_EducatorProfile.html"})

    public class TestReport_EducatorProfile extends AbstractTestNGCucumberTests {

    }




