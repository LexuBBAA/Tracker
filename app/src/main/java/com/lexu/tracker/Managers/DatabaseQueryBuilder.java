package com.lexu.tracker.Managers;

public class DatabaseQueryBuilder {
    public static String buildCreateTableQuery(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "Id VARCHAR(100) NOT NULL UNIQUE, " +
                "Title VARCHAR(255), " +
                "Description VARCHAR(255), " +
                "Date DATE NOT NULL, " +
                "Hours NUMBER(8, 0), " +
                "Minutes NUMBER(8, 0)" +
                ")";
    }

    public static String buildSelectAllQuery(String tableName) {
        return "SELECT * FROM " + tableName;
    }
}
