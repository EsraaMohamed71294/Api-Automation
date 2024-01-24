package TestReport;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/Features/StudentClassesFeatures"
                ,glue = "studentClasses"
                , plugin = {"pretty", "html:target/TestReport_StudentClasses.html"})

public class TestReport_StudentClasses extends AbstractTestNGCucumberTests {

}