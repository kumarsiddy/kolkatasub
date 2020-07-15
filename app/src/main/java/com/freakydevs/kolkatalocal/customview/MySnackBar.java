package com.freakydevs.kolkatalocal.customview;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.freakydevs.kolkatalocal.R;

/**
 * Created by PURUSHOTAM on 11/1/2017.
 */

//TODO refactor this
public class MySnackBar {

    public static void show(Context context, ConstraintLayout layout, String message) {

        Snackbar bar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG)
                .setAction(context.getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle user action

                    }
                });

        View view = bar.getView();
        view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(context.getResources().getColor(R.color.whitesnow));
        bar.setActionTextColor(context.getResources().getColor(R.color.colorAccentDark));
        bar.show();

    }


    public static void showFromActivity(Activity activity, String message) {
        View activityView = activity.findViewById(android.R.id.content);
        Snackbar bar = Snackbar.make(activityView, message, Snackbar.LENGTH_LONG)
                .setAction(activity.getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

        View view = bar.getView();
        view.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(activity.getResources().getColor(R.color.whitesnow));
        bar.setActionTextColor(activity.getResources().getColor(R.color.colorAccentDark));
        bar.show();
    }

}
