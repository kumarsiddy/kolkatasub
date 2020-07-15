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
import com.freakydevs.kolkatalocal.interfaces.PnrStatusInterface;
import com.freakydevs.kolkatalocal.models.PnrDetails;
import com.freakydevs.kolkatalocal.resources.ObjectSerializer;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;

import java.io.IOException;
import java.util.ArrayList;

import static com.freakydevs.kolkatalocal.utils.Constants.TASKS;

/**
 * Created by PURUSHOTAM on 11/6/2017.
 */

public class PnrHistoryAdapter extends RecyclerView.Adapter<PnrHistoryAdapter.MyViewHolder> {

    private ArrayList<PnrDetails> pnrDetailsList;
    private Context context;
    private RecyclerView pnrRecyclerView;
    private MyClickListener myOnClickListener;
    private PnrStatusInterface pnrStatusInterface;

    public PnrHistoryAdapter(Context context, ArrayList<PnrDetails> pnrDetailsList, RecyclerView pnrRecyclerView, Fragment fragment) {
        this.context = context;
        this.pnrDetailsList = pnrDetailsList;
        this.pnrRecyclerView = pnrRecyclerView;
        pnrStatusInterface = (PnrStatusInterface) fragment;
        myOnClickListener = new MyClickListener();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_pnr_history, parent, false);
        return new PnrHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PnrDetails pnrDetails = pnrDetailsList.get(position);
        holder.pnrNo.setText("PNR No: " + pnrDetails.getPnrNo());
        holder.fromToCode.setText(pnrDetails.getFromTo());
        holder.trainNoName.setText(pnrDetails.getTrainNoName());

    }

    @Override
    public int getItemCount() {
        return pnrDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView trainNoName, pnrNo, fromToCode;
        Button deleteButton;

        public MyViewHolder(View view) {
            super(view);
            trainNoName = view.findViewById(R.id.train_no_name);
            pnrNo = view.findViewById(R.id.pnr_no);
            fromToCode = view.findViewById(R.id.from_to_code);
            deleteButton = view.findViewById(R.id.delete);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pnrDetailsList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    try {
                        SharedPreferenceManager.getPnrSharedPrefsEditor(context).putString(TASKS, ObjectSerializer.serialize(pnrDetailsList)).apply();
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

            int itemPosition = pnrRecyclerView.getChildLayoutPosition(v);
            PnrDetails pnrDetails = pnrDetailsList.get(itemPosition);
            pnrStatusInterface.checkPnr(pnrDetails);

        }
    }

}
