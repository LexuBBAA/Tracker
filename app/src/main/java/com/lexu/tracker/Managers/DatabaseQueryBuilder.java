package com.lexu.tracker.Managers;

class DatabaseQueryBuilder {

    static final String _ID_COLUMN_NAME = "_Id";
    static final String TITLE_COLUMN_NAME = "Title";
    static final String DESCRIPTION_COLUMN_NAME = "Description";
    static final String DATE_COLUMN_NAME = "Date";
    static final String HOURS_COLUMN_NAME = "Hours";
    static final String MINUTES_COLUMN_NAME = "Minutes";
    static final String CREATED_DATE_COLUMN_NAME = "CreatedDate";

    static String buildCreateTableQuery(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                _ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE_COLUMN_NAME + " VARCHAR(255), " +
                DESCRIPTION_COLUMN_NAME + " VARCHAR(255), " +
                DATE_COLUMN_NAME + " DATE NOT NULL, " +
                HOURS_COLUMN_NAME + " NUMBER(8, 0), " +
                MINUTES_COLUMN_NAME + " NUMBER(8, 0), " +
                CREATED_DATE_COLUMN_NAME + " DATE NOT NULL" +
                ")";
    }

    static String buildSelectAllQuery(String tableName) {
        return "SELECT * FROM " + tableName + " ORDER BY " + CREATED_DATE_COLUMN_NAME + " DESC";
    }

    static String buildSelectByPeriodQuery(String tableName, int period) {
        switch (period) {
            case 1:     //Requesting entries tracked this month
                return "SELECT * FROM " + tableName + " " +
                        "WHERE DATEDIFF(m, " + DATE_COLUMN_NAME + ", GETDATE()) = 0 " +
                        "ORDER BY " + CREATED_DATE_COLUMN_NAME + " DESC";

            case 2:     //Requesting entries tracked today
                return "SELECT * FROM " + tableName + " " +
                        "WHERE DATEDIFF(d, " + DATE_COLUMN_NAME + ", GETDATE()) = 0 " +
                        "ORDER BY " + CREATED_DATE_COLUMN_NAME + " DESC";

            default:    //Requesting entries tracked this week
                return "SELECT * FROM " + tableName + " " +
                        "WHERE DATEDIFF(ww, " + DATE_COLUMN_NAME + ", GETDATE()) = 0 " +
                        "ORDER BY " + CREATED_DATE_COLUMN_NAME + " DESC";
        }
    }
}
