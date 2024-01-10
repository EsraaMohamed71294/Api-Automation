package TestReport;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/Features",glue = "studentClasses", plugin = {"pretty",
        "html:target/ TestReport.html"
}
)

public class TestReport extends AbstractTestNGCucumberTests {

}
