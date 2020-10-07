package com.business.csv;

import com.config.path.ConfigParams;
import com.data.ConditionalCSVFormatDTO;
import com.data.FileCompareDTO;
import com.tool.common.CommonNames;
import com.tool.common.CommonUtils;
import com.tool.common.report.html.FileComparisonReporter;
import com.tool.csv.CSVMatcher;
import com.tool.csv.CSVReader;
import com.tool.csv.CSVWriter;
import cucumber.api.java.hu.Ha;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.*;

public class CSVOperator implements CommonNames {
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

    public void writeCSVRecordListWithRequiredHeadersAndAdditionalParameter(List<CSVRecord>  recordList,String relativeOutputFilePath,String additionalParamValue,String ... outputHeaders)
    {
        ArrayList<String> headerList = new ArrayList<String>(Arrays.asList(outputHeaders));
        headerList.add(0,ConfigParams.additionalCSVParamHeaderName);
        CSVPrinter printer = writer.createCSVPrinter(relativeOutputFilePath,headerList.toArray(new String [headerList.size()]));
        writer.writeCSVPrinter(reader.addAdditionalParamAndGetFilteredOutColumnPrinter(recordList,printer,additionalParamValue,outputHeaders));
    }

    public void conditionalFormatDataAndWriteCSV(String inputCSVPath, ArrayList<ConditionalCSVFormatDTO> formatDTOArrayList,String outputCSVPath)
    {

        List<CSVRecord> recordList = reader.getAllRecordListWithHeaders(inputCSVPath);
        ArrayList<ArrayList<String>> printerList = new ArrayList<ArrayList<String>>();
        ArrayList<String>headerList = new ArrayList<>();
        Map<String,String> recordHeaderMap = recordList.get(0).toMap();
        recordHeaderMap.forEach((key,map)-> headerList.add(key));


        for(CSVRecord record: recordList)
        {
            ArrayList<String> valueList = new ArrayList<String>();
            Map<String,String> recordMap= record.toMap();
            recordMap.forEach((key,value)-> valueList.add(value));
            HashMap<String,String> conditionalMap = conditionalFormatCSVRecord(record,formatDTOArrayList);
            for(String key:conditionalMap.keySet())
            {
                if(!headerList.contains(key))
                headerList.add(key);
                valueList.add(conditionalMap.get(key));
            }
            printerList.add(valueList);
        }

        writer.writeArrayListAsCSV(outputCSVPath,printerList,headerList.toArray(new String [headerList.size()]));
    }

    private ArrayList<String> getHeaderMapFromCSVRecord( CSVRecord record)
    {
        ArrayList<String>headerList = new ArrayList<>();
        record.forEach((key)-> headerList.add(key));
        return headerList;
    }

    private HashMap<String,String> conditionalFormatCSVRecord(CSVRecord record,ArrayList<ConditionalCSVFormatDTO> formatDTOArrayList)
    {
        HashMap<String,String> valueMap = new HashMap<String,String>();



        for(ConditionalCSVFormatDTO dto:formatDTOArrayList)
        {
            String conditionalKey = dto.getConditionHeaderName();
            String conditionalValue = dto.getConditionHeaderValue();
            String recordValue = record.get(conditionalKey);
            if(recordValue.equalsIgnoreCase(conditionalValue))
            {
                valueMap.put(dto.getFormatHeaderName(),dto.getFormatHeaderValue());
                break;
            }
            else
                valueMap.put(dto.getFormatHeaderName(),"");
        }

        return valueMap;

    }








    public void     writeCSVRecordListWithRequiredHeaders(ArrayList<ArrayList<String>> recordList,String relativeOutputFilePath,String ... outputHeaders)
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


