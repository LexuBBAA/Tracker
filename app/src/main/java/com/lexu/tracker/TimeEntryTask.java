package com.lexu.tracker;

import android.util.Log;

import com.lexu.tracker.Models.TimeEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class TimeEntryTask extends TimerTask {

    private int minutes = 0;
    private int seconds = 0;
    private ArrayList<PauseRange> mPauseBreaks = new ArrayList<PauseRange>();
    private OnTimeUpdateCallback callback = null;

    private boolean isPaused = false;
    private boolean isAlive = false;

    TimeEntryTask(OnTimeUpdateCallback callback) {
        this.callback = callback;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: " + minutes + "m , " + seconds + 's');
        if(!this.isAlive) {
            this.isAlive = true;
        }

        if(this.isPaused) {
            return;
        }

        this.seconds++;

        if(this.seconds >= 60) {
            this.minutes++;
            this.seconds -= 60;
        }

        this.callback.onUpdate(this.minutes, this.seconds);
    }

    @Override
    public boolean cancel() {
        int hours = 0;
        int resultedMinutes = 0;
        if(this.minutes >= 60) {
            hours = this.minutes / 60;
            resultedMinutes = this.minutes % 60;

            if(seconds >= 60) {
                resultedMinutes++;
            }
        }

        this.callback.onStop(
                new TimeEntry.Builder()
                        .setHours(hours)
                        .setMinutes(resultedMinutes)
                        .build()
        );
        return super.cancel();
    }

    public void pause() {
        this.isPaused = true;
        mPauseBreaks.add(new PauseRange(Calendar.getInstance().getTime()));
    }

    public void resume()  {
        this.isPaused = false;
        mPauseBreaks.get(mPauseBreaks.size() - 1).setStopTime(Calendar.getInstance().getTime());
    }
}
