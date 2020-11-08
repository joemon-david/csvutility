package com.tool.common.report.excel;


import com.config.path.ConfigParams;
import com.tool.excel.ExcelWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ExcelReportCreator implements ConfigParams {


  private LinkedHashMap<Integer,LinkedHashMap<String,String>> tableData = new LinkedHashMap<>();
  private int columnIndex=0;


   public void addNewRowObject(int rowIndex)
   {
       columnIndex=0;
       tableData.put(rowIndex,new LinkedHashMap<String,String>());
   }

   public void addRowData(int rowIndex,String column1Header,Object column1Value,String column2Header,Object column2Value)
   {
       columnIndex++;
       String value1 = (column1Value==null)?"Not Available":column1Value.toString();
       String value2 = (column2Value==null)?"Not Available":column2Value.toString();
       String result;
       if(value1.equalsIgnoreCase(value2))
            result = COMPARE_RESULT_MATCHES;
       else if (value1.equalsIgnoreCase("Not Available") || value2.equalsIgnoreCase("Not Available"))
            result = "Value Missing";
       else
           result = COMPARE_RESULT_MIS_MATCHES;


       if(tableData.containsKey(rowIndex))
       {
           tableData.get(rowIndex).put(column1Header,value1);
           tableData.get(rowIndex).put(column2Header,value2);
           tableData.get(rowIndex).put("Result_"+column1Header,result);
       }else
       {
           addNewRowObject(rowIndex);
           tableData.get(rowIndex).put(column1Header,value1);
           tableData.get(rowIndex).put(column2Header,value2);
           tableData.get(rowIndex).put("Result_"+column1Header,result);
       }
   }

   public LinkedHashMap<Integer,LinkedHashMap<String,String>> getReportData()
   {
       return tableData;
   }

    public static void main(String[] args) throws IOException {
        ArrayList<HashMap<String,Object>> recList = new ArrayList<HashMap<String,Object>>();
        HashMap<String,Object> dbList = new HashMap<>();
        HashMap<String,Object> recordList1 = new HashMap<>();
        recordList1.put("Security_Id",123);
        recordList1.put("TRADE_DATE","12/10/2020");
        recordList1.put("SETTLEMENT_DATE","10/10/2020");
        HashMap<String,Object> recordList2 = new HashMap<>();
        recordList2.put("Security_Id",240);
        recordList2.put("TRADE_DATE","12/10/2020");
        recordList2.put("SETTLEMENT_DATE","10/10/2020");
        HashMap<String,Object> recordList3 = new HashMap<>();
        recordList3.put("Security_Id",123);
        recordList3.put("TRADE_DATE","12/10/2020");
        recordList3.put("SETTLEMENT_DATE","10/10/2020");
        dbList.put("Security_Id",123);
        dbList.put("TRADE_DATE","13/10/2020");
        dbList.put("SETTLEMENT_DATE","13/10/2020");
        recList.add(recordList1);
        recList.add(recordList2);
        recList.add(recordList3);
        ExcelReportCreator reporter = new ExcelReportCreator();
        for(int i=0;i<recList.size();i++)
        {
            HashMap<String,Object> recordList =recList.get(i);
            for(String key:recordList.keySet())
            {
                reporter.addRowData(i,"CSV_"+key,recordList.get(key),"DB_"+key,dbList.get(key));

            }
        }

        new ExcelWriter().writeDataToExcelSheet("data//output//excelReport.xls",reporter.getReportData(),"Report");

    }

    public void printReportData(LinkedHashMap<Integer,LinkedHashMap<String,String>> tableData)
    {
        System.out.print("||");
        tableData.get(0).forEach((key,value) -> System.out.print(key+"\t ||"));
        System.out.println();
        for(int inedex: tableData.keySet())
        {
            System.out.print("||");
            HashMap<String,String> data = tableData.get(inedex);
            data.forEach((key,value) -> System.out.print(value+"\t ||"));
            System.out.println();

        }
    }




}
