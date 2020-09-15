package com.helper.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MSSQLUtility {

    public static void main(String[] args) throws SQLException {
        SQLExecuter sql = SQLExecuter.createSQLServerExecutor("I.P.Address","heartbeat","1433","UBER",
                "userName","Password","heartbeat");

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
        while(rs.next())
        {
            StringBuilder sb = new StringBuilder();
            System.out.println();
            for(int i=1;i<20;i++)
                sb.append(rs.getString(i)).append("\t");
            System.out.println(sb.toString());

        }
    }
}
