package com.freakydevs.kolkatalocal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.models.Passenger;

import java.util.ArrayList;

/**
 * Created by PURUSHOTAM on 11/6/2017.
 */

public class PassengerListAdapter extends ArrayAdapter<Passenger> {

    private ArrayList<Passenger> passengers;
    private Context context;

    public PassengerListAdapter(@NonNull Context context, ArrayList<Passenger> passengers) {
        super(context, R.layout.list_passenger, passengers);
        this.context = context;
        this.passengers = passengers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_passenger, parent, false);
        TextView serialNo = rowView.findViewById(R.id.serial_no);
        TextView bookingStatus = rowView.findViewById(R.id.booking_status);
        TextView currentStatus = rowView.findViewById(R.id.current_status);

        Passenger passenger = passengers.get(position);
        serialNo.setText(position + 1 + "");
        bookingStatus.setText(passenger.getSeat_number());
        currentStatus.setText(passenger.getStatus());
        return rowView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;

    }
}
