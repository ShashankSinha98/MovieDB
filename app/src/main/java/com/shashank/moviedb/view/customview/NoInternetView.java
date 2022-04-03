package com.shashank.moviedb.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.shashank.moviedb.R;
import com.shashank.moviedb.data.Status;

public class NoInternetView extends ConstraintLayout {

    private static final String TAG = "NoInternetView";

    private ConstraintLayout rootNoInternet;
    private AppCompatButton btnretry;


    public NoInternetView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.layout_no_internet, this);

        rootNoInternet = findViewById(R.id.cl_root);
        btnretry = findViewById(R.id.btn_retry);
    }

    public void update(Status status, OnClickListener listener) {
        Log.d(TAG, "xlr8: Status: "+status);
        switch (status) {
            case ERROR:
                rootNoInternet.setVisibility(VISIBLE);
                btnretry.setOnClickListener(listener);
                break;

            case SUCCESS:
                rootNoInternet.setVisibility(GONE);
                break;
        }
    }
}
