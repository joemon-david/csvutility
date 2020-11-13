package com.business.csv;

import com.config.path.ConfigParams;
import com.tool.common.report.excel.ExcelReportCreator;
import com.tool.csv.CSVReader;
import com.tool.excel.ExcelReader;
import com.tool.excel.ExcelWriter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.*;

public class CSVComparator implements ConfigParams {

    public static void main(String[] args) throws IOException {
        CSVComparator comp = new CSVComparator();
        comp.compareCSVAndCreateReport("data//input//compare//mapping//Path_SDO_File Mapping V0.1.xlsx");
//        LinkedHashMap<Integer, LinkedHashMap<String, Object>> s = new ExcelReader().readAllDataFromExcelFile("data//input//compare//mapping//Path_SDO_File Mapping V0.1.xlsx","Config",7);
//
//
//                LinkedHashSet<String> value = comp.getConditionalColumnValueList(s,COMPARE_PRIMARY_KEY,"Y",COMPARE_TAR_COLUMN);
//        System.out.println(value);




    }

    public LinkedHashSet<String> getUniqueColumnList(LinkedHashMap<Integer, LinkedHashMap<String, Object>> sheetData, String headerName)
    {
        LinkedHashSet<String> columnData = new LinkedHashSet<>();

        sheetData.forEach((index,rowData)->{
            columnData.add(rowData.get(headerName).toString());
        });
        return columnData;
    }

    public LinkedHashSet<String> getConditionalColumnValueList(LinkedHashMap<Integer, LinkedHashMap<String, Object>> sheetData, String conditionalHeaderName,
                                                           String conditionalValue,String columnHeader)
    {
        LinkedHashSet<String> columnData = new LinkedHashSet<>();

        sheetData.forEach((index,rowData)->{
            if(rowData.get(conditionalHeaderName).toString().equalsIgnoreCase(conditionalValue))
            columnData.add(rowData.get(columnHeader).toString());
        });
        return columnData;
    }
    public String getConditionalColumnValue(LinkedHashMap<Integer, LinkedHashMap<String, Object>> sheetData, String conditionalHeaderName,
                                                               String conditionalValue,String columnHeader)
    {
        String columnValue = "";

        for (Integer index  : sheetData.keySet()) {
            LinkedHashMap<String, Object> rowData = sheetData.get(index);
            if (rowData.get(conditionalHeaderName).toString().equalsIgnoreCase(conditionalValue)) {
                columnValue = rowData.get(columnHeader).toString();
                break;
            }
        }
        return columnValue;

    }

    public void compareCSVAndCreateReport(String configfile) throws IOException {
        CSVReader reader = new CSVReader();
        ExcelReportCreator reportCreator = new ExcelReportCreator();
        LinkedHashMap<Integer, LinkedHashMap<String, Object>> s = new ExcelReader().readAllDataFromExcelFile(configfile,"Config",7);

        String fileTypeToRun = "Positions";
        ArrayList<String>fileTypeToRunList = new ArrayList<>();
        LinkedHashSet<String> fileTypeList = getUniqueColumnList(s,COMPARE_FILE_TYPE);
        if(fileTypeToRun.equalsIgnoreCase("All"))
            fileTypeToRunList.addAll(fileTypeList);
        else
            fileTypeToRunList.add(fileTypeToRun);


        HashMap<String,HashMap<String, String>> srcTargetColumnMapList = getSrcTargetMappingList(s, fileTypeToRunList);
        int index = 0;

        for(String fileType:fileTypeToRunList)
        {

            String sourceFilePath = COMPARE_SRC_DIR_PATH+getConditionalColumnValue(s,COMPARE_FILE_TYPE,fileType,COMPARE_SRC_FILE);
            List<CSVRecord>  sourceFileRecordList = reader.getAllRecordListWithHeaders(sourceFilePath);
            String targetFilePath = COMPARE_TARGET_DIR_PATH+getConditionalColumnValue(s,COMPARE_FILE_TYPE,fileType,COMPARE_TAR_FILE);
            List<CSVRecord>  targetFileRecordList = reader.getAllRecordListWithHeaders(targetFilePath);
            LinkedHashSet<String> primaryKeySet = getConditionalColumnValueList(s,COMPARE_PRIMARY_KEY,"Y",COMPARE_SRC_COLUMN);
            HashMap<String,String> srcTargetColumnMap = srcTargetColumnMapList.get(fileType);
            for(CSVRecord sourceRecord:sourceFileRecordList)
            {
                index++;
                CSVRecord matchingTargetRecord = null;
                for(CSVRecord targetRecord: targetFileRecordList)
                {
                    boolean isTargetRowMatched = true;
                    for(String primaryKey:primaryKeySet)
                        {
                            String sourceValue = sourceRecord.get(primaryKey);
                            String targetHeaderName = srcTargetColumnMap.get(primaryKey);
                            String targetValue = targetRecord.get(targetHeaderName);
                            if(!sourceValue.equalsIgnoreCase(targetValue))
                                isTargetRowMatched = false;
                        }
                    if(isTargetRowMatched)
                        matchingTargetRecord = targetRecord;

                }
                for(String sourceKey:srcTargetColumnMap.keySet())
                {
                    String targetKey = srcTargetColumnMap.get(sourceKey);
                    String source = sourceRecord.get(sourceKey);


                if(matchingTargetRecord == null)
                {
                    System.out.println("**** There is no matching target row found !!!");
                    reportCreator.addRowData(index,sourceKey,source,targetKey,"Not Found");
                }
                else
                {
                    String target = matchingTargetRecord.get(targetKey);
                    reportCreator.addRowData(index,sourceKey,source,targetKey,target);
                }
                }
            }

            new ExcelWriter().writeDataToExcelSheet("data//output//excelReport.xlsx",reportCreator.getReportData(),fileType);
        }


    }

    private HashMap<String,HashMap<String, String>> getSrcTargetMappingList(LinkedHashMap<Integer, LinkedHashMap<String, Object>> s, ArrayList<String> fileTypeToRunList) {
        HashMap<String,HashMap<String,String>> srcTargetColumnMapList = new HashMap<String,HashMap<String, String>>();
        for(String fileType:fileTypeToRunList)
        {
            LinkedHashSet<String> srcList = getConditionalColumnValueList(s,COMPARE_FILE_TYPE,fileType,COMPARE_SRC_COLUMN);
            HashMap<String,String> columnMap = new HashMap<>();
            srcList.forEach((key)-> {
                String value = getConditionalColumnValue(s,COMPARE_SRC_COLUMN,key,COMPARE_TAR_COLUMN);
                columnMap.put(key,value);
            });
            srcTargetColumnMapList.put(fileType,columnMap);
        }
        return srcTargetColumnMapList;
    }


}
