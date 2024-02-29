package TestReports;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = {"src/test/resources/Features/Nagwa.comAPIs"}
        ,glue = "Nagwa.comAPIs"
        ,plugin = {"pretty", "html:target/Reports/TestReport_NagwaAPI.html"})

public class TestReport_NagwaAPI extends AbstractTestNGCucumberTests {

}
