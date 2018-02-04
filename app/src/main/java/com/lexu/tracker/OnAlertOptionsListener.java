package com.lexu.tracker;

public interface OnAlertOptionsListener {
    void onCancel();
    void onSave(final int hours, final int minutes, final boolean mode);
}
