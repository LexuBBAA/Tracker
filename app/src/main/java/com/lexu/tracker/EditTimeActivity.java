package com.lexu.tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lexu.tracker.Models.TimeEntry;

public class EditTimeActivity extends AppCompatActivity implements View.OnClickListener {

    private TimeEntry mRecord = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time);

        mRecord = (TimeEntry) getIntent().getSerializableExtra(MainActivity.TRACKER_NEW_ENTRY_KEY);

        EditText recordTitle = (EditText) findViewById(R.id.edit_time_task_title);
        EditText recordDescription = (EditText) findViewById(R.id.edit_time_task_description);
        EditText recordSpentTime = (EditText) findViewById(R.id.edit_time_recorded_time);
        TextView recordDate = (TextView) findViewById(R.id.edit_time_task_date);

        if(mRecord != null) {
            recordTitle.setText(mRecord.getTitle());
            recordDescription.setText(mRecord.getDescription());
            recordDate.setText(mRecord.getDate());
            recordSpentTime.setText(String.format(recordSpentTime.getText().toString(), mRecord.getSpentHours(), mRecord.getSpentMinutes()));

            recordSpentTime.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
