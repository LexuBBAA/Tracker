package com.lexu.tracker.Managers;

import java.util.ArrayList;

public interface OnDatabaseCallback<T> {
    void onDataLoaded(ArrayList<T> data);
}
