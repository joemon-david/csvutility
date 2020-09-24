package com.helper.db;


import com.tool.csv.CSVWriter;
import com.tool.date.DateUtility;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class MSSQLUtility {

    public void writeResultSetAsCSV(ResultSet rs, String outputCSVFilePath, ArrayList<String> csvKeyValueList,String key) throws SQLException {

        ResultSetMetaData rsmd = rs.getMetaData();
        ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
        int columnCount = rsmd.getColumnCount();
        String [] headers = new String [columnCount];
        for(int i=1;i<=columnCount;i++)
            headers[i-1] = rsmd.getColumnName(i);
        while (rs.next())
     {
        if(csvKeyValueList.contains(rs.getString(key)))
        {
            ArrayList<String> rowData = new ArrayList<>();
            for(int i=1;i<=columnCount;i++)
            {
                int columnType = rsmd.getColumnType(i);
//                System.out.println(rsmd.getColumnName(i)+" - Column Type "+columnType);
                if(columnType == 93)
                rowData.add(DateUtility.convertSQLDate(rs.getDate(i)));
                else
                    rowData.add(rs.getString(i));
            }
            dataList.add(rowData);

        }
        new CSVWriter().writeArrayListAsCSV(outputCSVFilePath,dataList,headers);
     }
    }




    public static void main(String[] args) throws SQLException, IOException {
        SQLExecuter sql = SQLExecuter.createSQLServerExecutor("","","1433","",
                "joemon.david","","");

        String sqlQuery = "SELECT TOP 100 [bedId]\n" +
                "      ,[bayId]\n" +
                "      ,[locationId]\n" +
                "      ,[bedNumber]\n" +
                "      ,[bayName]\n" +
                "      ,[bedNumberPart]\n" +
                "      ,[phoneNumber]\n" +
                "      ,[bedStatus]\n" +
                "      ,[bedStatusTime]\n" +
                "      ,[bedStatusMessage]\n" +
                "      ,[bedStatusMessageTime]\n" +
                "      ,[isIsolation]\n" +
                "      ,[isolationDesc]\n" +
                "      ,[positionInBay]\n" +
                "      ,[sortOrder]\n" +
                "      ,[comments]\n" +
                "      ,[phone]\n" +
                "      ,[lastAlertEscalationTime]\n" +
                "      ,[isDefault]\n" +
                "      ,[deleted]\n" +
                "  FROM [heartbeat].[dbo].[hb_bed]";

        ResultSet rs = sql.executeQueryAndGetData(sqlQuery);
        ArrayList<String> bedIdList = new ArrayList<>(Arrays.asList("66","67","68","69","70"));
        new MSSQLUtility().writeResultSetAsCSV(rs,"data/output/bed.csv",bedIdList,"bedId");

//        while(rs.next())
//        {
//            StringBuilder sb = new StringBuilder();
//            System.out.println();
//            for(int i=1;i<20;i++)
//                sb.append(rs.getString(i)).append("\t");
//            System.out.println(sb.toString());
//
//        }
//        ScriptRunner runner = new ScriptRunner(sql.getConnection());
//        InputStreamReader reader = new InputStreamReader(new FileInputStream("sql/select_sound.sql"));
//        try {
//            runner.runScript(reader);
//
//        } finally {
//            reader.close();
//            runner.closeConnection();
//        }



    }
}
