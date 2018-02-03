package com.lexu.tracker.Models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lexu.tracker.Utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeEntry implements Serializable {
    private static final String DATE_FORMAT = "dd-MMM-yyyy";

    private String mID;
    private String mTitle; //optional
    private String mDescription; //optional
    private Calendar mDate; //mandatory
    private int mSpentHours; //mandatory
    private int mSpentMinutes; //mandatory

    private TimeEntry() {

    }

    public TimeEntry(@Nullable String title, @Nullable String description, Calendar date, int spentHours, int spentMinutes) {
        mID = Utils.generateTimeEntryID();
        mTitle = title;
        mDescription = description;
        mDate = date;
        mSpentHours = spentHours;
        mSpentMinutes = spentMinutes;
    }

    public TimeEntry(@NonNull String id, @Nullable String title, @Nullable String description, Calendar date, int spentHours, int spentMinutes) {
        mID = id;
        mTitle = title;
        mDescription = description;
        mDate = date;
        mSpentHours = spentHours;
        mSpentMinutes = spentMinutes;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public String getTitle() {
        if(mTitle == null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            return dateFormat.format(mDate.getTime());
        }

        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription == null ? "" : mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return dateFormat.format(mDate.getTime());
    }

    public void setDate(Calendar date) {
        mDate = date;
    }

    public int getSpentHours() {
        return mSpentHours;
    }

    public void setSpentHours(int spentHours) {
        mSpentHours = spentHours;
    }

    public int getSpentMinutes() {
        return mSpentMinutes;
    }

    public void setSpentMinutes(int spentMinutes) {
        mSpentMinutes = spentMinutes;
    }

    public static class Builder {
        private TimeEntry mTimeEntry;

        public Builder() {
            mTimeEntry = new TimeEntry();
            mTimeEntry.setDate(Calendar.getInstance());
        }

        public Builder setTitle(String title) {
            mTimeEntry.setTitle(title);
            return this;
        }

        public Builder setDescription(String description) {
            mTimeEntry.setDescription(description);
            return this;
        }

        public Builder setHours(int hours) {
            mTimeEntry.setSpentHours(hours);
            return this;
        }

        public Builder setMinutes(int minutes) {
            mTimeEntry.setSpentMinutes(minutes);
            return this;
        }

        public TimeEntry build() {
            return mTimeEntry;
        }
    }
}
