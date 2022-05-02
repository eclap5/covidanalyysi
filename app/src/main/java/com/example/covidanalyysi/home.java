package com.example.covidanalyysi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


// This class implements the functionalities for home screen interface fragment_home.
public class home extends Fragment
{
    covidData jsonData = covidData.getInstance();
    TextView infecs;
    TextView date;
    int infNum = 0;
    String value = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        infecs = (TextView) v.findViewById(R.id.infections);
        date = (TextView) v.findViewById(R.id.date);

        // Sets the value for all infections from the start time of data to the current date.
        infNum = jsonData.getHCD_array().get(21).getWeeklyInfections(jsonData.getHCD_array().get(jsonData.getHCDId()).getMaxWeekNum());
        value = String.valueOf(infNum);
        infecs.setText(value);
        date.setText(getDate());

        return v;
    }

    public String getDate()
    {
        String dt;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime date = LocalDateTime.now();
        dt = dtf.format(date);
        return dt;
    }
}
