package com.swaglabs.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbConectivity {
    /**
     * Method to execute DB query
     *
     * @throws SQLException
     *             SQL exception
     * @throws ClassNotFoundException
     *             in case class not found
     */
    public static List<HashMap<String, Object>> dbQuery(Map<String, String> details, String query) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        List<HashMap<String, Object>> rs1 = null;
        try (Connection connection = DriverManager.getConnection(details.get("URL"), details.get("USERNAME"),
                details.get("PASSWORD"));
             Statement stmt1 = connection.createStatement();

             ResultSet rs = stmt1.executeQuery(query)) {
            rs1 = convertResultSetToList(rs);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rs1;
    }

    public static List<HashMap<String, Object>> dbParamQuery(Map<String, String> details, String query,
                                                             String parameter) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        String queryParam = query + parameter + ")";
        List<HashMap<String, Object>> rs1 = null;
        try (Connection connection = DriverManager.getConnection(details.get("URL"), details.get("USERNAME"),
                details.get("PASSWORD"));
             Statement stmt1 = connection.createStatement();
             ResultSet rs = stmt1.executeQuery(queryParam)) {
            rs1 = convertResultSetToList(rs);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rs1;
    }

    /**
     * Method to convert Result set to List of HashMaps
     *
     * @param rs
     *            result set to fetch data
     * @return List of Hash map
     * @throws SQLException
     *             in case resultset i null
     */

    public static List<HashMap<String, Object>> convertResultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<String, Object>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }

        return list;
    }

    public static void updateDbQuery(Map<String, String> details, String query) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        Connection connection = null;
        Statement stmt1 = null;
        try {
            connection = DriverManager.getConnection(details.get("URL"), details.get("USERNAME"),
                    details.get("PASSWORD"));
            stmt1 = connection.createStatement();
            stmt1.executeUpdate(query);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Message while executing the query: " + query);
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (stmt1 != null) {
                try {
                    stmt1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
