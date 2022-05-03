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

public class CovidData
{
    private ArrayList<HealthCareDistrict> HCDArray = new ArrayList<>();
    private ArrayList<HealthCareDistrict> favouritesArray = new ArrayList<>();
    private JSONObject DWLabels = null;

    // Current spinner healthcare district id.
    // Default to 21 "Kaikki alueet".
    private int HCDId = 21;
    private int favIndex;

    private CovidData()
    {

    }

    private static CovidData JSONData = new CovidData();

    public static CovidData getInstance()
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
    // Objects are added to the arraylist HCDArray.
    // To cut the infections for proper healthcare district we us variable with value of
    // 157 to cut the JSON array when the HCD changes. 158 will be added to variable so next infections
    // will cut from correct position.
    public void sortData()
    {
        int j = 0;
        int infectionsWeeklyIndex = 157;
        try
        {
            JSONObject dataset = getJSON().getJSONObject("dataset");
            JSONObject dimensions = dataset.getJSONObject("dimension");

            JSONObject HCD = dimensions.getJSONObject("hcdmunicipality2020");
            JSONObject HCDCategory = HCD.getJSONObject("category");
            JSONObject HCDIndex = HCDCategory.getJSONObject("index");
            JSONObject HCDLabels = HCDCategory.getJSONObject("label");

            JSONObject dateWeeks = dimensions.getJSONObject("dateweek20200101");
            JSONObject DWCategory = dateWeeks.getJSONObject("category");
            JSONObject DWIndex = DWCategory.getJSONObject("index");
            DWLabels = DWCategory.getJSONObject("label");

            JSONObject infections = dataset.getJSONObject("value");

            JSONArray HCDIndexKey = HCDIndex.names();
            JSONArray DWIndexKey = DWIndex.names();
            JSONArray infectionsKey = infections.names();

            for (int i = 0; i < HCDIndexKey.length(); i++)
            {
                String HCDKeys = HCDIndexKey.getString(i);
                HealthCareDistrict dist = new HealthCareDistrict();
                dist.setDistrictName(HCDLabels.getString(HCDKeys));
                dist.setId(HCDIndex.getInt(HCDKeys));

                for (int k = 0; k < DWIndexKey.length(); k++, j++)
                {
                    String DWKeys = DWIndexKey.getString(k);
                    if (infectionsKey.getInt(j) == infectionsWeeklyIndex)
                    {
                        String infectionsKeys = infectionsKey.getString(j);
                        dist.setWeeklyInfections(DWIndex.getInt(DWKeys), infections.getInt(infectionsKeys));
                        infectionsWeeklyIndex += 158;
                        break;
                    }
                    else
                    {
                        String inf_keys = infectionsKey.getString(j);
                        dist.setWeeklyInfections(DWIndex.getInt(DWKeys), infections.getInt(inf_keys));
                    }
                }
                HCDArray.add(dist);
            }
            trimHCDArray();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    // In the start of the hashmap in HealthCareDistrict object there is one extra
    // key and value at the key 0. This method removes the 0 key and value, so
    // HashMap start value is 1.
    public void trimHCDArray()
    {
        for (int i = 0; i < HCDArray.size(); i++)
        {
            HCDArray.get(i).getInfectionsPerWeek().remove(0);
        }
    }


    // This method adds an object to the arraylist containing favourites. (favouritesArray)
    public void addFavourites(HealthCareDistrict fav)
    {
        favouritesArray.add(fav);
    }

    public ArrayList<HealthCareDistrict> getFavouritesArray()
    {
        return favouritesArray;
    }

    public ArrayList<HealthCareDistrict> getHCDArray()
    {
        return HCDArray;
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
    public JSONObject getDWLabels()
    {
        return DWLabels;
    }
}
