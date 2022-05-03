package com.example.covidanalyysi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


// This class implements the functionalities for interface of fragment_hcd_selection.
// In this interface user can choose healthcare district and time to see amount of infections
// on that area and time.
public class HCDSelection extends Fragment
{
    Spinner spinner;
    Spinner spinner2;
    TextView textBox;
    TextView textBox2;
    Button button;
    Button button2;
    Button button3;

    CovidData JSONData = CovidData.getInstance();
    HandleCSV handler = HandleCSV.getInstance();
    CredentialsDataBase credentialsDataBase = CredentialsDataBase.getInstance();
    SortWeeks time = new SortWeeks();

    private int weekId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_hcd_selection, container, false);

        spinner = (Spinner) v.findViewById(R.id.spinner);
        spinner2 = (Spinner) v.findViewById(R.id.spinner2);
        textBox = (TextView) v.findViewById(R.id.textBox);
        textBox2 = (TextView) v.findViewById(R.id.textBox2);
        button = (Button) v.findViewById(R.id.button);
        button2 = (Button) v.findViewById(R.id.button2);
        button3 = (Button) v.findViewById(R.id.button3);


        button3.setVisibility(v.GONE);

        ArrayAdapter<HealthCareDistrict> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, JSONData.getHCDArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(JSONData.getHCDArray().get(JSONData.getHCDId())), false);
        textBox.setText(setData(JSONData.getHCDId()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                JSONData.setHCDId(i);
                textBox.setText(setData(i));
                if (JSONData.getFavouritesArray().contains(JSONData.getHCDArray().get(i)))
                {
                    button3.setVisibility(v.VISIBLE);
                }
                else
                {
                    button3.setVisibility(v.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, time.getWeekArray(JSONData.getDWLabels()));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner2.setAdapter(adapter1);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                weekId = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // With the "Hae" button user will get to see infections on selected timeframe but also
        // the change to previous week is shown.
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String data;
                String change;
                int prev;
                int diff;
                int current;
                char prefix = ' ';

                if (weekId < (157-52) + time.getWeekNum())
                {
                    current = JSONData.getHCDArray().get(JSONData.getHCDId()).getWeeklyInfections(weekId);

                    if (current == JSONData.getHCDArray().get(JSONData.getHCDId()).getWeeklyInfections(1))
                    {
                        diff = 0;
                    }
                    else
                    {
                        prev = JSONData.getHCDArray().get(JSONData.getHCDId()).getWeeklyInfections(weekId - 1);
                        if (prev > current)
                        {
                            diff = prev - current;
                            prefix = '-';
                        }
                        else if (current > prev)
                        {
                            diff = current - prev;
                            prefix = '+';
                        }
                        else
                        {
                            diff = 0;
                            prefix = ' ';
                        }
                    }

                    change = String.valueOf(prefix) + String.valueOf(diff);
                    data = "Tartunnat valittuna ajan jaksona: " + current + "\n" + "Muutos edelliseen viikkoon: " + change;
                    textBox2.setText(data);
                }
                else
                {
                    Toast.makeText(getContext(), "Ei dataa valitulle ajanjaksolle", Toast.LENGTH_SHORT).show();
                    textBox2.setText("");
                }

            }

        });


        // Adds current ID to the favourite arraylist in handleCSV class
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (credentialsDataBase.getLogInStatus())
                {
                    JSONData.addFavourites(JSONData.getHCDArray().get(JSONData.getHCDId()));
                    button3.setVisibility(v.VISIBLE);
                    Toast.makeText(getContext(), "Lisätty suosikkeihin", Toast.LENGTH_SHORT).show();
                    handler.setCSVData(String.valueOf(JSONData.getHCDId()));
                }
                else
                {
                    Toast.makeText(getContext(), "Kirjaudu sisään lisätäksesi suosikkeja", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Sets the visibility of "Poista suosikeista" button according if current object is in favourites.
        if (JSONData.getFavouritesArray().contains(JSONData.getHCDArray().get(JSONData.getHCDId())))
        {
            button3.setVisibility(v.VISIBLE);
        }
        else
        {
            button3.setVisibility(v.GONE);
        }


        // Deletes current object from favourites.
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getContext(), "Poistettu suosikeista", Toast.LENGTH_SHORT).show();
                JSONData.getFavouritesArray().remove(JSONData.getFavIndex());
                button3.setVisibility(v.GONE);
                handler.removeFromCSV(JSONData.getHCDId());
            }
        });
        return v;
    }


    // Sets data from current healthcare district in spinner to the text field.
    public String setData(int id)
    {
        String data = "";
        data = JSONData.getHCDArray().get(id).getDistrictName() + "\n" + "Tartunnat yhteensä: " + JSONData.getHCDArray().get(id).getWeeklyInfections(JSONData.getHCDArray().get(JSONData.getHCDId()).getMaxWeekNum());
        return data;
    }
}
