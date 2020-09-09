package com.runner;


import com.helper.csv.ReportingUtility;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/resources/csv/",
        glue= {"com.step.definition"},
        plugin= {"pretty","html:target","json:target/cucumber.json"},
        monochrome=true,
        strict=true,
        dryRun=false,
        tags= {"@RunNow"}
)
public class TestRunner {

    @AfterClass
    public static void teardown()
    {
        ReportingUtility.generateReport();
    }
}
