package com.lexu.tracker;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeEntry {
    private String mTitle;
    private String mDescription;
    private Calendar mDate;
    private int mSpentHours;
    private int mSpentMinutes;

    public TimeEntry(@Nullable String title, @Nullable String description, Calendar date, int spentHours, int spentMinutes) {
        mTitle = title;
        mDescription = description;
        mDate = date;
        mSpentHours = spentHours;
        mSpentMinutes = spentMinutes;
    }

    public String getTitle() {
        if(mTitle == null) {
            String returnValue = "";

            SimpleDateFormat dateFormat = SimpleDateFormat.getDateInstance();
            mDate.getTime()
        }

        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Calendar getDate() {
        return mDate;
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
}
