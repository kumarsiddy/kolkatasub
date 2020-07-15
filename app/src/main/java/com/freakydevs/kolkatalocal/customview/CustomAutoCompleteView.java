package com.freakydevs.kolkatalocal.customview;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

/**
 * Created by PURUSHOTAM on 03-09-2015.
 */
public class CustomAutoCompleteView extends AppCompatAutoCompleteTextView {
    private Context context;

    public CustomAutoCompleteView(Context context) {
        super(context);
    }

    public CustomAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAutoCompleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        String filterText = "";
        super.performFiltering(filterText, keyCode);
    }

    @Override
    protected void replaceText(final CharSequence text) {
        super.replaceText(text);
    }
}