    public void compareTwoCSVFile(String relativeInputFile1Path,String relativeInputFile2Path) {
        CSVReader reader = new CSVReader();
        FileCompareDTO dto = FileCompareDTO.builder().build();


        List<CSVRecord> recordList1 = reader.getAllRecordList(relativeInputFile1Path);
        List<CSVRecord> recordList2 = reader.getAllRecordList(relativeInputFile2Path);

        List<CSVRecord> recordList;
        List<CSVRecord> recordListNxt;

        int record1Size = recordList1.size();
        int record2Size = recordList2.size();
        CSVRecord file1columnNames = recordList1.remove(0);
        recordList2.remove(0);


        if (record1Size == record2Size) {
            System.out.println("Two files having the same number of rows");
            recordList = recordList1;
            recordListNxt = recordList2;
        } else {
            System.out.println("Files have different sizes. " + relativeInputFile1Path + " have " + (record1Size - 1) + " rows , and " + relativeInputFile2Path + " have " + (record2Size - 1));
            if (record1Size > record2Size) {
                recordList = recordList2;
                recordListNxt = recordList1;
            } else {
                recordList = recordList1;
                recordListNxt = recordList2;
            }

        }


        String file1Name = CommonUtils.extractFileName(relativeInputFile1Path);
        String file2Name = CommonUtils.extractFileName(relativeInputFile2Path);
        ArrayList<ArrayList<String>> disparityTable = new ArrayList<ArrayList<String>>();
        ArrayList<String> tableHeaderList = new ArrayList<>(Arrays.asList(MISMATCH_TABLE_SI,MISMATCH_TABLE_ROW_NO,MISMATCH_TABLE_COLMN,
                MISMATCH_TABLE_FILE_HEAD+file1Name,MISMATCH_TABLE_FILE_HEAD+file2Name));

        disparityTable.add(tableHeaderList);

        int recordRowNumber = 0;

        int completeRowMatchedRecords = 0;
        for (CSVRecord record1 : recordList) {
            boolean isMismatchFound = false;
            int misMatchIndex = 0;
            boolean isCompleteRowMatched = true;


            CSVRecord record2 = recordListNxt.get((recordRowNumber));
            int record1ItemLoop = 0;
            for (String item1 : record1) {
                String item2 = record2.get(record1ItemLoop);
                try {
                    ArrayList<String> mismatchRecord = new ArrayList<>();
                    if (!item1.equalsIgnoreCase(item2)) {
                        misMatchIndex++;
                        isMismatchFound = true;
                        isCompleteRowMatched = false;
                        mismatchRecord.addAll(Arrays.asList(misMatchIndex + "", recordRowNumber + 1 + "", file1columnNames.get(record1ItemLoop), item1, item2));
                        System.out.println("Item mismatch found at Row number " + recordRowNumber + " and at the column index " + record1ItemLoop + " (" + item1 + " -> " + item2 + ")");
                    } else
                        isMismatchFound = false;
                    if (isMismatchFound)
                        disparityTable.add(mismatchRecord);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                record1ItemLoop++;
            }
            if (isCompleteRowMatched)
                completeRowMatchedRecords++;
            recordRowNumber++;

        }


        dto.setDocumentTitle("File Comparison Report (" + file1Name + "," + file2Name + ")");
        dto.setPieChartTitle(PIE_CHART_TITLE);
        dto.setPieChartDivId(PIE_CHART_DIV_ID);
        StringBuilder data = new StringBuilder();
        int mismatchedRows = recordList.size()-completeRowMatchedRecords;
        data.append("['Item','Count'],");
        data.append("['Matched',").append(completeRowMatchedRecords + "").append("],").
                append("['MisMatched',").append(mismatchedRows+ "").append("],").
                append("['Orphan records',").append(recordListNxt.size() - recordList.size() + "").append("]");
        dto.setPieChartData(data);
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put(REP_TOT_REC_FIL1+file1Name,""+recordList.size());
        map.put(REP_TOT_REC_FIL2+file2Name,""+recordListNxt.size());
        map.put(REP_TOT_REC_COMPRD,""+recordList.size());
        map.put(REP_TOT_ROW_MIS_MATCH,mismatchedRows+"");
        map.put(REP_TOT_MIS_MATCH,""+disparityTable.size());

        dto.setBriefingMap(map);
        dto.setTableData(disparityTable);
        dto.setOutputFilePath(ConfigParams.fileCompareHtmlFilePath);
        FileComparisonReporter.createFileComparisonReport(dto);

    }




    public static void main(String[] args) {

        CSVWriter writer = new CSVWriter();
        CSVReader reader = new CSVReader();
        CSVOperator operator = new CSVOperator();

//        ArrayList<ConditionalCSVFormatDTO> formatDTOArrayList = new ArrayList<>();
//        formatDTOArrayList.add(new ConditionalCSVFormatDTO("SecurityID","577081BD3","CompanyName","Infosys"));
//        formatDTOArrayList.add(new ConditionalCSVFormatDTO("SecurityID","33767BAB5","CompanyName","TCS"));
//        operator.conditionalFormatDataAndWriteCSV("data//input//compare//csvSource.csv",formatDTOArrayList,"data//output//formated.csv");




//        List<CSVRecord> recordList=reader.getMatchingRecordList("data//input//compare//17_09_2020.csv","Assignee","joemon.david");
//        operator.writeCSVRecordListWithRequiredHeadersAndAdditionalParameter(recordList,"data//output//additional.csv",
//                "True","Issue Type","Issue key","Issue id","Summary","Status");

//        operator.compareTwoCSVFile("data//input//compare//17_09_2020.csv","data//input//compare//18_09_2020.csv");
//        ArrayList<String> list = reader.getRecordKeyList("data//input//compare//bed.csv","SecurityID");
//        for (String s : list) {
//            System.out.println(s);
//        }


//        operator.extractContentAndWrite("data//input//people.csv","data//output//benin.csv","Country","norway","Name","Age","Date of Birth","Country");

        List<CSVRecord> recordList=new CSVMatcher().init("data//input//Temp_TestSuite.csv").getAllMatchingRecord().
                where().headerValueEquals("SecurityType","GTM FXF")
        .and().headerValueHaveAny("TransactionType",new String [] {"Buy","Buy Cancel"})
                .sortByHeaderValues("TransactionType",new String [] {"Buy Cancel","Buy"}).build();

        String [] headers = {"BlockId","Automation","SecurityType","TransactionType","SecurityID","Account","RecordType","TradeDate","SettlementDate","Broker"};

        CSVPrinter printer = writer.createCSVPrinter("data//output//extracted.csv",headers);
        writer.writeCSVPrinter(reader.getFilteredOutColumnPrinter(recordList,printer,headers));
    }
}
