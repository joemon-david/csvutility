package com.helper.db;

import java.sql.SQLException;

public class MSSQLUtility {

    public static void main(String[] args) throws SQLException {
        SQLExecuter sql = SQLExecuter.createSQLServerExecutor("","","","",
                "","","");
        System.out.println(sql.selectSingleValue("Select userName from [heartbeat].[dbo].[hb_user] where userId=1"));
    }
}
