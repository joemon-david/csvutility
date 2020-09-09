package com.step.definition;

import com.business.csv.CSVOperator;
import cucumber.api.java.en.Given;

public class CSVStepDefiner {

    @Given("Read file {string} to get all records where coulumn {string} equals {string} and create file {string} with columns {string}")
    public void read_file_to_get_all_records_where_coulumn_equals_and_create_file_with_columns(String inputFilePath, String header, String matchingValue, String outputFilePath, String outputColumns) {
        // Write code here that turns the phrase above into concrete actions
        CSVOperator operator = new CSVOperator();

        operator.extractContentAndWrite(inputFilePath,outputFilePath,header,matchingValue,outputColumns.split("\\,"));
    }
}
