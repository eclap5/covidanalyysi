package com.example.covidanalyysi;

import java.util.HashMap;


// Class for healthcare district type objects.
public class HealthCareDistrict
{
    private String districtName = "";
    private int id = 0;
    private int weeklyInfections = 0;


    // Weekly infections are stored in HashMap with week number as key object.
    private HashMap<Integer, Integer> infectionsPerWeek = new HashMap<Integer, Integer>();

    public HealthCareDistrict()
    {

    }

    public void setId(int ID)
    {
        id = ID;
    }

    public void setDistrictName(String disName)
    {
        districtName = disName;
    }

    public void setWeeklyInfections(int weekNum, int inf)
    {
        infectionsPerWeek.put(weekNum, inf);
    }

    public String getDistrictName()
    {
        return districtName;
    }

    public int getWeeklyInfections(int wNum)
    {
        weeklyInfections = infectionsPerWeek.get(wNum);
        return weeklyInfections;
    }

    public HashMap<Integer, Integer> getInfectionsPerWeek()
    {
        return infectionsPerWeek;
    }

    public int getId()
    {
        return id;
    }

    public String toString()
    {
        return districtName;
    }


    // Returns the number of weeks in hashmap.
    public int getMaxWeekNum()
    {
        int maxWeekNum = 0;

        for (int i = 0; i < infectionsPerWeek.size(); i++) {
            maxWeekNum++;
        }
        return maxWeekNum;
    }

}


