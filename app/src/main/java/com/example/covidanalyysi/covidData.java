package com.example.covidanalyysi;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

// JSON data is read in this class, and it has been implemented
// using singleton, so data will remain unaltered during execution.

public class covidData
{
    private ArrayList<healthCareDistrict> HCD_array = new ArrayList<>();
    private ArrayList<healthCareDistrict> fav_Array = new ArrayList<>();
    private JSONObject DW_labels = null;

    // Current spinner healthcare district id.
    // Default to 21 "Kaikki alueet".
    private int HCDId = 21;
    private int favIndex;
    sortWeeks time = new sortWeeks();

    private covidData()
    {

    }

    private static covidData JSONData = new covidData();

    public static covidData getInstance()
    {
        return JSONData;
    }


    // In this method JSON data is retrieved from THL open data source.
    // This method returns a JSON object containing all the data from data source.
    private JSONObject getJSON()
    {
        JSONObject all = null;
        try {
            URL url = new URL("https://sampo.thl.fi/pivot/prod/fi/epirapo/covid19case/fact_epirapo_covid19case.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String response = sb.toString();
            in.close();

            all = new JSONObject(response);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return all;
    }


    // In this method data is first split into smaller objects.
    // Also objects are made from healthCareDistrict.java and the values
    // of sorted THL data are added to the objects.
    // Objects are added to the arraylist HCD_array.
    public void sortData()
    {
        int j = 0;
        try
        {
            JSONObject dataset = getJSON().getJSONObject("dataset");
            JSONObject dimensions = dataset.getJSONObject("dimension");

            JSONObject HCD = dimensions.getJSONObject("hcdmunicipality2020");
            JSONObject HCD_category = HCD.getJSONObject("category");
            JSONObject HCD_index = HCD_category.getJSONObject("index");
            JSONObject HCD_labels = HCD_category.getJSONObject("label");

            JSONObject dateWeeks = dimensions.getJSONObject("dateweek20200101");
            JSONObject DW_category = dateWeeks.getJSONObject("category");
            JSONObject DW_index = DW_category.getJSONObject("index");
            DW_labels = DW_category.getJSONObject("label");

            JSONObject infections = dataset.getJSONObject("value");

            JSONArray HCD_key = HCD_index.names();
            JSONArray DW_key = DW_index.names();
            JSONArray inf_key = infections.names();

            for (int i = 0; i < HCD_key.length(); i++)
            {
                String HCD_keys = HCD_key.getString(i);
                healthCareDistrict dist = new healthCareDistrict();
                dist.setDistrictName(HCD_labels.getString(HCD_keys));
                dist.setId(HCD_index.getInt(HCD_keys));

                for (int k = 0; k < DW_key.length(); k++, j++)
                {
                    String DW_keys = DW_key.getString(k);
                    if (DW_index.getInt(DW_keys) == ((158 - 52) + time.getWeekNum() - 1))
                    {
                        break;
                    }
                    else
                    {
                        String inf_keys = inf_key.getString(j);
                        dist.setWeekNum(DW_index.getInt(DW_keys));
                        dist.setWeeklyInfections(DW_index.getInt(DW_keys), infections.getInt(inf_keys));
                    }
                }
                HCD_array.add(dist);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    // This method adds an object to the arraylist containing favourites. (fav_Array)
    public void addFavourites(healthCareDistrict fav)
    {
        fav_Array.add(fav);
    }

    public ArrayList<healthCareDistrict> getFav_Array()
    {
        return fav_Array;
    }

    public ArrayList<healthCareDistrict> getHCD_array()
    {
        return HCD_array;
    }


    // HCDId variable defines the id of the current healthcare district shown in the spinner widget
    // so these methods are for usage of current HCD.
    public void setHCDId(int newId)
    {
        HCDId = newId;
    }

    public int getHCDId()
    {
        return HCDId;
    }


    // favIndex is used to get the index of current favourite from an arraylist fav_Array.
    public void setFavIndex(int i)
    {
        favIndex = i;
    }

    public int getFavIndex()
    {
        return favIndex;
    }


    // This method returns the JSON object containing weeks and years as Strings.
    // Used to format time selection spinner items to a readable form.
    public JSONObject getDW_labels()
    {
        return DW_labels;
    }
}
