package com.helper.db;

import java.sql.*;

public class SQLExecuter {

    private static String VALIDITY_CHECK_SQL = "select 1";

    private String connStr;
    private String connUser;
    private String connPassword;


    /**
     * Initializes and configures SQLExecuter. Please note that the constructor doesn't make any connection to the database but simply sets the properties.
     *
     * @param connStr      - Database connection string
     * @param connUser     - Database user
     * @param connPassword - Database password
     */
    public SQLExecuter(String connStr, String connUser, String connPassword) {
        this.connStr = connStr;
        this.connUser = connUser;
        this.connPassword = connPassword;
    }

    /**
     * Convenience factory method to create a SQLExecuter that is specific to Microsoft SQL server.
     * Uses jtds driver from http://jtds.sourceforge.net/ (must be in the classpath

     *
     * @param server   - SQL Server address or IP (required). Example: sqlserver.mhqa.pub
     * @param dbName   - Database Name (required): Example: heartbeat
     * @param port     - Database port (optional). Example: 1433
     * @param instance - Database Instance Name (optional). Example: mhcurese
     * @param userName - Database username(if using SQL authentication) or domain username(if using Windows authentication)
     * @param password - Password for user
     * @param domain   - Authentication domain (optional). Required if using Windows Authentication. Example: heartbeat
     * @return SQLExecuter
     */
    public static SQLExecuter createSQLServerExecutor(String server, String dbName, String port, String instance,
                                                                      String userName, String password, String domain) {

        String portString = "";

        if (port != null && port.length() != 0 && !port.equals("-")) {
            portString = ":" + port;
        }

        //String driverName="net.sourceforge.jtds.jdbc.Driver";
        String connStr = "jdbc:jtds:sqlserver://" + server + portString + ";DatabaseName=" + dbName + ";";
        if (instance != null && instance.length() != 0 & !instance.equals("-")) {
            connStr += "instance=" + instance + ";";
        }
        if (domain != null && domain.length() != 0 && !domain.equals("-")) {
            connStr += "domain=" + domain + ";";
        }
        return new SQLExecuter(connStr, userName, password);

    }

    /**
     * Triet connect to the database for until maxMillisecondsToWait. if a connection is succesful within maxMillisecondsToWait,
     * returns true. Otherwise returns false.
     *
     * @param maxMillisecondsToWait -Please note that,if maxMillisecondsToWait is less than DriverManager.getLoginTime, then
     *                              DriverManager.getLoginTime is used as maxMillisecondsToWait
     * @return true is database ready and false otherwise
     */
    public boolean waitForDatabaseReady(long maxMillisecondsToWait) {

        long t1 = System.currentTimeMillis();

        long totalWaitedMilliSeconds = 0;
        long sleepBetweenRetries = 1000;
        while (true) {

            if (checkDatabaseConnectivity()) {
                long t2 = System.currentTimeMillis();
                log("waitForDatabaseReady took " + (t2 - t1) / 1000 + " secs");

                return true;
            }

            try {
                Thread.sleep(sleepBetweenRetries);
                totalWaitedMilliSeconds += sleepBetweenRetries;
                if (totalWaitedMilliSeconds >= maxMillisecondsToWait) {
                    return false;
                }

            } catch (InterruptedException e) {
                return false;
            }
        }

    }

    /**
     * Executes a select statements that returns a single value (such as select count(*))
     *
     * @param sql - SQL to be executed
     * @return - The value returned by the SQL execution
     * @throws SQLException
     */
    public Object selectSingleValue(String sql) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            return selectSingleValue(connection, sql);
        } finally {
            cleanup(connection, null, null);
        }
    }


    /**
     * Executes  sql statement such as an update,insert or delete
     *
     * @param sql - SQL to execute
     * @return - number of rows updated/inserted/deleted
     * @throws SQLException
     */
    public int executeUpdate(String sql) throws SQLException {
        Connection connection = null;
        Statement stmt = null;

        try {
            connection = getConnection();
            stmt = connection.createStatement();
            return stmt.executeUpdate(sql);
        } finally {
            cleanup(connection, stmt, null);
        }
    }

    /**
     * Executes a stored proc with an optional single string parameter
     * @param procName - stored proc name(sp_dothis)
     * @param paramName - paramater name or null if there are no parameters
     * @param paramValue - parameter value. ignored if paramName is null
     * @throws SQLException
     */
    public void executeStoredProc(String procName, String paramName, String paramValue) throws SQLException {

        Connection connection = null;
        CallableStatement stmt = null;
        try {
            long t1 = System.currentTimeMillis();

            connection = getConnection();

            String procCommand = buildProcCommand(procName, paramName);
            stmt = connection.prepareCall(procCommand);
            if (paramName != null) {
                stmt.setObject(paramName, paramValue);
            }

            stmt.execute();
            stmt.getMoreResults();
            long t2 = System.currentTimeMillis();
            log("executeStoredProc: " + procName + ", took " + (t2 - t1) / 1000 + " secs");

        } finally {
            cleanup(connection, stmt, null);
        }

    }

    private String buildProcCommand(String procName, String paramName) {
        return paramName == null ? "{call " + procName + "}" : "{call " + procName + "(?)}";
    }

    private boolean checkDatabaseConnectivity() {
        try {
            selectSingleValue(VALIDITY_CHECK_SQL);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    private Object selectSingleValue(Connection connection, String sql) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                return rs.getObject(1);
            }
        } finally {
            cleanup(null, stmt, rs);
        }
        return null;
    }


    private Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(connStr, connUser, connPassword);
        connection.setAutoCommit(true);
        return connection;
    }


    private void cleanup(Connection connection, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
        }
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    //log("Connection closed");
                }
            } catch (SQLException e) {
            }
        }
    }

    private void log(String message) {
        //System.out.println(this.getClass() + ":" + connStr + ":" + message);
    }

}
