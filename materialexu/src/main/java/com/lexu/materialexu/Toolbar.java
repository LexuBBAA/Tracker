/*
 * Copyright (c)  Bogdan Andrei Alexandru Birsasteanu 2018.
 * All rights are reserved by Bogdan Andrei Alexandru Birsasteanu.
 * This is an open-source code, and it can be used as reference for various projects.
 */

/*
 * Copyright (c)  Bogdan Andrei Alexandru Birsasteanu 2018.
 * All rights are reserved by Bogdan Andrei Alexandru Birsasteanu.
 * This is an open-source code, and it can be used as reference for various projects.
 */

/*
 * Copyright (c)  Bogdan Andrei Alexandru Birsasteanu 2018.
 * All rights are reserved by Bogdan Andrei Alexandru Birsasteanu.
 * This is an open-source code, and it can be used as reference for various projects.
 */

/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

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
        View root = LayoutInflater.from(getContext()).inflate(R.layout.layout_toolbar, null);

        mTitle = root.findViewById(R.id.toolbar_title);
        mNavigationButton = root.findViewById(R.id.toolbar_navigation);
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
