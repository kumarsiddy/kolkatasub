package com.freakydevs.kolkatalocal.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.adapter.SearchHistoryAdapter;
import com.freakydevs.kolkatalocal.customview.CustomAutoCompleteTextChangedListener;
import com.freakydevs.kolkatalocal.customview.CustomAutoCompleteView;
import com.freakydevs.kolkatalocal.customview.MySnackBar;
import com.freakydevs.kolkatalocal.interfaces.MainActivityInterface;
import com.freakydevs.kolkatalocal.interfaces.SearchInterface;
import com.freakydevs.kolkatalocal.models.HistoryFromTo;
import com.freakydevs.kolkatalocal.models.Station;
import com.freakydevs.kolkatalocal.resources.MyDataBaseHandler;
import com.freakydevs.kolkatalocal.resources.ObjectSerializer;
import com.freakydevs.kolkatalocal.resources.SearchTrainDetails;
import com.freakydevs.kolkatalocal.utils.Constants;
import com.freakydevs.kolkatalocal.utils.Logger;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.freakydevs.kolkatalocal.utils.Constants.TASKS;

/**
 * Created by PURUSHOTAM on 10/30/2017.
 */

public class SearchFragment extends Fragment implements SearchInterface, View.OnClickListener {

    private CustomAutoCompleteView from, to;
    private Button findButton;
    private Context context;
    private Button exchange;
    private ArrayAdapter<String> customAdapter;
    private String[] items = new String[]{};
    private ArrayList<HistoryFromTo> historyFromToList;
    private SearchHistoryAdapter historyAdapter;
    private RecyclerView historyRecycler;
    private String fromText, toText;
    private MyDataBaseHandler myDataBaseHandler;
    private MainActivityInterface mainActivityInterface;
    private View view;
    private ConstraintLayout parentLayout;
    private EditText editDate;
    private Calendar c;
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateTime();
        }

    };
    private CheckBox checkBox;

    public SearchFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        myDataBaseHandler = new MyDataBaseHandler(context);
        mainActivityInterface = (MainActivityInterface) context;
        initHistoryListFromSharedPref();
    }

    private void initHistoryListFromSharedPref() {
        try {
            historyFromToList = (ArrayList<HistoryFromTo>) ObjectSerializer.deserialize(SharedPreferenceManager.getSearchHistorySharedPreference(context).getString(TASKS, ObjectSerializer.serialize(new ArrayList<HistoryFromTo>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (historyFromToList == null)
            historyFromToList = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        parentLayout = view.findViewById(R.id.parent_layout);
        editDate = view.findViewById(R.id.edit_date);

        from = view.findViewById(R.id.edit_from);
        CustomAutoCompleteTextChangedListener listenerFrom = new CustomAutoCompleteTextChangedListener(context, from, true, this);
        from.addTextChangedListener(listenerFrom);

        to = view.findViewById(R.id.edit_to);
        CustomAutoCompleteTextChangedListener listenerTo = new CustomAutoCompleteTextChangedListener(context, to, false, this);
        to.addTextChangedListener(listenerTo);

        exchange = view.findViewById(R.id.exchange);
        exchange.setOnClickListener(this);

        findButton = view.findViewById(R.id.find);
        findButton.setOnClickListener(this);

        checkBox = view.findViewById(R.id.checkbox);

        String data[] = SharedPreferenceManager.getAppFromTo(context);
        if (data != null && data[0].trim().length() > 2) {
            from.setText(String.format("%s - %s", data[1], data[0]));
            to.setText(String.format("%s - %s", data[3], data[2]));
        }
        historyRecycler = view.findViewById(R.id.history_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        historyRecycler.setLayoutManager(mLayoutManager);
        historyRecycler.setItemAnimator(new DefaultItemAnimator());
        historyAdapter = new SearchHistoryAdapter(context, historyFromToList, historyRecycler, this);
        historyRecycler.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();

        c = Calendar.getInstance();

        editDate.setOnClickListener(this);
        updateDateTime();

        from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                to.requestFocus();
            }
        });
        to.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                hideKeyboard();
            }
        });

        return view;
    }

    private void hideKeyboard() {
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(to.getApplicationWindowToken(), 0);
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(to, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void historyClicked(HistoryFromTo fromTo) {
        from.setText(String.format("%s - %s", fromTo.getFromStation(), fromTo.getFromCode()));
        to.setText(String.format("%s - %s", fromTo.getToStation(), fromTo.getToCode()));
        findTrains();
    }

    public String[] getItemsFromDb(String searchTerm) {
        // add items on the array dynamically
        List<Station> products = myDataBaseHandler.read(searchTerm);
        int rowCount = products.size();

        String[] item = new String[rowCount];
        int x = 0;
        for (Station record : products) {

            item[x] = record.getStationName() + " - " + record.getStationCode();
            x++;
        }
        return item;
    }

    @Override
    public void fillData(String userInput) {
        items = getItemsFromDb(userInput);
        customAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, items);
        customAdapter.notifyDataSetChanged();
        from.setAdapter(customAdapter);
        to.setAdapter(customAdapter);
    }

    public void exchange() {
        InputMethodManager methodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromInputMethod(to.getWindowToken(), 0);
        String e = from.getText().toString();
        String ex = to.getText().toString();
        from.setText(ex);
        to.setText(e);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.exchange:
                exchange();
                break;
            case R.id.find:
                findTrains();
                break;
            case R.id.edit_date:
                new DatePickerDialog(context, date, c
                        .get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }

    }

    public void findTrains() {
        int from_id = -1;
        int to_id = -2;
        fromText = from.getText().toString();
        toText = to.getText().toString();

        String fromCode = "", fromStation = "", toCode = "", toStation = "";

        String[] fromCodeStation = fromText.split("-");
        String[] toCodeStation = toText.split("-");

        if (fromText != null && fromText.contains("-")) {
            fromCode = fromCodeStation[1].trim();
            fromStation = fromCodeStation[0].trim();
        }
        if (toText != null && toText.contains("-")) {
            toCode = toCodeStation[1].trim();
            toStation = toCodeStation[0].trim();
        }

        SQLiteDatabase SQ = null;
        try {
            String selectQuery = "SELECT * FROM station_table WHERE stationCode='" + fromCode + "' LIMIT 1;";
            String selectQuery1 = "SELECT * FROM station_table WHERE stationCode='" + toCode + "' LIMIT 1;";
            SQ = myDataBaseHandler.getReadableDatabase();
            Cursor cursor = SQ.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                from_id = cursor.getInt(cursor.getColumnIndex("_id"));
                cursor.close();
            }
            Cursor cursor1 = SQ.rawQuery(selectQuery1, null);
            if (cursor1 != null && cursor1.moveToFirst()) {
                to_id = cursor1.getInt(cursor1.getColumnIndex("_id"));
                cursor1.close();
            }
        } catch (SQLException e) {
            mainActivityInterface.notifyDatabaseDownload();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (SQ != null) {
                SQ.close();
            }
        }

        Logger.d("data" + from_id + "  " + to_id);

        if (from_id == -1 && to_id == -2) {
            MySnackBar.show(context.getApplicationContext(), parentLayout, "Source and Destination Incorrect!!");
        } else if (from_id == -1) {
            MySnackBar.show(context.getApplicationContext(), parentLayout, "Source Incorrect!!");
        } else if (to_id == -2) {
            MySnackBar.show(context.getApplicationContext(), parentLayout, "Destination Incorrect!!");
        } else if (fromText.equals(toText)) {
            MySnackBar.show(context.getApplicationContext(), parentLayout, "Source and Destination Same!!");
        } else {
            HistoryFromTo historyFromTo = new HistoryFromTo(fromCode, fromStation, toCode, toStation);
            addFromTo(historyFromTo);
            historyAdapter.notifyDataSetChanged();
            SharedPreferenceManager.saveAppFromTo(context, fromCodeStation[1].trim(), fromCodeStation[0].trim(), toCodeStation[1].trim(), toCodeStation[0].trim());
            new SearchTrainDetails(context, this, editDate.getText().toString(), historyFromTo, from_id, to_id, checkBox.isChecked()).execute();
        }


    }

    public void addFromTo(HistoryFromTo t) {

        ifDataExist(t);
        historyFromToList.add(0, t);
        historyAdapter.notifyDataSetChanged();

        if (historyFromToList.size() > Constants.SIZE_OF_HISTORY_LIST)
            historyFromToList.remove(historyFromToList.size() - 1);

        try {
            SharedPreferenceManager.getSearchHistoryEditor(context).putString(TASKS, ObjectSerializer.serialize(historyFromToList)).commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ifDataExist(HistoryFromTo f) {

        for (HistoryFromTo fromToStation : historyFromToList) {

            if (f.getFromCode().equals(fromToStation.getFromCode()) && f.getToCode().equals(fromToStation.getToCode())) {
                historyFromToList.remove(fromToStation);
                break;
            }

        }
    }

    private void updateDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        editDate.setText(formattedDate);
    }

    @Override
    public void disableFindButton() {
        findButton.setClickable(false);
    }

    @Override
    public void enableFindButton() {
        findButton.setClickable(true);
    }


}
