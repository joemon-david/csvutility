package com.business.csv;


import com.data.CSVMatchingRecord;
import com.tool.common.CommonUtils;
import com.tool.common.NumericUtils;
import com.tool.common.TypeIdentifier;
import com.tool.common.report.excel.ExcelReportCreator;
import com.tool.csv.CSVReader;
import com.tool.excel.ExcelReader;
import com.tool.excel.ExcelWriter;
import cucumber.api.java.eo.Do;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.*;

public class CSVComparatorUpdated  extends CSVComparator {

    public static void main(String[] args) throws IOException {
        CSVComparatorUpdated comp = new CSVComparatorUpdated();
        comp.compareCSVAndCreateReport("data//input//compare//mapping//Path_SDO_File Mapping V0.1.xlsx");
//        LinkedHashMap<Integer, LinkedHashMap<String, Object>> s = new ExcelReader().readAllDataFromExcelFile("data//input//compare//mapping//Path_SDO_File Mapping V0.1.xlsx","Config",7);
//
//
//                LinkedHashSet<String> value = comp.getConditionalColumnValueList(s,COMPARE_PRIMARY_KEY,"Y",COMPARE_TAR_COLUMN);
//        System.out.println(value);




    }

    String combinePrimaryKeyValues(CSVRecord record,LinkedHashSet<String> primaryKeySet)
    {
        StringBuilder sb = new StringBuilder();
        primaryKeySet.forEach(key-> {sb.append(record.get(key)).append(":");});
        return sb.toString();

    }


    HashMap<String,LinkedHashMap<String,String>> convertCSVRecordToMap(List<CSVRecord> recordList,LinkedHashSet<String> primaryKeySet,
                                                                       LinkedHashMap<String,String> srcTargetColumnMap,boolean isSourceRecord)
    {
        HashMap<String,LinkedHashMap<String,String>> map = new HashMap<String,LinkedHashMap<String,String>>();
        for (CSVRecord rowData:recordList)
        {
            String key = combinePrimaryKeyValues(rowData,primaryKeySet);
            LinkedHashMap<String,String> valueMap = new LinkedHashMap<String,String>();
            srcTargetColumnMap.forEach((src,tar)-> {
                if(isSourceRecord)
                    valueMap.put(src,rowData.get(src));
                else
                    valueMap.put(src,rowData.get(tar));

            });
            map.put(key,valueMap);
        }


        return map;
    }


    String getRandomKeyOfMap(HashMap<String,LinkedHashMap<String,String>> map)
    {
        List<String> keysAsArray = new ArrayList<String>(map.keySet());
        Random r = new Random();
        return keysAsArray.get(r.nextInt(keysAsArray.size() ));
    }

