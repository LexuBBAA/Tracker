package com.lexu.tracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.lexu.tracker.Models.TimeEntry;
import com.lexu.tracker.Views.TimeEntryView;

import java.util.ArrayList;
import java.util.Date;

public class Utils {
    public static String generateTimeEntryID() {
        return "";
    }
}

class TimeEntryListAdapter extends ArrayAdapter<TimeEntry> {
    private ArrayList<TimeEntry> data;

    TimeEntryListAdapter(@NonNull Context context, int resource, ArrayList<TimeEntry> data) {
        super(context, resource);

        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return convertView == null ?
                new TimeEntryView(getContext(), this.data.get(position)) : convertView;
    }

    @Override
    public int getCount() {
        return this.data == null ? 0 : this.data.size();
    }

    @Nullable
    @Override
    public TimeEntry getItem(int position) {
        if(data == null) {
            return null;
        }

        return position >= data.size() ? null : data.get(position);
    }
}

interface OnTimeUpdateCallback {
    void onUpdate(int minutes, int seconds);
    void onStop(TimeEntry newTimeEntry);
}

class PauseRange {
    private Date mStartTime = null;
    private Date mStopTime = null;

    public PauseRange(Date startTime) {
        mStartTime = startTime;
    }

    public Date getStartTime() {
        return mStartTime;
    }

    public Date getStopTime() {
        return mStopTime;
    }

    public void setStopTime(Date stopTime) {
        mStopTime = stopTime;
    }

    public static int computePause(Date d1, Date d2) {
        return (int) ((d1.getTime() - d2.getTime()) / 60 / 1000);
    }
}