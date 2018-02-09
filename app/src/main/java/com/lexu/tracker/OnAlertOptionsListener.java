/*
 * Copyright (c)  Bogdan Andrei Alexandru Birsasteanu 2018.
 * All rights are reserved by Bogdan Andrei Alexandru Birsasteanu.
 * This is an open-source code, and it can be used as reference for various projects.
 */

package com.lexu.tracker;

public interface OnAlertOptionsListener {
    void onCancel();

    void onSave(final int hours, final int minutes, final boolean mode);
}
