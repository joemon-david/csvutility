package com.tool.csv;

import cucumber.api.Scenario;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CSVWriter {


    public void writeCSVRecordList(String relativeOutputFilePath,List<CSVRecord> csvRecordList,String ... headers)
    {
        try {

            Writer writer = Files.newBufferedWriter(Paths.get(relativeOutputFilePath));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers));
            csvPrinter.printRecord(csvRecordList);
            csvPrinter.flush();
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public void writeCSVPrinter(CSVPrinter printer)
    {
        try {
            printer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public CSVPrinter createCSVPrinter(String relativeOutputFilePath,String ... headers)
    {
        CSVPrinter csvPrinter=null;
        try {

            Writer writer = Files.newBufferedWriter(Paths.get(relativeOutputFilePath));
             csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers));
        }catch (Exception e){
            e.printStackTrace();

        }
        return csvPrinter;
    }


}
