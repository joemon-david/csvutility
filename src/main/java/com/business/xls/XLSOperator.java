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
        matcher = new ExcelMatcher();
                matcher.getInstance(ConfigParams.masterFilePath, ConfigParams.masterSheetName);
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
        headerList.add(0,ConfigParams.additionalParamHeaderName);
        Reporter.printArrayList(headerList);
        Reporter.printListOfList(filteredList);
        new CSVOperator().writeCSVRecordListWithRequiredHeaders(filteredList,relativeOutputFilePath,headerList.toArray(new String[headerList.size()]));
    }

    public void filterExcelMapAndCreateCSVWithRequiredHeadersAndAdditionalParameter(String relativeOutputFilePath,String additionalParam,ArrayList<String> outputHeaders,String ...filteringHeaders)
    {
        result = matcher.build();
        ArrayList<ArrayList<String>> filteredList =  convertMapToListWithRequiredHeadersAndAdditionalParameter(result,additionalParam,filteringHeaders);
        outputHeaders.add(0,ConfigParams.additionalParamHeaderName);
        Reporter.printArrayList(outputHeaders);
        Reporter.printListOfList(filteredList);
        new CSVOperator().writeCSVRecordListWithRequiredHeaders(filteredList,relativeOutputFilePath,outputHeaders.toArray(new String[outputHeaders.size()]));
    }

    public static void main(String[] args) {

        XLSOperator operator = new XLSOperator();

        //Name Age    Date of Birth Phone  email  City   Country

        String [] headers = new String[]{"Tran Type","Day","Portfolio (RKS)","Portfolio Desk","Trade Date","Settle Date"};
        String [] outPutHeaders = new String[]{"Transaction Type","No of Days","Portfolio (RKS)","Portfolio Desk","Trade Date","Settle Date"};
        ArrayList<String> outputHeaderList = new ArrayList<String>(Arrays.asList(outPutHeaders));

        operator.readXLSFileAndSaveContentToMap();

        operator.selectAllMatchingRecord();

        operator.selectRowWithHeaderValueEquals("SecurityType","GTM Fixed Income");

        operator.selectRowWithHeaderValueEquals("Date of execution","9/03/2020");

//            operator.selectRowWithHeaderValueEquals("Portfolio (GATE)","LABLT");

        operator.filterExcelMapAndCreateCSVWithRequiredHeadersAndAdditionalParameter("data//output//FI_C_JSIT.csv","800",outputHeaderList,headers);


//             LinkedHashMap<Integer, LinkedHashMap<String, Object>> result = ExcelMatcher.getInstance(ConfigParams.masterFilePath, ConfigParams.masterSheetName)
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
        ArrayList<String> outputHeaders = new ArrayList<>(Arrays.asList(headers));

        for (Map.Entry<Integer, LinkedHashMap<String, Object>> entry : map.entrySet()) {
            ArrayList<String> rowList = getFilteredRowValues(outputHeaders, entry);
            resultList.add(rowList);
        }
            return resultList;
    }

    private ArrayList<String> getFilteredRowValues(ArrayList<String> outputHeaders, Map.Entry<Integer, LinkedHashMap<String, Object>> entry) {
        Integer index = entry.getKey();
        LinkedHashMap<String, Object> rowMap = entry.getValue();
        ArrayList<String> rowList = new ArrayList<>();
        for (String header:outputHeaders) {
            if(!rowMap.containsKey(header))
            {
                System.out.println("**** Matching Row map does not contain the header key "+header);
            }
            String value = (null!= rowMap.get(header))?rowMap.get(header).toString():"";
            rowList.add(value);

//
        }
        return rowList;
    }

    public ArrayList<ArrayList<String>> convertMapToListWithRequiredHeadersAndAdditionalParameter(LinkedHashMap<Integer, LinkedHashMap<String, Object>> map,String additionalParam,String... headers)
    {
        ArrayList<ArrayList<String>> resultList = new ArrayList<>();
        ArrayList<String> outputHeaders = new ArrayList<> (Arrays.asList(headers));

        int suffixIndex = 0;

        for (Map.Entry<Integer, LinkedHashMap<String, Object>> entry : map.entrySet()) {
            suffixIndex++;
            ArrayList<String> rowList = getFilteredRowValues(outputHeaders, entry);
            rowList.add(0,additionalParam+"_"+suffixIndex);
//            Reporter.printArrayList(rowList);
            resultList.add(rowList);
        }
        return resultList;
    }
}
