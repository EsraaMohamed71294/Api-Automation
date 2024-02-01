package TestReports;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = {"src/test/resources/Features/AdminAreaFeatures/CreateEducator.feature",
                             "src/test/resources/Features/AdminAreaFeatures/GetEducator.feature",
                             "src/test/resources/Features/AdminAreaFeatures/CreateClass.feature",
                             "src/test/resources/Features/AdminAreaFeatures/GetClass.feature",
                             "src/test/resources/Features/AdminAreaFeatures/CreateSession.feature",
                             "src/test/resources/Features/AdminAreaFeatures/GetSession.feature",
                             "src/test/resources/Features/AdminAreaFeatures/CreateEducationalResource.feature",
                             "src/test/resources/Features/AdminAreaFeatures/AssignEducationalResources.feature"}

        ,glue = "AdminArea"
        ,plugin = {"pretty", "html:target/Reports/TestReport_AdminArea.html"})

public class TestReport_AdminArea extends AbstractTestNGCucumberTests {
}
