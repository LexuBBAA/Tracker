package com.lexu.tracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.lexu.tracker.Models.TimeEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView currentDate = (TextView) findViewById(R.id.main_date);
        currentDate.setText(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                        .format(Calendar.getInstance())
        );

        ListView timeEntryList = (ListView) findViewById(R.id.main_entry_list);
        timeEntryList.setAdapter(new TimeEntryListAdapter(this, R.layout.layout_time_entry, new ArrayList<TimeEntry>()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_add_entry);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: navigate to Create Time Entry Screen
            }
        });
    }
}
