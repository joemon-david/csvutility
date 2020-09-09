package com.tool.csv;

import com.helper.csv.CSVFilter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {


    public List<CSVRecord> getMatchingRecordList(String relativeCSVFilePath, String headerName, String matchValue)
    {
        List<CSVRecord> matchingCSVRecordList = new ArrayList<CSVRecord>();
        try {

            BufferedReader reader = Files.newBufferedReader(Paths.get(relativeCSVFilePath));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());


            for(CSVRecord csvRecord : csvParser)
            {
               String headerValue = csvRecord.get(headerName);
                if(headerValue.equalsIgnoreCase(matchValue))
                {
                    matchingCSVRecordList.add(csvRecord);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return matchingCSVRecordList;

    }


    public CSVPrinter getFilteredOutColumnPrinter(List<CSVRecord> fullColumnRecordList,CSVPrinter printer, String ... neededColumns)
    {

        try {
        for(CSVRecord record:fullColumnRecordList)

        {
            List<String> valueList = new ArrayList<String>();
            for(String column: neededColumns)
            {
                String columnValue = record.get(column);
                valueList.add(columnValue);

            }

                printer.printRecord(valueList);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return printer;
    }



   public List<CSVRecord> getAllRecordList(String relativeCSVFilePath)
   {
       List<CSVRecord> allCSVRecordList = new ArrayList<CSVRecord>();
       try {

           BufferedReader reader = Files.newBufferedReader(Paths.get(relativeCSVFilePath));
           CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withTrim());
           for(CSVRecord csvRecord : csvParser)
           {
               allCSVRecordList.add(csvRecord);
               
           }

   } catch (Exception e) {
        e.printStackTrace();
    }
       return allCSVRecordList;
   }


}
