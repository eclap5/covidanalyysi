package com.example.covidanalyysi;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;


// This class handles time and weekly strings from DWLabels JSON object.
public class SortWeeks
{
    public SortWeeks()
    {

    }

    // This method returns current week number.
    public int getWeekNum()
    {
        int weekNum = 0;
        Date date = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(GregorianCalendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);
        calendar.setTime(date);
        weekNum = calendar.get(GregorianCalendar.WEEK_OF_YEAR);
        return weekNum;
    }


    // This method sorts the weekly strings from JSON object "DWLabels"
    // and adds the strings to an arraylist.
    public ArrayList<String> getWeekArray(JSONObject DW_labels)
    {
        ArrayList<String> weekArray = new ArrayList<>();
        JSONArray week_key = DW_labels.names();
        for (int i = 0; i < week_key.length(); i++)
        {
            try
            {
                String week_keys = week_key.getString(i);
                {
                    weekArray.add(DW_labels.getString(week_keys));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        weekArray.remove(157);
        convertWeeks(weekArray);
        return weekArray;
    }


    // This method converts the weekly strings in arraylist to more readable form
    // so it will be easier for user to specify time scale.
    public void convertWeeks(@NonNull ArrayList<String> weekArray)
    {
        Date date;
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(GregorianCalendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);

        for (int i = 0; i < weekArray.size(); i++)
        {
            String var[];
            String time = weekArray.get(i);
            var = time.split(" ");
            calendar.setWeekDate(Integer.parseInt(var[1]), Integer.parseInt(var[3]), GregorianCalendar.MONDAY);
            date = calendar.getTime();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int month = localDate.getMonthValue();
            String weekConverted = "";

            if (month == 1) {
                weekConverted = "Vko " + var[3] + "/Tammikuu " + var[1];
            } else if (month == 2) {
                weekConverted = "Vko " + var[3] + "/Helmikuu " + var[1];
            } else if (month == 3) {
                weekConverted = "Vko " + var[3] + "/maaliskuu " + var[1];
            } else if (month == 4) {
                weekConverted = "Vko " + var[3] + "/Huhtikuu " + var[1];
            } else if (month == 5) {
                weekConverted = "Vko " + var[3] + "/Toukokuu " + var[1];
            } else if (month == 6) {
                weekConverted = "Vko " + var[3] + "/Kesäkuu " + var[1];
            } else if (month == 7) {
                weekConverted = "Vko " + var[3] + "/Heinäkuu " + var[1];
            } else if (month == 8) {
                weekConverted = "Vko " + var[3] + "/Elokuu " + var[1];
            } else if (month == 9) {
                weekConverted = "Vko " + var[3] + "/Syyskuu " + var[1];
            } else if (month == 10) {
                weekConverted = "Vko " + var[3] + "/Lokakuu " + var[1];
            } else if (month == 11) {
                weekConverted = "Vko " + var[3] + "/Marraskuu " + var[1];
            } else if (month == 12) {
                weekConverted = "Vko " + var[3] + "/Joulukuu " + var[1];
            }
            weekArray.set(i, weekConverted);
        }
    }
}
