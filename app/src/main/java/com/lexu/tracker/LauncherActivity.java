package com.lexu.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lexu.tracker.Managers.DatabaseProvider;
import com.lexu.tracker.Managers.OnDatabaseCallback;
import com.lexu.tracker.Models.TimeEntry;

import java.util.ArrayList;

public class LauncherActivity extends AppCompatActivity implements OnDatabaseCallback<TimeEntry> {

    private static final String TAG = "LauncherActivity";

    private ArrayList<TimeEntry> mData = new ArrayList<TimeEntry>();
    private boolean isLoading = true;
    private int runDuration = 0;
    private int commaCount = 0;

    private TextView loadingMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        DatabaseProvider.Builder.getInstance()
                .with(LauncherActivity.this)
                .callback(this)
                .build()
                .execute();

        this.loadingMessage = (TextView) findViewById(R.id.launcher_loading_text);
        this.loadingMessage.setVisibility(View.VISIBLE);

        final String message = getResources().getString(R.string.loading_phase_0_text);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: " + isLoading + ' ' + runDuration);
                if(!isLoading && runDuration >= 5) {
                    loadingMessage.setText(getResources().getString(R.string.loaded_text));
                    proceed();
                } else {
                    String displayMsg = message;
                    Log.d(TAG, "run: " + displayMsg);
                    commaCount++;
                    switch (commaCount) {
                        case 1:
                            displayMsg = displayMsg.concat(".");
                            break;

                        case 2:
                            displayMsg = displayMsg.concat("..");
                            break;

                        case 3:
                            displayMsg = displayMsg.concat("...");
                            break;
                    }

                    loadingMessage.setText(displayMsg);

                    if (commaCount == 3) {
                        commaCount = 0;
                    }

                    runDuration += 1;
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    @Override
    public void onDataLoaded(final ArrayList<TimeEntry> data) {
        isLoading = false;
        mData = data;
    }

    private void proceed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent navigate = new Intent(LauncherActivity.this, MainActivity.class);
                navigate.putExtra(MainActivity.TRACKER_DATA_KEY, mData);
                LauncherActivity.this.startActivity(navigate);
                LauncherActivity.this.finish();
            }
        });
    }
}
