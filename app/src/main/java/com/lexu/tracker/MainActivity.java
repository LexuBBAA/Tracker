/*
 * Copyright (c)  Bogdan Andrei Alexandru Birsasteanu 2018.
 * All rights are reserved by Bogdan Andrei Alexandru Birsasteanu.
 * This is an open-source code, and it can be used as reference for various projects.
 */

package com.lexu.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lexu.materialexu.OnNavigationListener;
import com.lexu.materialexu.Toolbar;
import com.lexu.tracker.Managers.DatabaseProvider;
import com.lexu.tracker.Models.TimeEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int TRACKER_REQUEST_CODE_TRACK = 1001;
    public static final int TRACKER_RESULT_CODE_TRACK = 2001;
    public static final int TRACKER_RESULT_CODE_EDIT = 2002;
    public static final String TRACKER_DATA_KEY = "TRACKER_DATA";
    public static final String TRACKER_NEW_ENTRY_KEY = "TRACKER_NEW_ENTRY";
    private static final String TAG = "MainActivity";
    private static final int TRACKER_REQUEST_CODE_EDIT = 1002;
    private static final String TRACKER_UPDATE_POSITION = "TRACKER_UPDATE_POSITION";

    private ArrayList<TimeEntry> mData = new ArrayList<TimeEntry>();

    private TimeEntryListAdapter mListAdapter;
    private TextView noRecordsMessage;
    private ListView timeEntryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mData = (ArrayList<TimeEntry>) savedInstanceState.getSerializable(TRACKER_DATA_KEY);
        }

        Intent inboundData = getIntent();
        if (inboundData.hasExtra(TRACKER_DATA_KEY)) {
            mData = (ArrayList<TimeEntry>) inboundData.getSerializableExtra(TRACKER_DATA_KEY);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnNavigationListener(new OnNavigationListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onBackPressed();
            }
        });

        TextView currentDate = findViewById(R.id.main_date);
        currentDate.setText(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                        .format(Calendar.getInstance().getTime())
        );

        noRecordsMessage = findViewById(R.id.main_no_records_message);

        timeEntryList = findViewById(R.id.main_entry_list);
        mListAdapter = new TimeEntryListAdapter(this, R.layout.layout_time_entry, mData);
        timeEntryList.setAdapter(mListAdapter);

        if (mData.size() == 0) {
            noRecordsMessage.setVisibility(View.VISIBLE);
            timeEntryList.setVisibility(View.GONE);
        } else {
            noRecordsMessage.setVisibility(View.GONE);
            timeEntryList.setVisibility(View.VISIBLE);
        }

        timeEntryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent navigate = new Intent(MainActivity.this, EditTimeActivity.class);
                navigate.putExtra(TRACKER_NEW_ENTRY_KEY, mData.get(position));
                navigate.putExtra(TRACKER_UPDATE_POSITION, position);
                startActivityForResult(navigate, TRACKER_REQUEST_CODE_EDIT);
            }
        });

        FloatingActionButton fab = findViewById(R.id.main_add_entry);
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
        if (requestCode == TRACKER_REQUEST_CODE_TRACK && resultCode == TRACKER_RESULT_CODE_TRACK) {
            TimeEntry newEntry = (TimeEntry) data.getSerializableExtra(TRACKER_NEW_ENTRY_KEY);
            if (newEntry != null) {
                if (mData.size() == 0) {
                    this.noRecordsMessage.setVisibility(View.GONE);
                    this.timeEntryList.setVisibility(View.VISIBLE);
                }

                mData.add(0, newEntry);
                mListAdapter.notifyDataSetChanged(mData);
                return;
            }
        } else if (requestCode == TRACKER_REQUEST_CODE_EDIT && resultCode == TRACKER_RESULT_CODE_EDIT) {
            TimeEntry entry = (TimeEntry) data.getSerializableExtra(TRACKER_NEW_ENTRY_KEY);
            if (entry != null) {
                int pos = data.getIntExtra(TRACKER_UPDATE_POSITION, -1);
                if (pos < 0) {
                    Log.e(TAG, "onActivityResult: error retrieving position: " + pos);
                    return;
                }

                mData.get(pos).update(entry);
                mListAdapter.notifyDataSetChanged();
                return;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(TRACKER_DATA_KEY, mData);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        DatabaseProvider.Builder.getInstance().closeDatabase();
        super.onDestroy();
    }
}