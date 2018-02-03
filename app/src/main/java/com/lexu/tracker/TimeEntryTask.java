package com.lexu.tracker;

import com.lexu.tracker.Models.TimeEntry;

import java.util.Calendar;
import java.util.TimerTask;

public class TimeEntryTask extends TimerTask {

    private int minutes = 0;
    private int seconds = 0;
    private OnTimeUpdateCallback callback = null;

    public TimeEntryTask(OnTimeUpdateCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
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

        this.callback.onStop(new TimeEntry(null, null, Calendar.getInstance(), hours, resultedMinutes));
        return super.cancel();
    }
}
