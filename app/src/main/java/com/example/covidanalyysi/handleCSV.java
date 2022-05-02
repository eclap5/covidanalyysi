package com.example.covidanalyysi;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


// In this class the CSV file which saves the favourites is written and read.
// Implemented using singleton so marked favourites won't get overwritten if changed.
// Healthcare districts ID is stored to arraylist as String and content of arraylist is written to file.
public class handleCSV
{
    private String CSVData;
    private ArrayList<String> dataList = new ArrayList<>();
    covidData JSONData = covidData.getInstance();
    CredentialsDataBase credentialsDataBase = CredentialsDataBase.getInstance();

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
        CSVData = credentialsDataBase.getUsername() + ";";
        for (int i = 0; i < dataList.size(); i++)
        {
            CSVData += dataList.get(i) + ";";
        }
        try {
            OutputStreamWriter ows = new OutputStreamWriter(c.openFileOutput("favourites.csv", Context.MODE_PRIVATE));
            ows.write(CSVData + "\n");
            ows.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // This method reads spesific line of favourites added to favourites.CSV file based on username which is added
    // to the same line with favourites IDs.
    public String readCSV(Context c)
    {
        String line;
        String CSVData = "";
        try {
            InputStream ins = c.openFileInput("favourites.csv");

            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            while ((line = br.readLine()) != null)
            {
                if (line.contains(credentialsDataBase.getUsername()))
                {
                    CSVData = line;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CSVData;
    }


    // When program is started with this method favourites are added to
    // arraylist based on ID in the file.
    public void getFavouritesFromCSV(Context c) {
        String CSVData = readCSV(c);
        String favData[];

        if (CSVData != "")
        {
            favData = CSVData.split(";");
            for (int i = 1; i < favData.length; i++)
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
