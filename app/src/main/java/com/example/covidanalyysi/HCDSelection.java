package com.example.covidanalyysi;

import android.content.Context;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


public class HCDSelection extends Fragment
{
    Spinner spinner;
    Spinner spinner2;
    TextView textBox;
    TextView textBox2;
    Button button;
    Button button2;
    Button button3;

    covidData JSONData = covidData.getInstance();
    handleCSV handler = handleCSV.getInstance();
    sortWeeks time = new sortWeeks();

    private int weekId1;

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

        ArrayAdapter<healthCareDistrict> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, JSONData.getHCD_array());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(JSONData.getHCD_array().get(JSONData.getHCDId())), false);
        textBox.setText(setData(JSONData.getHCDId()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                JSONData.setHCDId(i);
                textBox.setText(setData(i));
                if (JSONData.getFav_Array().contains(JSONData.getHCD_array().get(i)))
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

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, time.getWeekArray(JSONData.getDW_labels()));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner2.setAdapter(adapter1);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                weekId1 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

                if (weekId1 <= (158-52) + time.getWeekNum())
                {
                    current = JSONData.getHCD_array().get(JSONData.getHCDId()).getWeeklyInfections(weekId1);

                    if (current == JSONData.getHCD_array().get(JSONData.getHCDId()).getWeeklyInfections(0))
                    {
                        diff = 0;
                    }
                    else
                    {
                        prev = JSONData.getHCD_array().get(JSONData.getHCDId()).getWeeklyInfections(weekId1 - 1);
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

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                JSONData.addFavourites(JSONData.getHCD_array().get(JSONData.getHCDId()));
                button3.setVisibility(v.VISIBLE);
                Toast.makeText(getContext(), "Lisätty suosikkeihin", Toast.LENGTH_SHORT).show();
                handler.setCSVData(String.valueOf(JSONData.getHCDId()));
            }
        });

        if (JSONData.getFav_Array().contains(JSONData.getHCD_array().get(JSONData.getHCDId())))
        {
            button3.setVisibility(v.VISIBLE);
        }
        else
        {
            button3.setVisibility(v.GONE);
        }

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getContext(), "Poistettu suosikeista", Toast.LENGTH_SHORT).show();
                JSONData.getFav_Array().remove(JSONData.getFavIndex());
                button3.setVisibility(v.GONE);
                handler.removeFromCSV(JSONData.getHCDId());
            }
        });
        return v;
    }

    public String setData(int id)
    {
        String data = "";
        data = JSONData.getHCD_array().get(id).getDistrictName() + "\n" + "Tartunnat yhteensä: " + JSONData.getHCD_array().get(id).getWeeklyInfections(JSONData.getHCD_array().get(JSONData.getHCDId()).getMaxWeekNum());
        return data;
    }
}
