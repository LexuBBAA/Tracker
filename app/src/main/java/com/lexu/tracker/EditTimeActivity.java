/*
 * Copyright (c)  Bogdan Andrei Alexandru Birsasteanu 2018.
 * All rights are reserved by Bogdan Andrei Alexandru Birsasteanu.
 * This is an open-source code, and it can be used as reference for various projects.
 */

package com.lexu.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lexu.materialexu.OnNavigationListener;
import com.lexu.materialexu.Toolbar;
import com.lexu.tracker.Managers.DatabaseProvider;
import com.lexu.tracker.Models.TimeEntry;
import com.lexu.tracker.Views.EditTimeAlert;

import java.util.Locale;

public class EditTimeActivity extends AppCompatActivity implements View.OnClickListener, OnAlertOptionsListener {

    private static final String TAG = "EditTimeActivity";
    private TimeEntry mRecord = null;

    private TextView recordSpentTime;
    private EditTimeAlert editTimeAlert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnNavigationListener(new OnNavigationListener() {
            @Override
            public void onClick(View view) {
                EditTimeActivity.this.onBackPressed();
            }
        });

        mRecord = (TimeEntry) getIntent().getSerializableExtra(MainActivity.TRACKER_NEW_ENTRY_KEY);

        final EditText recordTitle = findViewById(R.id.edit_time_task_title);
        final EditText recordDescription = findViewById(R.id.edit_time_task_description);
        this.recordSpentTime = findViewById(R.id.edit_time_recorded_time);
        TextView recordDate = findViewById(R.id.edit_time_task_date);

        final Button saveButton = findViewById(R.id.edit_time_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setEnabled(false);

                if (v.getId() == saveButton.getId()) {
                    mRecord.setTitle(recordTitle.getText().toString());
                    mRecord.setDescription(recordDescription.getText().toString());

                    boolean updated = DatabaseProvider.Builder.getInstance().update(mRecord.getID(), mRecord);

                    if (updated) {
                        Intent result = getIntent();
                        result.putExtra(MainActivity.TRACKER_NEW_ENTRY_KEY, mRecord);
                        setResult(MainActivity.TRACKER_RESULT_CODE_EDIT, result);
                        finish();
                        return;
                    } else {
                        Toast.makeText(EditTimeActivity.this, "Could not save record", Toast.LENGTH_LONG).show();
                    }
                }

                saveButton.setEnabled(true);
            }
        });

        if (mRecord != null) {
            recordTitle.setText(mRecord.getTitle());
            recordDescription.setText(mRecord.getDescription());
            recordDate.setText(mRecord.getDate());
            this.recordSpentTime.setText(String.format(this.recordSpentTime.getText().toString(), mRecord.getSpentHours(), mRecord.getSpentMinutes()));

            this.recordSpentTime.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (this.editTimeAlert == null) {
            this.editTimeAlert = new EditTimeAlert.Builder()
                    .with(EditTimeActivity.this)
                    .setHours(mRecord.getSpentHours())
                    .setMinutes(mRecord.getSpentMinutes())
                    .build();
            this.editTimeAlert.setOnOptionListener(this);
        }

        this.editTimeAlert.show();
    }

    @Override
    public void onCancel() {
        EditTimeAlert.Builder.reset(this.editTimeAlert, mRecord.getSpentHours(), mRecord.getSpentMinutes());
        this.editTimeAlert.hide();
    }

    @Override
    public void onSave(final int hours, final int minutes, final boolean mode) {
        int hoursVal = hours;
        int minutesVal = minutes;
        if (minutes >= 60 || minutes > mRecord.getSpentMinutes()) {
            hoursVal += minutes / 60;
            minutesVal = minutes % 60;
        }

        mRecord.addHours(mode ? hoursVal : 0 - hoursVal);
        mRecord.addMinutes(mode ? minutesVal : 0 - minutesVal);

        this.recordSpentTime.setText(String.format(Locale.getDefault(), "%dh %dm", mRecord.getSpentHours(), mRecord.getSpentMinutes()));

        EditTimeAlert.Builder.reset(this.editTimeAlert, mRecord.getSpentHours(), mRecord.getSpentMinutes());
        this.editTimeAlert.hide();
    }
}
