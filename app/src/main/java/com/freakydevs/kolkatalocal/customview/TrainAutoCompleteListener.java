package com.freakydevs.kolkatalocal.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.interfaces.TrainRouteFragInterface;

/**
 * Created by PURUSHOTAM on 03-09-2015.
 */
public class TrainAutoCompleteListener implements TextWatcher {
    private Context context;
    private CustomAutoCompleteView customAutoCompleteView;
    private TrainRouteFragInterface fragInterface;


    public TrainAutoCompleteListener(Context context, final CustomAutoCompleteView customAutoCompleteView, Fragment fragment) {
        this.context = context;
        fragInterface = (TrainRouteFragInterface) fragment;
        this.customAutoCompleteView = customAutoCompleteView;
        final Drawable x = context.getResources().getDrawable(R.drawable.clear);//your x image, this one from standard android images looks pretty good actually
        x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
        customAutoCompleteView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (customAutoCompleteView.getCompoundDrawables()[2] == null) {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > customAutoCompleteView.getWidth() - customAutoCompleteView.getPaddingRight() - x.getIntrinsicWidth()) {
                    customAutoCompleteView.setText("");
                    customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.train, 0, 0, 0);
                }
                return false;
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
        customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.from_icon, 0, 0, 0);
        fragInterface.fillData(userInput.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.train, 0, R.drawable.clear, 0);
        } else {
            customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.train, 0, 0, 0);
        }
    }

}
