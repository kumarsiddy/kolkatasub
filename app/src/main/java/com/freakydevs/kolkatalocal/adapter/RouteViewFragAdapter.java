package com.freakydevs.kolkatalocal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.models.Station;

import java.util.ArrayList;

/**
 * Created by PURUSHOTAM on 11/3/2017.
 */

public class RouteViewFragAdapter extends RecyclerView.Adapter<RouteViewFragAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Station> stationArrayList;

    public RouteViewFragAdapter(Context context, ArrayList<Station> stationArrayList) {
        this.context = context;
        this.stationArrayList = stationArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_routeview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Station station = stationArrayList.get(position);
        String stationCode = station.getStationCode();

        if (position % 2 == 0) {
            holder.parenLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.parenLayout.setBackgroundColor(context.getResources().getColor(R.color.whitesnow));
        }

        if (position == 0) {
            holder.trackDown.setVisibility(View.VISIBLE);
            holder.trackUp.setVisibility(View.INVISIBLE);
            holder.trackMedium.setBackgroundResource(R.drawable.top);
        } else if (position == stationArrayList.size() - 1) {
            holder.trackDown.setVisibility(View.INVISIBLE);
            holder.trackMedium.setBackgroundResource(R.drawable.down);
            holder.trackUp.setVisibility(View.VISIBLE);
        } else {
            holder.trackUp.setVisibility(View.VISIBLE);
            holder.trackDown.setVisibility(View.VISIBLE);
            holder.trackMedium.setBackgroundResource(R.drawable.medium);
        }

        holder.stationName.setText(station.getStationName() + " - " + station.getStationCode());
        holder.stationTime.setText(station.getArrivalTime());

    }

    @Override
    public int getItemCount() {
        return stationArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView stationName, stationTime;
        private View trackUp, trackDown, trackMedium;
        private RelativeLayout parenLayout;

        public MyViewHolder(View view) {
            super(view);
            stationName = view.findViewById(R.id.station_name);
            stationTime = view.findViewById(R.id.station_time);
            trackUp = view.findViewById(R.id.track_up);
            trackDown = view.findViewById(R.id.track_down);
            trackMedium = view.findViewById(R.id.track_medium);
            parenLayout = view.findViewById(R.id.parent_layout);
        }
    }
}