    @Override
    public void compareCSVAndCreateReport(String configFile) throws IOException {


            CSVReader reader = new CSVReader();
            ExcelReportCreator reportCreator = new ExcelReportCreator();
            LinkedHashMap<Integer, LinkedHashMap<String, Object>> s = new ExcelReader().readAllDataFromExcelFile(configFile,"Config",COMPARE_MAX_COLUMN_IN_CONFIG_FILE);

            String fileTypeToRun = "Positions";
            ArrayList<String> fileTypeToRunList = new ArrayList<>();
            LinkedHashSet<String> fileTypeList = getUniqueColumnList(s,COMPARE_FILE_TYPE);
            if(fileTypeToRun.equalsIgnoreCase("All"))
                fileTypeToRunList.addAll(fileTypeList);
            else if (fileTypeToRun.split(",").length>1)
                fileTypeToRunList.addAll(Arrays.asList(fileTypeToRun.split(",")));
            else
                fileTypeToRunList.add(fileTypeToRun);


        LinkedHashMap<String, LinkedHashMap<String, String>> srcTargetColumnMapList = getSrcMappingList(s, fileTypeToRunList,COMPARE_TAR_COLUMN);
        LinkedHashMap<String,LinkedHashMap<String, String>> srcTransLogicMapList = getSrcMappingList(s, fileTypeToRunList,COMPARE_TRANSF_LOGIC);

            int index = 0;

            for(String fileType:fileTypeToRunList)
            {

                System.out.println("Executing the file comparison of File Type "+fileType+"  ......");
                String sourceFilePath = COMPARE_SRC_DIR_PATH+getConditionalColumnValue(s,COMPARE_FILE_TYPE,fileType,COMPARE_SRC_FILE);
                List<CSVRecord>  sourceFileRecordList = reader.getAllRecordListWithHeaders(sourceFilePath);
                String targetFilePath = COMPARE_TARGET_DIR_PATH+getConditionalColumnValue(s,COMPARE_FILE_TYPE,fileType,COMPARE_TAR_FILE);

                List<CSVRecord>  targetFileRecordList = reader.getAllRecordListWithHeaders(targetFilePath);
                LinkedHashSet<String> primaryKeySet = getConditionalColumnValueList(s,COMPARE_PRIMARY_KEY,"Y",COMPARE_SRC_COLUMN);
                LinkedHashMap<String,String> srcTargetColumnMap = srcTargetColumnMapList.get(fileType);
                LinkedHashMap<String,String> srcTransLogicMap = srcTransLogicMapList.get(fileType);
                LinkedHashSet<CSVMatchingRecord> matchingRecordsList = new LinkedHashSet<>();
                int recordNumber=0;
                HashMap<String,LinkedHashMap<String,String>> sourceRecordMap = convertCSVRecordToMap(sourceFileRecordList,primaryKeySet,srcTargetColumnMap,true);
                HashMap<String,LinkedHashMap<String,String>> targetRecordMap = convertCSVRecordToMap(targetFileRecordList,primaryKeySet,srcTargetColumnMap,false);

                for(String primKey:sourceRecordMap.keySet())
                {
                    Double allowedTolerance=0.0;
                    if(targetRecordMap.containsKey(primKey))
                    {
                        LinkedHashMap<String,String> sourceRow = sourceRecordMap.get(primKey);
                        LinkedHashMap<String,String> targetRow =  targetRecordMap.get(primKey);
                        for (String column:sourceRow.keySet()) {
                            String sourceValue = sourceRow.get(column);
                            String targetHeaderName = srcTargetColumnMap.get(column);
                            String targetValue = targetRow.get(column);
                            String transLogic = srcTransLogicMap.containsKey(column)?srcTransLogicMap.get(column):null;
                            if ((TypeIdentifier.getDataTypes(sourceValue) == TypeIdentifier.DATA_TYPES.DOUBLE) && (TypeIdentifier.getDataTypes(targetValue) == TypeIdentifier.DATA_TYPES.DOUBLE) ) {
                                if (null != transLogic && CommonUtils.extractTransLogicType(transLogic).equalsIgnoreCase(COMPARE_TRANS_LOGIC_TOLERANCE))
                                    allowedTolerance = Double.parseDouble(CommonUtils.extractTransLogicValue(transLogic).toString());
                                else allowedTolerance=0.0;

                            }
                            reportCreator.addRowData(recordNumber,column,sourceValue,targetHeaderName,targetValue,allowedTolerance);
                        }
                    }else {
                        for (String column:sourceRecordMap.get(primKey).keySet()) {
                            String sourceValue = sourceRecordMap.get(primKey).get(column);
                            LinkedHashMap<String,String> targetRow=targetRecordMap.get(getRandomKeyOfMap(targetRecordMap));
                            String targetValue = targetRow.get(column);
                            String targetHeaderName = srcTargetColumnMap.get(column);
                            reportCreator.addRowData(recordNumber, column, sourceValue, targetHeaderName, targetValue, allowedTolerance);
                        }

                    }

                    recordNumber++;
                }




//                for(CSVRecord sourceRecord:sourceFileRecordList)
//                {
//
////                    CSVMatchingRecord matchingRecord = new CSVMatchingRecord();
////                    matchingRecord.setPrimaryKeySet(primaryKeySet);
////                    matchingRecord.setSrcRecord(sourceRecord);
//                    HashMap<String,Object> toleranceMap = getRowToleranceMap(srcTransLogicMap);
////                    matchingRecord.setRowToleranceMap(toleranceMap);
////                    matchingRecord.setSrcTargetMap(srcTargetColumnMap);
//                    CSVRecord matchingTargetRecord = null;
//                    Double allowedTolerance=0.0;
//                    boolean isTargetRowMatched = false;
////                    matchingRecord.setMatchFound(false);
//                    for(CSVRecord targetRecord: targetFileRecordList)
//                    {
//
//                        for(String primaryKey:primaryKeySet)
//                        {
//                            String sourceValue = sourceRecord.get(primaryKey);
//                            String targetHeaderName = srcTargetColumnMap.get(primaryKey);
//                            String targetValue = targetRecord.get(targetHeaderName);
//                            String transLogic = srcTransLogicMap.containsKey(primaryKey)?srcTransLogicMap.get(primaryKey):null;
//
//                            if ((TypeIdentifier.getDataTypes(sourceValue) == TypeIdentifier.DATA_TYPES.DOUBLE) && (TypeIdentifier.getDataTypes(targetValue) == TypeIdentifier.DATA_TYPES.DOUBLE) )
//                            {
//                                if(null!=transLogic && CommonUtils.extractTransLogicType(transLogic).equalsIgnoreCase(COMPARE_TRANS_LOGIC_TOLERANCE))
//                                    allowedTolerance = Double.parseDouble(CommonUtils.extractTransLogicValue(transLogic).toString());
//                                if(Double.parseDouble(sourceValue) == Double.parseDouble(targetValue))
//                                    isTargetRowMatched = true;
//                                else
//                                {
//                                    Double differncePercentage = Math.abs(NumericUtils.percentageOfDifference(Double.parseDouble(sourceValue),Double.parseDouble(targetValue)));
//                                    if( differncePercentage <= allowedTolerance)
//                                        isTargetRowMatched = true;
//                                }
//
//                            }else if(sourceValue.equalsIgnoreCase(targetValue))
//                            {
//                                isTargetRowMatched = true;
//                            }else
//                                isTargetRowMatched = false;
//                        }
//                        if(isTargetRowMatched)
//                        {
//                            //                            matchingRecord.setTargetRecord(targetRecord);
////                            matchingRecord.setMatchFound(true);
//                            matchingTargetRecord=targetRecord;
//                            System.out.println("Source Record at index "+ sourceRecord.getRecordNumber()+ " have a match at Target Record Number "+targetRecord.getRecordNumber());
//                            break;
//                        }
//                    }
//                        if(!isTargetRowMatched)
//                        {
////                            matchingRecord.setMatchFound(false);
////                            matchingRecord.setTargetRecord(targetFileRecordList.get(recordNumber));
//                            matchingTargetRecord=targetFileRecordList.get(recordNumber);
//                        }
//                    for(String key: srcTargetColumnMap.keySet())
//                    {
//                        String targetKey = srcTargetColumnMap.get(key);
//                        Object tolerance = (toleranceMap.containsKey(key))?toleranceMap.get(key):0.0;
//                        if(isTargetRowMatched)
//                            reportCreator.addRowData(recordNumber,key,sourceRecord.get(key),targetKey,matchingTargetRecord.get(targetKey),tolerance);
//                        else
//                            reportCreator.addRowData(recordNumber,key,sourceRecord.get(key),targetKey,matchingTargetRecord.get(targetKey),tolerance);
//                    }
//
//
////                    matchingRecordsList.add(matchingRecord);
//                    recordNumber++;
//
//                }
                System.out.println("Total Number of Records "+matchingRecordsList.size());
                System.out.println("Columns to add in result "+ srcTargetColumnMap);
//                for(CSVMatchingRecord record:matchingRecordsList)
//                {
//
//                    CSVRecord sourceRecord = record.getSrcRecord();
//                    CSVRecord targetRecord = record.getTargetRecord();
//                    HashMap<String,String> sourceTargetMap = record.getSrcTargetMap();
//                    for(String key: sourceTargetMap.keySet())
//                    {
//                        String targetKey = srcTargetColumnMap.get(key);
//                        Object tolerance = (record.getRowToleranceMap().containsKey(key))?record.getRowToleranceMap().get(key):0.0;
//                        if(record.isMatchFound())
//                            reportCreator.addRowData(index,key,sourceRecord.get(key),targetKey,targetRecord.get(targetKey),tolerance);
//                        else
//                            reportCreator.addRowData(index,key,sourceRecord.get(key),targetKey,null,tolerance);
//                    }
//                    index++;
//                }

                new ExcelWriter().writeDataToExcelSheet("data//output//excelReport.xlsx",reportCreator.getReportData(),fileType);


        }

    }

    private HashMap<String,Object> getRowToleranceMap(HashMap<String,String> transLogicMap)
    {
        HashMap<String,Object> toleranceMap = new HashMap<>();
        for(String key : transLogicMap.keySet())
        {
            String transLogic = transLogicMap.get(key);
            if (CommonUtils.extractTransLogicType(transLogic).equalsIgnoreCase(COMPARE_TRANS_LOGIC_TOLERANCE))
            {
                Object allowedTolerance = Double.parseDouble(CommonUtils.extractTransLogicValue(transLogic).toString());
                toleranceMap.put(key,allowedTolerance);
            }
        }
        return toleranceMap;

    }



}
