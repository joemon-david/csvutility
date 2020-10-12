package com.tool.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CSVMatcher {


    private  CSVParser csvParser = null;

    private  Map<Object,Object> equalsObjectMap = null;
    private  HashMap<Object,ArrayList<Object>> containsObjectMap = null;
    private  HashMap<Object,ArrayList<Object>> sortingOrderMap = null;
    private  boolean isEqualOperationCalled = false;
    private  boolean isContainsOperationCalled = false;
    private boolean isSortingOperationCalled = false;
    private StringBuilder sb = new StringBuilder();



 public CSVMatcher init(String relativeCSVFilePath)
 {


         BufferedReader reader;
         try {
             reader = Files.newBufferedReader(Paths.get(relativeCSVFilePath));
             csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
         } catch (IOException e) {
             e.printStackTrace();
         }

     equalsObjectMap = new HashMap<>();
     containsObjectMap = new HashMap<Object,ArrayList<Object>>();
     sortingOrderMap  = new HashMap<Object,ArrayList<Object>> ();
     return this;

 }

 public CSVMatcher getAllMatchingRecord()
 {
     sb.append("Select All Matching records , where ");
     return this;
 }
 public CSVMatcher where()
 {
     return this;
 }
 public CSVMatcher and()
 {
     return this;
 }
 public CSVMatcher headerValueEquals(Object header, Object match)
 {
     sb.append("'"+header+"' = '"+match+"'  and ");
     isEqualOperationCalled = true;
     equalsObjectMap.put(header,match);
     return this;
 }

 public CSVMatcher headerValueHaveAny(Object header, String [] anyMatchingValues)
 {
     sb.append(" contains ("+Arrays.asList(anyMatchingValues)+" ) and ");
     isContainsOperationCalled = true;
     ArrayList<Object> containsList = new ArrayList<>(Arrays.asList(anyMatchingValues));
     containsObjectMap.put(header,containsList);
     return this;
 }

 public CSVMatcher sortByHeaderValues(Object header, String [] sortingOrder)
 {
     isSortingOperationCalled = true;
     ArrayList<Object> sortOrderList = new ArrayList<>(Arrays.asList(sortingOrder));
     sortingOrderMap.put(header,sortOrderList);
     return this;
 }


 public List<CSVRecord> build()
 {
     System.out.println(sb.toString());
     List<CSVRecord> matchingCSVRecordList = new ArrayList<>();
     for(CSVRecord csvRecord : csvParser)
     {
        boolean isEqualsMatchFound = isRecordMatchesEqualsObjectMap(csvRecord);
         boolean isContainsMatchFound = isRecordMatchesContainsObjectMap(csvRecord);
         if(isEqualsMatchFound && isContainsMatchFound)
             matchingCSVRecordList.add(csvRecord);
     }
     if(isSortingOperationCalled)
     {
         matchingCSVRecordList = sortCSVRecord(matchingCSVRecordList);
     }

     return matchingCSVRecordList;
 }
 private List<CSVRecord> sortCSVRecord(List<CSVRecord> list)
 {
     List<CSVRecord> sortedList = new ArrayList<>();
     for(Object header:sortingOrderMap.keySet())
     {
        ArrayList<Object> sortBaseList = sortingOrderMap.get(header);

            for(Object sort:sortBaseList)
            {
                for(CSVRecord record : list)
                {
                    String value = record.get(header.toString());
                    if(value.equalsIgnoreCase(sort.toString()))
                        sortedList.add(record);
                }
            }
     }
     return sortedList;

 }


 private boolean isRecordMatchesEqualsObjectMap(CSVRecord csvRecord)
 {
     boolean isMatchFound = false;
     if(isEqualOperationCalled)
     {
         for (Map.Entry<Object, Object> entry : equalsObjectMap.entrySet()) {
             Object header = entry.getKey();
             Object values = entry.getValue();
             String headerValue = csvRecord.get(header.toString());
             if (headerValue.equalsIgnoreCase(values.toString())) {
                 isMatchFound=true;
             } else {
                 isMatchFound = false;
                 break;
             }

         }
     }else
         isMatchFound = true;

     return isMatchFound;
 }

    private boolean isRecordMatchesContainsObjectMap(CSVRecord csvRecord)
    {
        boolean isMatchFound = false;
        if(isContainsOperationCalled)
        {
            for (Object key : containsObjectMap.keySet()) {
                ArrayList<Object> containsList = containsObjectMap.get(key);
                String headerValue = csvRecord.get(key.toString());
                if (containsList.contains(headerValue)) {
                    isMatchFound=true;
                } else {
                    isMatchFound=false;
                    break;
                }

            }
        }else
            isMatchFound = true;

        return isMatchFound;
    }



}
