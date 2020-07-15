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
import com.freakydevs.kolkatalocal.interfaces.TrainRouteFragInterface;
import com.freakydevs.kolkatalocal.models.TrainNoName;
import com.freakydevs.kolkatalocal.resources.ObjectSerializer;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;

import java.io.IOException;
import java.util.ArrayList;

import static com.freakydevs.kolkatalocal.utils.Constants.TRAINHISTORY;

/**
 * Created by PURUSHOTAM on 11/5/2017.
 */

public class TrainRouteHistoryAdapter extends RecyclerView.Adapter<TrainRouteHistoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<TrainNoName> trainNoNames;
    private RecyclerView recyclerView;
    private TrainRouteFragInterface anInterface;
    private MyClickListener myClickListener;

    public TrainRouteHistoryAdapter(Context context, ArrayList<TrainNoName> trainNoNames, RecyclerView recyclerView, Fragment fragment) {
        this.context = context;
        this.trainNoNames = trainNoNames;
        this.recyclerView = recyclerView;
        anInterface = (TrainRouteFragInterface) fragment;
        myClickListener = new MyClickListener();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_trainroute_history, parent, false);
        return new TrainRouteHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TrainNoName train = trainNoNames.get(position);
        holder.textTrainNo.setText(train.getTrainNo());
        holder.textTrainName.setText(train.getTrainName());

    }

    @Override
    public int getItemCount() {
        return trainNoNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textTrainNo, textTrainName;
        private Button deleteButton;

        public MyViewHolder(View view) {
            super(view);
            textTrainNo = view.findViewById(R.id.train_no);
            textTrainName = view.findViewById(R.id.train_name);
            deleteButton = view.findViewById(R.id.delete);

            view.setOnClickListener(myClickListener);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trainNoNames.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    try {
                        SharedPreferenceManager.getTrainRouteSharedPrefsEditor(context).putString(TRAINHISTORY, ObjectSerializer.serialize(trainNoNames)).apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int itemPosition = recyclerView.getChildLayoutPosition(v);
            TrainNoName trainNoName = trainNoNames.get(itemPosition);
            anInterface.searchRoute(trainNoName);

        }
    }

}
