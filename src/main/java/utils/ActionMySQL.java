package utils;

import entity.Worker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

public class ActionMySQL {

    private static final Logger LOG = Logger.getLogger(ActionMySQL.class.getName());

    public static boolean createDB(Statement stmt, String dbName) {
        String sqlDB = format("CREATE DATABASE IF NOT EXISTS %s", dbName);
        try {
            stmt.executeUpdate(sqlDB);
        } catch (Exception ex) {
            LOG.log(Level.INFO, "Database " + dbName + "created fail");
            return false;
        }
        LOG.log(Level.INFO, "Database " + dbName + "created successfully");
        return true;
    }

    public static void createTable(Statement stmt, String dbUseName, String dbCreateTable) throws SQLException {
        stmt.executeUpdate(dbUseName);
        stmt.executeUpdate(dbCreateTable);
        LOG.log(Level.INFO, "Table  created successfully...");
    }

    public static void insertInToTable(Statement stmt, String dbInsertTable) throws SQLException {
        stmt.executeUpdate(dbInsertTable);
        LOG.log(Level.INFO, "Data inserted in to table");
    }

    public static boolean cleanAndDeleteTable(Statement stmt, String tableName) {
        String sqlCleanTable = format("TRUNCATE TABLE %s", tableName);
        String sqlDeleteTable = format("DROP TABLE %s ", tableName);
        try {
            stmt.executeUpdate(sqlCleanTable);
            stmt.executeUpdate(sqlDeleteTable);
        } catch (Exception ex) {
            LOG.log(Level.INFO, "Table " + tableName + "clean and delete fail");
            return false;
        }
        LOG.log(Level.INFO, "Table " + tableName + "clean and delete completed");
        return true;
    }


    public static boolean deleteDB(Statement stmt, String dbName) {
        String sql = format("DROP DATABASE %s", dbName);
        try {
            stmt.executeUpdate(sql);
        } catch (Exception ex) {
            LOG.log(Level.INFO, "Database " + dbName + "deleted fail");
            return false;
        }
        LOG.log(Level.INFO, "Database " + dbName + "deleted successfully");
        return true;
    }

    public static Worker createWorker(ResultSet rs) throws SQLException {

        int id = rs.getInt("Id");
        String name = rs.getString("firstName");
        String position = rs.getString("position");
        int salary = rs.getInt("salary");
        return new Worker(id, name, position, salary);
    }

}
