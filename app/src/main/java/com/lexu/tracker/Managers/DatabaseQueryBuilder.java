package com.lexu.tracker.Managers;

class DatabaseQueryBuilder {

    static final String _ID_COLUMN_NAME = "_Id";
    static final String TITLE_COLUMN_NAME = "Title";
    static final String DESCRIPTION_COLUMN_NAME = "Description";
    static final String DATE_COLUMN_NAME = "Date";
    static final String HOURS_COLUMN_NAME = "Hours";
    static final String MINUTES_COLUMN_NAME = "Minutes";

    static String buildCreateTableQuery(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "_Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Title VARCHAR(255), " +
                "Description VARCHAR(255), " +
                "Date DATE NOT NULL, " +
                "Hours NUMBER(8, 0), " +
                "Minutes NUMBER(8, 0)" +
                ")";
    }

    static String buildSelectAllQuery(String tableName) {
        return "SELECT * FROM " + tableName + " ORDER BY Date";
    }

    static String buildSelectByPeriodQuery(String tableName, int period) {
        switch (period) {
            case 1:     //Requesting entries tracked this month
                return "SELECT * FROM " + tableName + " " +
                        "WHERE DATEDIFF(m, " + DATE_COLUMN_NAME + ", GETDATE()) = 0 " +
                        "ORDER BY Date";

            case 2:     //Requesting entries tracked today
                return "SELECT * FROM " + tableName + " " +
                        "WHERE DATEDIFF(d, " + DATE_COLUMN_NAME + ", GETDATE()) = 0 " +
                        "ORDER BY Date";

            default:    //Requesting entries tracked this week
                return "SELECT * FROM " + tableName + " " +
                        "WHERE DATEDIFF(ww, " + DATE_COLUMN_NAME + ", GETDATE()) = 0 " +
                        "ORDER BY Date";
        }
    }
}
