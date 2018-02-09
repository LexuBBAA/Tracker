/*
 * Copyright (c)  Bogdan Andrei Alexandru Birsasteanu 2018.
 * All rights are reserved by Bogdan Andrei Alexandru Birsasteanu.
 * This is an open-source code, and it can be used as reference for various projects.
 */

package com.lexu.tracker.Views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lexu.tracker.Models.TimeEntry;
import com.lexu.tracker.R;

import java.util.Locale;

@Deprecated
public class TimeEntryView extends ConstraintLayout {
    private TimeEntry mEntry = null;

    public TimeEntryView(Context context) {
        super(context);
    }

    public TimeEntryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeEntryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimeEntryView(Context context, TimeEntry entry) {
        super(context);
        mEntry = entry;
        createView();
    }

    private void createView() {
        Context context = getContext();
        View root = LayoutInflater.from(context).inflate(R.layout.layout_time_entry, null);

        TextView title = root.findViewById(R.id.time_entry_title);
        TextView description = root.findViewById(R.id.time_entry_description);
        TextView date = root.findViewById(R.id.time_entry_date);
        TextView time = root.findViewById(R.id.time_entry_duration);

        title.setText(mEntry.getTitle());
        description.setText(mEntry.getDescription());
        date.setText(mEntry.getDate());
        time.setText(String.format(Locale.getDefault(), "%dh %dm", mEntry.getSpentHours(), mEntry.getSpentMinutes()));
    }
}