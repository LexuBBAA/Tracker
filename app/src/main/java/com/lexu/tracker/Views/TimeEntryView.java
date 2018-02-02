package com.lexu.tracker.Views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lexu.tracker.R;
import com.lexu.tracker.Models.TimeEntry;

import java.util.Locale;

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

        TextView title = (TextView) root.findViewById(R.id.time_entry_title);
        TextView description = (TextView) root.findViewById(R.id.time_entry_description);
        TextView date = (TextView) root.findViewById(R.id.time_entry_date);
        TextView time = (TextView) root.findViewById(R.id.time_entry_duration);

        title.setText(mEntry.getTitle());
        description.setText(mEntry.getDescription());
        date.setText(mEntry.getDate());
        time.setText(String.format(Locale.getDefault(),"%dh %dm", mEntry.getSpentHours(), mEntry.getSpentMinutes()));
    }
}