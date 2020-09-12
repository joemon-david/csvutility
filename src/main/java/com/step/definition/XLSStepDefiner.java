package com.step.definition;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class XLSStepDefiner {
    @Given("Read xls file {string}")
    public void readXlsFile(String arg0) {
    }

    @And("Select all the xls records that matches below condition")
    public void selectAllTheXlsRecordsThatMatchesBelowCondition() {
    }

    @When("the xls column header {string} equals {string}")
    public void theXlsColumnHeaderEquals(String arg0, String arg1) {
    }

    @Then("create a csv file {string} from xls file with fields {string}")
    public void createACsvFileFromXlsFileWithFields(String arg0, String arg1) {
    }
}
