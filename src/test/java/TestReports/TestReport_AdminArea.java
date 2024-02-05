package TestReports;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = {"src/test/resources/Features/AdminArea/CreateEducator.feature",
                             "src/test/resources/Features/AdminArea/GetEducator.feature",
                             "src/test/resources/Features/AdminArea/CreateClass.feature",
                             "src/test/resources/Features/AdminArea/GetClass.feature",
                             "src/test/resources/Features/AdminArea/CreateSession.feature",
                             "src/test/resources/Features/AdminArea/GetSession.feature",
                             "src/test/resources/Features/AdminArea/CreateEducationalResource.feature",
                             "src/test/resources/Features/AdminArea/AssignEducationalResources.feature"}

        ,glue = "AdminArea"
        ,plugin = {"pretty", "html:target/Reports/TestReport_AdminArea.html"})

public class TestReport_AdminArea extends AbstractTestNGCucumberTests {
}
