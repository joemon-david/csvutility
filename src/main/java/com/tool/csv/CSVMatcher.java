package com.tool.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class CSVMatcher {


    private static CSVParser csvParser = null;
    private static List<CSVRecord> matchingCSVRecordList = null;
    private static Map<Object,Object> equalsObjectMap = null;



 public void init(String relativeCSVFilePath)
 {

     if(csvParser ==null)
     {
         BufferedReader reader;
         try {
             reader = Files.newBufferedReader(Paths.get(relativeCSVFilePath));
             csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
         } catch (IOException e) {
             e.printStackTrace();
         }

     }

     if (matchingCSVRecordList== null)
     {
         matchingCSVRecordList = new ArrayList<>();
     }

     if(equalsObjectMap == null)
     {
         equalsObjectMap = new HashMap<>();
     }
 }

 public CSVMatcher getAllMatchingRecord()
 {
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
     equalsObjectMap.put(header,match);
     return this;
 }


 public List<CSVRecord> build()
 {
     for(CSVRecord csvRecord : csvParser)
     {
         boolean isMatchFound = false;
         for (Map.Entry<Object, Object> entry : equalsObjectMap.entrySet()) {
             Object header = entry.getKey();
             Object values = entry.getValue();
             String headerValue = csvRecord.get(header.toString());
             if (headerValue.equalsIgnoreCase(values.toString())) {
                 isMatchFound=true;
             } else {
                 break;
             }           

         }
         if(isMatchFound)
             matchingCSVRecordList.add(csvRecord);


     }
     return matchingCSVRecordList;
 }

}
