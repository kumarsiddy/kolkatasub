package com.freakydevs.kolkatalocal.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.interfaces.SearchInterface;
import com.freakydevs.kolkatalocal.models.HistoryFromTo;
import com.freakydevs.kolkatalocal.resources.ObjectSerializer;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;

import java.io.IOException;
import java.util.ArrayList;

import static com.freakydevs.kolkatalocal.utils.Constants.TASKS;

/**
 * Created by PURUSHOTAM on 10/9/2016.
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyViewHolder> {

    ArrayList<HistoryFromTo> histories;
    Context context;
    RecyclerView recyclerView;
    private MyClickListener myOnClickListener;
    private SearchInterface searchInterface;

    public SearchHistoryAdapter(Context context, ArrayList<HistoryFromTo> histories, RecyclerView recyclerview, Fragment fragment) {

        this.context = context;
        this.histories = histories;
        this.recyclerView = recyclerview;
        searchInterface = (SearchInterface) fragment;
        myOnClickListener = new MyClickListener();
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyler_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {


        HistoryFromTo historyFromTo = histories.get(position);
        viewHolder.fromCode.setText(historyFromTo.getFromCode());
        viewHolder.fromStation.setText(historyFromTo.getFromStation());
        viewHolder.toCode.setText(historyFromTo.getToCode());
        viewHolder.toStation.setText(historyFromTo.getToStation());


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView fromCode, fromStation, toCode, toStation;
        public Button delete_button;

        public MyViewHolder(View view) {
            super(view);

            fromCode = view.findViewById(R.id.from_code);
            fromStation = view.findViewById(R.id.from_station);
            toCode = view.findViewById(R.id.to_code);
            toStation = view.findViewById(R.id.to_station);
            delete_button = view.findViewById(R.id.delete);

            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    histories.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    try {
                        SharedPreferenceManager.getSearchHistoryEditor(context).putString(TASKS, ObjectSerializer.serialize(histories)).apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            view.setOnClickListener(myOnClickListener);

        }
    }

    class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int itemPosition = recyclerView.getChildLayoutPosition(v);
            HistoryFromTo fromToStation = histories.get(itemPosition);
            searchInterface.historyClicked(fromToStation);

        }
    }

}