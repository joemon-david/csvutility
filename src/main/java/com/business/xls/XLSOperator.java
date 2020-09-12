package com.business.xls;

import com.config.path.FilePath;
import com.tool.excel.ExcelMatcher;

import java.util.*;


public class XLSOperator {

    public static void main(String[] args) {
        LinkedHashMap<Integer, LinkedHashMap<String, Object>> result = ExcelMatcher.getInstance(FilePath.masterFilePath, FilePath.masterSheetName)
                .getAllMatchingRecord().where().headerValueEquals("name","userStatusRowTapped")
                .and().headerValueEquals("timestamp","9/14/2020").build();
        List<String> outputHeaders = Arrays.asList(new String[]{"timestamp","name", "application_Version","client_Type"});

        result.forEach( (index,map)-> {
            System.out.println("============================================================================");
            map.forEach( (key,value)-> {
                if(outputHeaders.contains(key))
                System.out.print("| "+value+"\t"+"|");
            });
            System.out.println("============================================================================");

        });
    }
}
