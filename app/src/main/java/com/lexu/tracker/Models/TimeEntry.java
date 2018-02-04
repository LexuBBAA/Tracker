package com.lexu.tracker.Models;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeEntry implements Serializable {
    private static final String DATE_FORMAT = "dd-MMM-yyyy";

    private int mID;
    private String mTitle; //optional
    private String mDescription; //optional
    private Calendar mDate; //mandatory
    private int mSpentHours; //mandatory
    private int mSpentMinutes; //mandatory

    private TimeEntry() {

    }

    public TimeEntry(@Nullable String title, @Nullable String description, Calendar date, int spentHours, int spentMinutes) {
        mID = -1;
        mTitle = title;
        mDescription = description;
        mDate = date;
        mSpentHours = spentHours;
        mSpentMinutes = spentMinutes;
    }

    public TimeEntry(int id, @Nullable String title, @Nullable String description, Calendar date, int spentHours, int spentMinutes) {
        mID = id;
        mTitle = title;
        mDescription = description;
        mDate = date;
        mSpentHours = spentHours;
        mSpentMinutes = spentMinutes;
    }

    public long getID() {
        return mID;
    }

    public void setID(int ID) {
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

    public void addMinutes(int minutes) {
        if(minutes < 0 && mSpentMinutes + minutes < 0) {
            int totalMinutes = mSpentHours * 60 + mSpentMinutes;
            mSpentHours = 0;

            totalMinutes += minutes;

            mSpentHours = totalMinutes / 60;
            mSpentMinutes = totalMinutes % 60;
        } else {
            mSpentMinutes += minutes;
            if(mSpentMinutes >= 60) {
                mSpentHours += mSpentMinutes / 60;
                mSpentMinutes = mSpentMinutes % 60;
            }
        }
    }

    public void addHours(int hours) {
        mSpentHours += hours;
    }

    public void update(TimeEntry updatedEntry) {
        mTitle = updatedEntry.getTitle();
        mDescription = updatedEntry.getDescription();

        mSpentHours = updatedEntry.getSpentHours();
        mSpentMinutes = updatedEntry.getSpentMinutes();
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
