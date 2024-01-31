package TestReports;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/Features/StudentClasses"
                ,glue = "studentClasses"
                , plugin = {"pretty", "html:target/Reports/TestReport_StudentClasses.html"})

public class TestReport_StudentClasses extends AbstractTestNGCucumberTests {

}
