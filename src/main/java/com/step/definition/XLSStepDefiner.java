package com.step.definition;

import com.business.xls.XLSOperator;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class XLSStepDefiner {
    private static XLSOperator operator = new XLSOperator();



    @Given("Read xls file provided in the FilePath interface")
    public void readXlsFileProvidedInTheFilePathInterface() {
        operator.readXLSFileAndSaveContentToMap();
    }

    @And("Select all the xls records that matches below condition")
    public void selectAllTheXlsRecordsThatMatchesBelowCondition() {
        operator.selectAllMatchingRecord();
    }

    @When("the xls column header {string} equals {string}")
    public void theXlsColumnHeaderEquals(String header, String value) {
        operator.selectRowWithHeaderValueEquals(header,value);
    }

    @Then("create a csv file {string} from xls file with fields {string}")
    public void createACsvFileFromXlsFileWithFields(String outputFilePath, String headers) {
        operator.filterExcelMapAndCreateCSVWithRequiredHeaders(outputFilePath,headers.split(","));
    }

    @Then("create a csv file {string} from xls file with fields {string} and an additional field {string}")
    public void createACsvFileFromXlsFileWithFieldsAndAnAdditionalField(String outputFilePath, String outputHeaders, String additionalParam) {
        operator.filterExcelMapAndCreateCSVWithRequiredHeadersAndAdditionalParameter(outputFilePath,additionalParam,outputHeaders.split(","));
    }


}
