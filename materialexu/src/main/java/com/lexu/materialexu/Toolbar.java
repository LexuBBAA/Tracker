package com.lexu.materialexu;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Toolbar extends android.support.v7.widget.Toolbar implements View.OnClickListener {
    private TextView mTitle;
    private Button mNavigationButton;

    private OnNavigationListener mOnNavigationListener = null;

    public Toolbar(Context context) {
        super(context);
        createView();
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createView();
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createView();
    }

    private void createView() {
        View root = (View) LayoutInflater.from(getContext()).inflate(R.layout.layout_toolbar, null);

        mTitle = (TextView) root.findViewById(R.id.toolbar_title);
        mNavigationButton = (Button) root.findViewById(R.id.toolbar_navigation);
        mNavigationButton.setOnClickListener(this);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setOnNavigationListener(OnNavigationListener onNavigationListener) {
        mOnNavigationListener = onNavigationListener;
    }

    @Override
    public void onClick(View v) {
        mOnNavigationListener.onClick(v);
    }
}
