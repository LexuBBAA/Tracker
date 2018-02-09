/*
 * Copyright (c)  Bogdan Andrei Alexandru Birsasteanu 2018.
 * All rights are reserved by Bogdan Andrei Alexandru Birsasteanu.
 * This is an open-source code, and it can be used as reference for various projects.
 */

package com.lexu.tracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lexu.tracker.Models.TimeEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

interface OnTimeUpdateCallback {
    void onUpdate(int minutes, int seconds);

    void onStop(TimeEntry newTimeEntry);
}

public class Utils {
}

class TimeEntryListAdapter extends ArrayAdapter<TimeEntry> {
    private ArrayList<TimeEntry> data;
    private Context context;

    TimeEntryListAdapter(@NonNull Context context, int resource, ArrayList<TimeEntry> data) {
        super(context, resource);

        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return convertView == null ?
//                new TimeEntryView(getContext(), this.data.get(position)) : convertView;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_time_entry, null);
        }

        TextView title = convertView.findViewById(R.id.time_entry_title);
        TextView description = convertView.findViewById(R.id.time_entry_description);
        TextView date = convertView.findViewById(R.id.time_entry_date);
        TextView time = convertView.findViewById(R.id.time_entry_duration);

        TimeEntry mEntry = this.data.get(position);

        title.setText((mEntry.getTitle().length() == 0) ? mEntry.getDate() : mEntry.getTitle());
        description.setText(mEntry.getDescription());
        date.setText(mEntry.getDate());
        time.setText(String.format(Locale.getDefault(), "%dh %dm", mEntry.getSpentHours(), mEntry.getSpentMinutes()));

        return convertView;
    }

    @NonNull
    @Override
    public Context getContext() {
        return this.context;
    }

    @Override
    public int getCount() {
        return this.data == null ? 0 : this.data.size();
    }

    @Nullable
    @Override
    public TimeEntry getItem(int position) {
        if (data == null) {
            return null;
        }

        return position >= data.size() ? null : data.get(position);
    }

    public void notifyDataSetChanged(ArrayList<TimeEntry> newData) {
        this.data = newData;
        this.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

class PauseRange {
    private Date mStartTime = null;
    private Date mStopTime = null;

    public PauseRange(Date startTime) {
        mStartTime = startTime;
    }

    public static int computePause(Date d1, Date d2) {
        return (int) ((d1.getTime() - d2.getTime()) / 60 / 1000);
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
}