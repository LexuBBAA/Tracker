package com.lexu.tracker.Managers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.lexu.tracker.Models.TimeEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatabaseProvider extends AsyncTask<Void, Void, Void> {

    private static final String DATABASE_NAME = "TRACKER_DB";
    private static final String TIMES_TABLE_NAME = "TRACKER_TIMES";

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private OnDatabaseCallback<TimeEntry> mCallback;
    SQLiteDatabase mDB;

    private int queryType = 0;

    private ArrayList<TimeEntry> data = new ArrayList<TimeEntry>();

    DatabaseProvider(Context context, OnDatabaseCallback<TimeEntry> callback) {
        mContext = context;
        mCallback = callback;
    }

    void setCallback(OnDatabaseCallback<TimeEntry> callback) {
        mCallback = callback;
    }

    public DatabaseProvider with(int queryType) {
        this.queryType = queryType;
        return this;
    }

    @Override
    protected void onPreExecute() {
        mDB = mContext.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        mDB.execSQL(DatabaseQueryBuilder.buildCreateTableQuery(TIMES_TABLE_NAME));

        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String query = "";
        switch (this.queryType) {
            case 1:     //Retrieve this month's data
            case 2:     //Retrieve this week's data
            case 3:     //Retrieve today's data
                query = DatabaseQueryBuilder.buildSelectByPeriodQuery(TIMES_TABLE_NAME, this.queryType);
                break;

            default:     //Retrieve all data
                query = DatabaseQueryBuilder.buildSelectAllQuery(TIMES_TABLE_NAME);
                break;
        }

        Cursor cursor = mDB.rawQuery(query, null);
        int idIndex = cursor.getColumnIndex(DatabaseQueryBuilder._ID_COLUMN_NAME);
        int titleIndex = cursor.getColumnIndex(DatabaseQueryBuilder.TITLE_COLUMN_NAME);
        int descriptionIndex = cursor.getColumnIndex(DatabaseQueryBuilder.DESCRIPTION_COLUMN_NAME);
        int dateIndex = cursor.getColumnIndex(DatabaseQueryBuilder.DATE_COLUMN_NAME);
        int hoursIndex = cursor.getColumnIndex(DatabaseQueryBuilder.HOURS_COLUMN_NAME);
        int minutesIndex = cursor.getColumnIndex(DatabaseQueryBuilder.MINUTES_COLUMN_NAME);

        this.data = new ArrayList<TimeEntry>();

        if(!cursor.moveToFirst()) {
            return null;
        }

        do {
            int rId = cursor.getInt(idIndex);
            String rTitle = cursor.getString(titleIndex);
            String rDescription = cursor.getString(descriptionIndex);
            String rDateString = cursor.getString(dateIndex);
            int rHours = cursor.getInt(hoursIndex);
            int rMinutes = cursor.getInt(minutesIndex);

            Date parsedDate = null;
            try {
                 parsedDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(rDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(parsedDate != null) {
                Calendar rDate = Calendar.getInstance();
                rDate.setTime(parsedDate);

                TimeEntry rEntry = new TimeEntry(rId, rTitle, rDescription, rDate, rHours, rMinutes);
                this.data.add(rEntry);
            }
        } while (cursor.moveToNext());

        cursor.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mCallback.onDataLoaded(this.data);

        super.onPostExecute(aVoid);
    }

    long insertRecord(TimeEntry record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseQueryBuilder.TITLE_COLUMN_NAME, record.getTitle());
        contentValues.put(DatabaseQueryBuilder.DESCRIPTION_COLUMN_NAME, record.getDescription());
        contentValues.put(DatabaseQueryBuilder.DATE_COLUMN_NAME, record.getDate());
        contentValues.put(DatabaseQueryBuilder.HOURS_COLUMN_NAME, record.getSpentHours());
        contentValues.put(DatabaseQueryBuilder.MINUTES_COLUMN_NAME, record.getSpentMinutes());
        contentValues.put(DatabaseQueryBuilder.CREATED_DATE_COLUMN_NAME,
                new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", Locale.getDefault())
                .format(Calendar.getInstance().getTime())
        );

        return mDB.insert(DatabaseProvider.TIMES_TABLE_NAME, null, contentValues);
    }

    boolean updateRecord(long id, TimeEntry record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseQueryBuilder.TITLE_COLUMN_NAME, record.getTitle());
        contentValues.put(DatabaseQueryBuilder.DESCRIPTION_COLUMN_NAME, record.getDescription());
        contentValues.put(DatabaseQueryBuilder.DATE_COLUMN_NAME, record.getDate());
        contentValues.put(DatabaseQueryBuilder.HOURS_COLUMN_NAME, record.getSpentHours());
        contentValues.put(DatabaseQueryBuilder.MINUTES_COLUMN_NAME, record.getSpentMinutes());

        String selection = DatabaseQueryBuilder._ID_COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        return mDB.update(TIMES_TABLE_NAME, contentValues, selection, selectionArgs) != 0;
    }

    boolean deleteRecord(long id) {
        String selection = DatabaseQueryBuilder._ID_COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        return mDB.delete(TIMES_TABLE_NAME, selection, selectionArgs) != 0;
    }

    void close() {
        mDB.close();
    }

    public static class Builder {
        private Context mContext;
        private DatabaseProvider mDatabaseProvider;
        private OnDatabaseCallback<TimeEntry> mOnDatabaseCallback;

        private static Builder INSTANCE = new Builder();

        public static Builder getInstance() {
            return INSTANCE;
        }

        private Builder() {
        }

        public Builder with(Context context) {
            mContext = context;
            return this;
        }

        public Builder callback(OnDatabaseCallback<TimeEntry> callback) {
            mOnDatabaseCallback = callback;
            return this;
        }

        public DatabaseProvider build() {
            if(mDatabaseProvider == null) {
                mDatabaseProvider = new DatabaseProvider(mContext, mOnDatabaseCallback);
                return mDatabaseProvider;
            }

            mDatabaseProvider.setCallback(mOnDatabaseCallback);
            return mDatabaseProvider;
        }

        public long insert(TimeEntry record) {
            return mDatabaseProvider.insertRecord(record);
        }

        public boolean update(long id, TimeEntry record) {
            return mDatabaseProvider.updateRecord(id, record);
        }

        public boolean delete(long id) {
            return mDatabaseProvider.deleteRecord(id);
        }

        public void closeDatabase() {
            mDatabaseProvider.close();
        }
    }
}
