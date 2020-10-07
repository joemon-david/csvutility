package com.tool.excel;



import com.tool.common.Reporter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExcelMatcher {

    private static LinkedHashMap<Integer, LinkedHashMap<String,Object>> excelSheetDataMap = new LinkedHashMap<>();
    private static Map<Object,Object> equalsObjectMap = new LinkedHashMap<>();
    private static LinkedHashMap<Integer, LinkedHashMap<String,Object>> matchingDataMap = new LinkedHashMap<>();
    private static StringBuilder sb = new StringBuilder();




    public static void getInstance(String excelFilePath, String sheetName){

        excelSheetDataMap = ExcelReader.readDataFromExcelFile(excelFilePath,sheetName);
    }
    public ExcelMatcher headerValueEquals(Object header, Object match)
    {
        equalsObjectMap.put(header,match);
        sb.append(" '").append(header).append("'").append(" == ").append(" '").append(match).append("' and ");
        return this;
    }
    public ExcelMatcher getAllMatchingRecord()
    {
        sb.append("Select all matching value where");return this;
    }
    public ExcelMatcher where()
    {
        sb.append(" where "); return this;
    }
    public ExcelMatcher and()
    {
        sb.append(" and "); return this;
    }

    public LinkedHashMap<Integer, LinkedHashMap<String,Object>> build()
    {
        int matchIndex=0;
        for (Map.Entry<Integer, LinkedHashMap<String, Object>> e : excelSheetDataMap.entrySet()) {

            Integer index = e.getKey();
            LinkedHashMap<String, Object> map = e.getValue();
//            Reporter.printHashMap(map);
            boolean isMatchFound = false;
            for (Map.Entry<Object, Object> entry : equalsObjectMap.entrySet()) {
                Object key = entry.getKey();
                Object value = map.get(key);
                if(null == value)
                    continue;

                Object expectedColumnValue = entry.getValue();
                String actualColumnValue = map.get(key).toString();
                if (actualColumnValue.equalsIgnoreCase(expectedColumnValue.toString()))
                {
                    isMatchFound=true;
//                    System.out.println("*** "+matchIndex+" - Match found for the key "+headerValue+" and "+value);
                }
                else {
                    isMatchFound=false;
                    break;
                }

            }
            if(isMatchFound)
            {
                matchingDataMap.put(matchIndex++,excelSheetDataMap.get(index));
            }

        }
        System.out.println(sb.toString());
        return matchingDataMap;
    }


}
