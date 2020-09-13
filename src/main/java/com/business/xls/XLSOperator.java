package com.business.xls;

import com.business.csv.CSVOperator;
import com.config.path.ConfigParams;
import com.tool.common.Reporter;
import com.tool.excel.ExcelMatcher;

import java.util.*;


public class XLSOperator {

    private static ExcelMatcher matcher;
    private static LinkedHashMap<Integer, LinkedHashMap<String, Object>> result;

    public void readXLSFileAndSaveContentToMap()
    {
        matcher = ExcelMatcher.getInstance(ConfigParams.masterFilePath, ConfigParams.masterSheetName);
    }

    public void selectAllMatchingRecord()
    {
        matcher=matcher.getAllMatchingRecord();
    }

    public void selectRowWithHeaderValueEquals(String header,String value)
    {
        matcher = matcher.headerValueEquals(header,value);
    }

    public void filterExcelMapAndCreateCSVWithRequiredHeaders(String relativeOutputFilePath,String ...headers)
    {
        result = matcher.build();
        ArrayList<ArrayList<String>> filteredList =  convertMapToListWithRequiredHeaders(result,headers);
        new CSVOperator().writeCSVRecordListWithRequiredHeaders(filteredList,relativeOutputFilePath,headers);
    }
    public void filterExcelMapAndCreateCSVWithRequiredHeadersAndAdditionalParameter(String relativeOutputFilePath,String additionalParam,String ...headers)
    {
        result = matcher.build();
        ArrayList<ArrayList<String>> filteredList =  convertMapToListWithRequiredHeadersAndAdditionalParameter(result,additionalParam,headers);
        ArrayList<String> headerList = new ArrayList<String>(Arrays.asList(headers));
        headerList.add(ConfigParams.additionalParamHeaderName);
        new CSVOperator().writeCSVRecordListWithRequiredHeaders(filteredList,relativeOutputFilePath,headerList.toArray(new String[headerList.size()]));
    }

    public static void main(String[] args) {

        XLSOperator operator = new XLSOperator();
        String [] headers = new String[]{"timestamp","name", "application_Version","client_Type"};
        operator.readXLSFileAndSaveContentToMap();
        operator.selectAllMatchingRecord();
        operator.selectRowWithHeaderValueEquals("name","userStatusRowTapped");
        operator.selectRowWithHeaderValueEquals("timestamp","9/11/2020");
        operator.filterExcelMapAndCreateCSVWithRequiredHeadersAndAdditionalParameter("data//output//mh.csv","Apple",headers);
//        LinkedHashMap<Integer, LinkedHashMap<String, Object>> result = ExcelMatcher.getInstance(ConfigParams.masterFilePath, ConfigParams.masterSheetName)
//                .getAllMatchingRecord().where().headerValueEquals("name","userStatusRowTapped")
//                .and().headerValueEquals("timestamp","9/11/2020").build();
//
//        List<String> outputHeaders = Arrays.asList(headers);
//
//        result.forEach( (index,map)-> {
//            System.out.println("============================================================================");
//            map.forEach( (key,value)-> {
//                if(outputHeaders.contains(key))
//                System.out.print("| "+value+"\t"+"|");
//            });
//            System.out.println();
//
//        });
//        List<String> headerList = Arrays.asList(headers);
//        headerList.add(ConfigParams.additionalParamHeaderName);
//
//        new CSVOperator().writeCSVRecordListWithRequiredHeaders(new XLSOperator().convertMapToListWithRequiredHeadersAndAdditionalParameter(result,"Apple",headers),"data//output//mh.csv",headers);
    }

    public ArrayList<ArrayList<String>> convertMapToListWithRequiredHeaders(LinkedHashMap<Integer, LinkedHashMap<String, Object>> map,String... headers)
    {
        ArrayList<ArrayList<String>> resultList = new ArrayList<>();
        List<String> outputHeaders = Arrays.asList(headers);

        for (Map.Entry<Integer, LinkedHashMap<String, Object>> entry : map.entrySet()) {
            ArrayList<String> rowList = getFilteredRowValues(outputHeaders, entry);
            resultList.add(rowList);
        }
            return resultList;
    }

    private ArrayList<String> getFilteredRowValues(List<String> outputHeaders, Map.Entry<Integer, LinkedHashMap<String, Object>> entry) {
        Integer index = entry.getKey();
        LinkedHashMap<String, Object> rowMap = entry.getValue();
        ArrayList<String> rowList = new ArrayList<>();
        for (Map.Entry<String, Object> e : rowMap.entrySet()) {
            String key = e.getKey();
            Object value = e.getValue();
            if(outputHeaders.contains(key))
            {
                rowList.add(value.toString());
            }
        }
        return rowList;
    }

    public ArrayList<ArrayList<String>> convertMapToListWithRequiredHeadersAndAdditionalParameter(LinkedHashMap<Integer, LinkedHashMap<String, Object>> map,String additionalParam,String... headers)
    {
        ArrayList<ArrayList<String>> resultList = new ArrayList<>();
        List<String> outputHeaders = Arrays.asList(headers);
        Reporter.printArrayList(outputHeaders);
        int suffixIndex = 0;

        for (Map.Entry<Integer, LinkedHashMap<String, Object>> entry : map.entrySet()) {
            suffixIndex++;
            ArrayList<String> rowList = getFilteredRowValues(outputHeaders, entry);
            rowList.add(additionalParam+"_"+suffixIndex);
            Reporter.printArrayList(rowList);
            resultList.add(rowList);
        }
        return resultList;
    }
}
