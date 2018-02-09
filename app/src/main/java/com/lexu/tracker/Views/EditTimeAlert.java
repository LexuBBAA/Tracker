/*
 * Copyright (c)  Bogdan Andrei Alexandru Birsasteanu 2018.
 * All rights are reserved by Bogdan Andrei Alexandru Birsasteanu.
 * This is an open-source code, and it can be used as reference for various projects.
 */

package com.lexu.tracker.Views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.lexu.tracker.OnAlertOptionsListener;
import com.lexu.tracker.R;

public class EditTimeAlert extends Dialog implements View.OnClickListener {
    private Context mContext;

    private int mRecordedHours;
    private int mRecordedMinutes;

    private EditText mHours;
    private EditText mMinutes;
    private RadioButton mAddTime;
    private RadioButton mRemoveTime;

    private OnAlertOptionsListener mCallback;

    protected EditTimeAlert(Context context) {
        super(context);
        mContext = context;

        createView();
    }

    protected EditTimeAlert(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;

        createView();
    }

    protected EditTimeAlert(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;

        createView();
    }

    private void createView() {
        View root = LayoutInflater.from(mContext).inflate(R.layout.layout_edit_time_popup, null);
        this.setContentView(root);
        mHours = root.findViewById(R.id.popup_hours);
        mMinutes = root.findViewById(R.id.popup_minutes);
        mAddTime = root.findViewById(R.id.popup_option_add);
        mRemoveTime = root.findViewById(R.id.popup_option_remove);

        Button cancel = root.findViewById(R.id.popup_cancel);
        Button save = root.findViewById(R.id.popup_save);

        cancel.setOnClickListener(this);
        save.setOnClickListener(this);

        View.OnClickListener radioListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == mAddTime.getId()) {
                    mAddTime.setEnabled(false);
                    mRemoveTime.setEnabled(true);
                    mRemoveTime.setChecked(false);
                } else if (v.getId() == mRemoveTime.getId()) {
                    mRemoveTime.setEnabled(false);
                    mAddTime.setEnabled(true);
                    mAddTime.setChecked(false);
                }
            }
        };

        mAddTime.setOnClickListener(radioListener);
        mRemoveTime.setOnClickListener(radioListener);
//        this.setView(root);
    }

    public void setOnOptionListener(OnAlertOptionsListener onOptionListener) {
        mCallback = onOptionListener;
    }

    @Override
    public void onClick(View v) {
        v.setEnabled(false);

        if (v.getId() == R.id.popup_cancel) {
            mCallback.onCancel();

            v.setEnabled(true);
            return;
        }

        if (v.getId() == R.id.popup_save) {
            int addedHours = mHours.getText().toString().length() != 0 ? Integer.valueOf(mHours.getText().toString()) : 0;
            int addedMinutes = mMinutes.getText().toString().length() != 0 ? Integer.valueOf(mMinutes.getText().toString()) : 0;
            if (mRemoveTime.isChecked()) {
                boolean valid = true;
                if (addedHours > mRecordedHours) {
                    mHours.setError("Invalid value");
                    valid = false;
                } else if (addedMinutes > mRecordedMinutes || addedMinutes > 59) {
                    if (addedHours != 0) {
                        mRecordedHours -= addedHours;
                    }

                    if (mRecordedHours <= 0) {
                        mMinutes.setError("Invalid value");
                        valid = false;
                    } else {
                        int totalMinutes = 60 * mRecordedHours;
                        if (addedMinutes > totalMinutes) {
                            mMinutes.setError("Invalid value");
                            valid = false;
                        }
                    }
                }

                if (!valid) {
                    v.setEnabled(true);
                    return;
                }
            }

            mCallback.onSave(
                    mHours.getText().toString().length() != 0 ? Integer.valueOf(mHours.getText().toString()) : 0,
                    mMinutes.getText().toString().length() != 0 ? Integer.valueOf(mMinutes.getText().toString()) : 0,
                    mAddTime.isChecked()
            );

            v.setEnabled(true);
            return;
        }

        v.setEnabled(true);
    }

    protected void setRecordedHours(int hours) {
        mRecordedHours = hours;
    }

    protected void setRecordedMinutes(int minutes) {
        mRecordedMinutes = minutes;
    }

    protected void resetFields() {
        mMinutes.setText("");
        mHours.setText("");
        mAddTime.setChecked(true);
        mAddTime.setEnabled(false);
        mRemoveTime.setChecked(false);
        mRemoveTime.setEnabled(true);
    }

    public static class Builder {
        private Context mContext;
        private int mHours;
        private int mMinutes;

        public Builder() {
        }

        public static void reset(EditTimeAlert alert, int hours, int minutes) {
            alert.setRecordedHours(hours);
            alert.setRecordedMinutes(minutes);
            alert.resetFields();
        }

        public Builder with(Context context) {
            mContext = context;
            return this;
        }

        public Builder setHours(int hours) {
            mHours = hours;
            return this;
        }

        public Builder setMinutes(int minutes) {
            mMinutes = minutes;
            return this;
        }

        public EditTimeAlert build() {
            EditTimeAlert alert = new EditTimeAlert(mContext);
            alert.setRecordedHours(mHours);
            alert.setRecordedMinutes(mMinutes);
            return alert;
        }
    }
}