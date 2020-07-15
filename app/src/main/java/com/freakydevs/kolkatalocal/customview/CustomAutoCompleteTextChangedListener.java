package com.freakydevs.kolkatalocal.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.interfaces.SearchInterface;

/**
 * Created by PURUSHOTAM on 03-09-2015.
 */
@SuppressLint("clickableviewaccessibility")
public class CustomAutoCompleteTextChangedListener implements TextWatcher {
    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    private Context context;
    private CustomAutoCompleteView customAutoCompleteView;
    private SearchInterface searchInterface;
    private boolean isFrom = false;


    public CustomAutoCompleteTextChangedListener(Context context, final CustomAutoCompleteView customAutoCompleteView, final boolean isFrom, Fragment fragment) {
        this.context = context;
        this.customAutoCompleteView = customAutoCompleteView;
        this.isFrom = isFrom;
        searchInterface = (SearchInterface) fragment;
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
                    if (isFrom) {
                        customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.from_icon, 0, 0, 0);
                    } else {
                        customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.to_icon, 0, 0, 0);
                    }

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
        searchInterface.fillData(userInput.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0 && isFrom) {
            customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.from_icon, 0, R.drawable.clear, 0);
        } else if (s.length() > 0 && !isFrom) {
            customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.to_icon, 0, R.drawable.clear, 0);
        } else if (s.length() == 0 && isFrom) {
            customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.from_icon, 0, 0, 0);
        } else if (s.length() == 0 && !isFrom) {
            customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.to_icon, 0, 0, 0);
        }
    }

}
