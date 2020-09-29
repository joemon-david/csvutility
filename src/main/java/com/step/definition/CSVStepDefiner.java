package com.step.definition;

import com.business.csv.CSVOperator;
import com.tool.csv.CSVMatcher;
import com.tool.csv.CSVReader;
import com.tool.csv.CSVWriter;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.csv.CSVRecord;

import java.util.List;

public class CSVStepDefiner {

    static CSVOperator operator = new CSVOperator();
    static CSVWriter writer = new CSVWriter();
    static CSVReader reader = new CSVReader();
    static CSVMatcher matcher;

    @Given("Read file {string} to get all records where coulumn {string} equals {string} and create file {string} with columns {string}")
    public void read_file_to_get_all_records_where_coulumn_equals_and_create_file_with_columns(String inputFilePath, String header, String matchingValue, String outputFilePath, String outputColumns) {
        // Write code here that turns the phrase above into concrete actions


        operator.extractContentAndWrite(inputFilePath,outputFilePath,header,matchingValue,outputColumns.split(","));
    }

    @Given("Read csv file {string}")
    public void read_csv_file(String relativeInputFilePath) {
        matcher=  new CSVMatcher();
        matcher.init(relativeInputFilePath);
    }

    @Given("Select all the records that matches below condition")
    public void select_all_the_records_that_matches_below_condition() {
        // Write code here that turns the phrase above into concrete actions
        matcher= matcher.getAllMatchingRecord();
    }

    @When("the header {string} equals {string}")
    public void the_header_equals(String header, Object value) {
        // Write code here that turns the phrase above into concrete actions

        matcher=matcher.headerValueEquals(header,value);

    }

    @Then("create a csv file {string} from above results with fields {string}")
    public void create_a_csv_file_from_above_results_with_fields(String relativeOutputFilePath, String outputColumns) {
        List<CSVRecord> recordList = matcher.build();
        operator.writeCSVRecordListWithRequiredHeaders(recordList,relativeOutputFilePath,outputColumns.split(","));
    }



}
