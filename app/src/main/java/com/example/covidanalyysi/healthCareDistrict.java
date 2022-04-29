package com.example.covidanalyysi;

import java.util.HashMap;

public class healthCareDistrict
{
    private String districtName = "";
    private int id = 0;
    private int weekNum = 0;
    private int weeklyInfections = 0;

    private HashMap<Integer, Integer> infPerWeek = new HashMap<Integer, Integer>();

    public healthCareDistrict()
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

    public void setWeekNum(int wNum)
    {
        weekNum = wNum;
    }

    public void setWeeklyInfections(int weekNum, int inf)
    {
        infPerWeek.put(weekNum, inf);
    }

    public String getDistrictName()
    {
        return districtName;
    }

    public int getWeeklyInfections(int wNum)
    {
        weeklyInfections = infPerWeek.get(wNum);
        return weeklyInfections;
    }

    public HashMap<Integer, Integer> getInfPerWeek()
    {
        return infPerWeek;
    }

    public int getId()
    {
        return id;
    }

    public String toString()
    {
        return districtName;
    }

    public int getMaxWeekNum()
    {
        int maxWeekNum = 0;

        for (int i = 1; i < infPerWeek.size(); i++) {
            maxWeekNum++;
        }
        System.out.println(maxWeekNum);
        return maxWeekNum;
    }

}


