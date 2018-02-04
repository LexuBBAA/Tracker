package com.lexu.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.lexu.materialexu.OnNavigationListener;
import com.lexu.materialexu.Toolbar;
import com.lexu.tracker.Models.TimeEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int TRACKER_REQUEST_CODE_TRACK = 1001;
    public static final int TRACKER_RESULT_CODE_TRACK = 1002;
    public static final int TRACKER_RESULT_CODE_EDIT = 1003;

    public static final String TRACKER_NEW_ENTRY_KEY = "TRACKER_NEW_ENTRY";

    private ArrayList<TimeEntry> mData = new ArrayList<TimeEntry>();

    private TimeEntryListAdapter mListAdapter;
    private TextView noRecordsMessage;
    private ListView timeEntryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnNavigationListener(new OnNavigationListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onBackPressed();
            }
        });

        TextView currentDate = (TextView) findViewById(R.id.main_date);
        currentDate.setText(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                        .format(Calendar.getInstance().getTime())
        );

        noRecordsMessage = (TextView) findViewById(R.id.main_no_records_message);

        timeEntryList = (ListView) findViewById(R.id.main_entry_list);
        mListAdapter = new TimeEntryListAdapter(this, R.layout.layout_time_entry, mData);
        timeEntryList.setAdapter(mListAdapter);

        if(mData.size() == 0) {
            noRecordsMessage.setVisibility(View.VISIBLE);
            timeEntryList.setVisibility(View.GONE);
        } else {
            noRecordsMessage.setVisibility(View.GONE);
            timeEntryList.setVisibility(View.VISIBLE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_add_entry);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigate = new Intent(MainActivity.this, TrackActivity.class);
                startActivityForResult(navigate, TRACKER_REQUEST_CODE_TRACK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == TRACKER_REQUEST_CODE_TRACK && resultCode == TRACKER_RESULT_CODE_TRACK) {
            TimeEntry newEntry = (TimeEntry) data.getSerializableExtra(TRACKER_NEW_ENTRY_KEY);
            if(newEntry != null) {
                if(mData.size() == 0) {
                    this.noRecordsMessage.setVisibility(View.GONE);
                    this.timeEntryList.setVisibility(View.VISIBLE);
                }

                mData.add(0, newEntry);
                mListAdapter.notifyDataSetChanged(mData);
            }
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }
}
