package com.business.csv;

import com.tool.csv.CSVMatcher;
import com.tool.csv.CSVReader;
import com.tool.csv.CSVWriter;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CSVOperator {
    static CSVReader reader = new CSVReader();
    static CSVWriter writer = new CSVWriter();


    public void extractContentAndWrite(String relativeInputFilePath,String relativeOutputFilePath,String conditionHeader,String matchValue,String ... outputHeaders)
    {
        List<CSVRecord> recordList = reader.getMatchingRecordList(relativeInputFilePath,conditionHeader,matchValue);
        CSVPrinter printer = writer.createCSVPrinter(relativeOutputFilePath,outputHeaders);
        writer.writeCSVPrinter(reader.getFilteredOutColumnPrinter(recordList,printer,outputHeaders));
    }

    public void writeCSVRecordListWithRequiredHeaders(List<CSVRecord>  recordList,String relativeOutputFilePath,String ... outputHeaders)
    {
        CSVPrinter printer = writer.createCSVPrinter(relativeOutputFilePath,outputHeaders);
        writer.writeCSVPrinter(reader.getFilteredOutColumnPrinter(recordList,printer,outputHeaders));
    }


    public void compareTwoCSVFile(String relativeInputFile1Path,String relativeInputFile2Path)
    {
        CSVReader reader = new CSVReader();
        List<CSVRecord> recordList1 = reader.getAllRecordList(relativeInputFile1Path);
        List<CSVRecord> recordList2 = reader.getAllRecordList(relativeInputFile2Path);

        List<CSVRecord> recordList;
        List<CSVRecord> recordListNxt;

        int record1Size=recordList1.size();
        int record2Size=recordList2.size();
        if(record1Size == record2Size)
        {
            System.out.println("Two files having the same number of rows");
            recordList=recordList1;
            recordListNxt=recordList2;
        }else
        {
            System.out.println("Files have different sizes. "+relativeInputFile1Path+" have "+(record1Size-1)+" rows , and "+relativeInputFile2Path+" have "+(record2Size-1));
            recordList = (record1Size > record2Size)? recordList2:recordList1;
            recordListNxt = (record1Size < record2Size)? recordList2:recordList1;
        }

        List<CSVRecord> finalRecordListNxt = recordListNxt;
        recordList.forEach((record1) ->
        {
            long recordRowNumber = record1.getRecordNumber();
            CSVRecord record2= finalRecordListNxt.get((int) recordRowNumber);
                AtomicInteger record1ItemLoop= new AtomicInteger();
                record1.forEach((item1) -> {
                   int recordCurItemNum = record1ItemLoop.getAndIncrement();
                        String item2 = record2.get(recordCurItemNum);
                        try {
                            if(!item1.equalsIgnoreCase(item2))
                                System.out.println("Item mismatch found at Row number "+recordRowNumber+" and at the column index "+recordCurItemNum+ " ("+item1+" -> "+item2+")");

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                });



        });

    }




    public static void main(String[] args) {

        CSVWriter writer = new CSVWriter();
        CSVReader reader = new CSVReader();
        CSVOperator operator = new CSVOperator();
//        operator.compareTwoCSVFile("data//output//benin.csv","data//output//iraq.csv");
//        operator.extractContentAndWrite("data//input//people.csv","data//output//benin.csv","Country","norway","Name","Age","Date of Birth","Country");

        List<CSVRecord> recordList=CSVMatcher.getInstance("data//input//people.csv").getAllMatchingRecord().where().headerValueEquals("age",36)
        .and().headerValueEquals("Country","Singapore").build();

        CSVPrinter printer = writer.createCSVPrinter("data//output//benin2.csv","Name","Age","Phone","email","City","Country");
        writer.writeCSVPrinter(reader.getFilteredOutColumnPrinter(recordList,printer,"Name","Age","Phone","email","City","Country"));
    }
}
