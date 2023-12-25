package TestReportt;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/main/resources/Features",glue = "NagwaClassesAPIs", plugin = {"pretty",
        "html:target/ Result report.html"
}
)

public class TestReport extends AbstractTestNGCucumberTests {

}
