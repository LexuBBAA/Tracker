package com.lexu.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lexu.tracker.Models.TimeEntry;

import java.util.Timer;

public class TrackActivity extends AppCompatActivity implements View.OnClickListener, OnTimeUpdateCallback {

    private static final String TAG = "TrackActivity";

    private Timer mTimer;
    private TimeEntryTask mTask;

    private TimeEntry mTimeEntry;

    private TextView mTitle;
    private TextView mMinutes;
    private TextView mSeconds;
    private LinearLayout mSaveSection;
    private EditText mTaskTitle;
    private EditText mTaskDescription;
    private ImageView mStartButton;
    private ImageView mStopButton;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_time);

        mStartButton = (ImageView) findViewById(R.id.track_play);
        mStopButton = (ImageView) findViewById(R.id.track_stop);

        mTitle = (TextView) findViewById(R.id.track_title);
        mMinutes = (TextView) findViewById(R.id.track_minutes);
        mSeconds = (TextView) findViewById(R.id.track_seconds);

        mSaveSection = (LinearLayout) findViewById(R.id.track_save_section);
        mTaskTitle = (EditText) mSaveSection.findViewById(R.id.track_task_title);
        mTaskDescription = (EditText) mSaveSection.findViewById(R.id.track_task_description);
        mSaveButton = (Button) mSaveSection.findViewById(R.id.track_task_save);

        mTitle.setText("Track new time");
        mMinutes.setText(String.format(mMinutes.getText().toString(), "00"));
        mSeconds.setText(String.format(mSeconds.getText().toString(), "00"));

        mStartButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSaveButton.setEnabled(false);
                if(mTimeEntry == null) {
                    mSaveButton.setEnabled(true);
                    return;
                }

                mTimeEntry.setTitle(mTaskTitle.getText().toString());
                mTimeEntry.setDescription(mTaskDescription.getText().toString());

                Intent result = getIntent();
                result.putExtra(MainActivity.TRACKER_NEW_ENTRY_KEY, mTimeEntry);
                setResult(MainActivity.TRACKER_RESULT_CODE_TRACK, result);
                finish();
            }
        });

        mTask = new TimeEntryTask(this);
        mTimer = new Timer();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mStartButton.getId()) {
            if(!mTask.isAlive()) {
                mStartButton.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                mTimer.scheduleAtFixedRate(mTask, 0, 1000);
            } else {
                if(mTask.isPaused()) {
                    mStartButton.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    mTask.resume();
                } else {
                    mStartButton.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                    mTask.pause();
                }
            }
            return;
        }

        if(v.getId() == mStopButton.getId()) {
            mTask.cancel();

            mStartButton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
            mStartButton.setEnabled(false);
            mStopButton.setEnabled(false);

            mSaveSection.setVisibility(View.VISIBLE);
            return;
        }
    }

    @Override
    public void onUpdate(final int minutes, final int seconds) {
        Log.d(TAG, "onUpdate: " + minutes + ' ' + seconds);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: " + minutes + ' ' + seconds);
                if(minutes > 0) {
                    mMinutes.setText(minutes >= 10 ? String.valueOf(minutes): "0".concat(String.valueOf(minutes)));
                }

                mSeconds.setText(seconds >= 10 ? String.valueOf(seconds): "0".concat(String.valueOf(seconds)));
            }
        });
    }

    @Override
    public void onStop(TimeEntry newTimeEntry) {
        mTimeEntry = newTimeEntry;
    }
}