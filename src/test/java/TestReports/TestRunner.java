package TestReports;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = {"src/test/resources/Features/AdminArea"}
                ,glue = "AdminArea"
                ,plugin = {"pretty", "html:target/Reports/TestRunner.html"})

public class TestRunner extends AbstractTestNGCucumberTests {

}
