package com.business.csv;

import com.tool.csv.CSVMatcher;
import com.tool.csv.CSVReader;
import com.tool.csv.CSVWriter;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

    public void writeCSVRecordListWithRequiredHeaders(ArrayList<ArrayList<String>> recordList,String relativeOutputFilePath,String ... outputHeaders)
    {
        CSVPrinter printer = writer.createCSVPrinter(relativeOutputFilePath,outputHeaders);

            recordList.forEach((list)->{
                try {
                    printer.printRecord(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        writer.writeCSVPrinter(printer);
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
        CSVRecord file1columnNames = recordList1.remove(0);
        CSVRecord file2columnNames = recordList2.remove(0);

        if(record1Size == record2Size)
        {
            System.out.println("Two files having the same number of rows");
            recordList=recordList1;
            recordListNxt=recordList2;
        }else
        {
            System.out.println("Files have different sizes. "+relativeInputFile1Path+" have "+(record1Size-1)+" rows , and "+relativeInputFile2Path+" have "+(record2Size-1));
            if(record1Size>record2Size)
            {
                recordList = recordList2;
                recordListNxt = recordList1;
            }else
            {
                recordList = recordList1;
                recordListNxt = recordList2;
            }

        }

//        List<CSVRecord> finalRecordListNxt = recordListNxt;
        ArrayList<HashMap> disparityMapList = new ArrayList<>();
        int recordRowNumber =0;

        for (CSVRecord record1 : recordList) {
            boolean isMismatchFound = false;
            HashMap<String,String> mismatchRecord = new HashMap<>();
//            long recordRowNumber = record1.getRecordNumber();
            CSVRecord record2 = recordListNxt.get( (recordRowNumber));
            int record1ItemLoop = 0;
            for (String item1 : record1) {
                String item2 = record2.get(record1ItemLoop);
                try {
                    if (!item1.equalsIgnoreCase(item2))
                    {
                        isMismatchFound = true;
                        mismatchRecord.put("rowNumber",""+recordRowNumber);
                        mismatchRecord.put("sourceColumn",file1columnNames.get(record1ItemLoop));
                        mismatchRecord.put("targetColumn",file2columnNames.get(record1ItemLoop));


                        System.out.println("Item mismatch found at Row number " + recordRowNumber + " and at the column index " + record1ItemLoop + " (" + item1 + " -> " + item2 + ")");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                record1ItemLoop++;
            }
            recordRowNumber++;

        }

    }




    public static void main(String[] args) {

//        CSVWriter writer = new CSVWriter();
//        CSVReader reader = new CSVReader();
        CSVOperator operator = new CSVOperator();
        operator.compareTwoCSVFile("data//input//compare//bed.csv","data//input//compare//bed1.csv");
//        ArrayList<String> list = reader.getRecordKeyList("data//input//compare//bed.csv","SecurityID");
//        for (String s : list) {
//            System.out.println(s);
//        }


//        operator.extractContentAndWrite("data//input//people.csv","data//output//benin.csv","Country","norway","Name","Age","Date of Birth","Country");

//        List<CSVRecord> recordList=CSVMatcher.getInstance("data//input//people.csv").getAllMatchingRecord().where().headerValueEquals("age",36)
//        .and().headerValueEquals("Country","Singapore").build();
//
//        CSVPrinter printer = writer.createCSVPrinter("data//output//benin2.csv","Name","Age","Phone","email","City","Country");
//        writer.writeCSVPrinter(reader.getFilteredOutColumnPrinter(recordList,printer,"Name","Age","Phone","email","City","Country"));
    }
}
