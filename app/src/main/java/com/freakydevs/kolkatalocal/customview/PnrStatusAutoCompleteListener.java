package com.freakydevs.kolkatalocal.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.freakydevs.kolkatalocal.R;

/**
 * Created by PURUSHOTAM on 03-09-2015.
 */
public class PnrStatusAutoCompleteListener implements TextWatcher {
    private Context context;
    private CustomAutoCompleteView customAutoCompleteView;


    public PnrStatusAutoCompleteListener(Context context, final CustomAutoCompleteView customAutoCompleteView) {
        this.context = context;
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
                    customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pnr, 0, 0, 0);
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
        customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pnr, 0, 0, 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

        if (s.length() > 0) {
            customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pnr, 0, R.drawable.clear, 0);
        } else {
            customAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pnr, 0, 0, 0);
        }
        if (s.length() == 10) {
            hideKeyboard();
        }

    }

    private void hideKeyboard() {
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(customAutoCompleteView.getApplicationWindowToken(), 0);
    }

}
