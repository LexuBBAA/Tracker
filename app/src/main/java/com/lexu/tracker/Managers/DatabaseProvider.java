package com.lexu.tracker.Managers;

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

    private Context mContext;
    private OnDatabaseCallback<TimeEntry> mCallback;
    private SQLiteDatabase mDB;

    private ArrayList<TimeEntry> data = new ArrayList<TimeEntry>();

    DatabaseProvider(Context context, OnDatabaseCallback<TimeEntry> callback) {
        mContext = context;
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        mDB = mContext.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        mDB.execSQL(DatabaseQueryBuilder.buildCreateTableQuery(TIMES_TABLE_NAME));

        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Cursor cursor = mDB.rawQuery(DatabaseQueryBuilder.buildSelectAllQuery(TIMES_TABLE_NAME), null);
        int idIndex = cursor.getColumnIndex("");
        int titleIndex = cursor.getColumnIndex("");
        int descriptionIndex = cursor.getColumnIndex("");
        int dateIndex = cursor.getColumnIndex("");
        int hoursIndex = cursor.getColumnIndex("");
        int minutesIndex = cursor.getColumnIndex("");

        this.data = new ArrayList<TimeEntry>();

//        cursor.moveToFirst();

        if(!cursor.moveToFirst()) {
            return null;
        }

        do {
            String rId = cursor.getString(idIndex);
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

    public static class Builder {
        private Context mContext;
        private DatabaseProvider mDatabaseProvider;
        private OnDatabaseCallback<TimeEntry> mOnDatabaseCallback;

        public Builder() {
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
            return new DatabaseProvider(mContext, mOnDatabaseCallback);
        }
    }
}
