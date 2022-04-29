package com.example.covidanalyysi;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class handleCSV
{
    private String CSVData = "";
    private ArrayList<String> dataList = new ArrayList<>();
    covidData JSONData = covidData.getInstance();

    private handleCSV()
    {

    }

    private static handleCSV handler = new handleCSV();

    public static handleCSV getInstance()
    {
        return handler;
    }

    public void setCSVData(String data)
    {
        dataList.add(data);
    }

    public void removeFromCSV(int id)
    {
        if (dataList.contains(String.valueOf(id)))
        {
            dataList.remove(String.valueOf(id));
        }
    }

    public void writeCSV(Context c)
    {
        for (int i = 0; i < dataList.size(); i++)
        {
            CSVData += dataList.get(i) + ";";
        }
        try {
            OutputStreamWriter ows = new OutputStreamWriter(c.openFileOutput("favourites.csv", Context.MODE_PRIVATE));
            ows.write(CSVData);
            ows.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readCSV(Context c)
    {
        String s;
        String var = "";
        try {
            InputStream ins = c.openFileInput("favourites.csv");

            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            while ((s = br.readLine()) != null)
            {
                var = s;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return var;
    }

    public void getFavouritesFromCSV(Context c) {
        String var = readCSV(c);
        String favData[];

        if (var != "")
        {
            favData = var.split(";");
            for (int i = 0; i < favData.length; i++)
            {
                for (int j = 0; j < JSONData.getHCD_array().size(); j++)
                {
                    if (JSONData.getHCD_array().get(j).getId() == Integer.parseInt(favData[i]))
                    {
                        JSONData.addFavourites(JSONData.getHCD_array().get(j));
                    }
                }
            }
        }
    }
}
