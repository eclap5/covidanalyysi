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
public class Home extends Fragment
{
    CovidData jsonData = CovidData.getInstance();
    CredentialsDataBase credentialsDataBase = CredentialsDataBase.getInstance();
    TextView infecs;
    TextView date;
    TextView userText;
    int infNum = 0;
    String value = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        infecs = (TextView) v.findViewById(R.id.infections);
        date = (TextView) v.findViewById(R.id.date);
        userText = (TextView) v.findViewById(R.id.userText);

        // Welcomes user if logged in.
        if (credentialsDataBase.getLogInStatus())
        {
            String text = "Tervetuloa " + credentialsDataBase.getUsername() + "!";
            userText.setText(text);
        }

        // Sets the value for all infections from the start time of data to the current date.
        infNum = jsonData.getHCDArray().get(21).getWeeklyInfections(jsonData.getHCDArray().get(jsonData.getHCDId()).getMaxWeekNum());
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
