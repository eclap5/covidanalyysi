package com.example.covidanalyysi;


import android.content.Context;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

// This class implements functionalities of saving and retrieving user log in credentials.
// This class has been implemented as singleton to prevent CSV data of being altered during execution.
public class CredentialsDataBase
{
    private String credentials;
    private String username;
    private String password;
    private Boolean logInStatus = false;
    private ArrayList<String> usernameArray = new ArrayList<>();

    private CredentialsDataBase()
    {

    }

    private static CredentialsDataBase credentialsInstance = new CredentialsDataBase();

    public static CredentialsDataBase getInstance()
    {
        return credentialsInstance;
    }


    // Credentials will be written to CSV file called 'credentials.CSV'.
    public void writeCredentials(Context c)
    {
        credentials = username + ";" + password + "\n";
        try {
            OutputStreamWriter ows = new OutputStreamWriter(c.openFileOutput("credentials.csv", Context.MODE_APPEND));
            ows.write(credentials);
            ows.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // User credentials are read from CSV file to arraylist for further use.
    public void readCredentials(Context c)
    {
        String line;
        try {
            InputStream ins = c.openFileInput("credentials.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            while ((line = br.readLine()) != null)
            {
                usernameArray.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getUsernameArray() {
        return usernameArray;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setLogInStatus(Boolean logInStatus)
    {
        this.logInStatus = logInStatus;
    }

    public Boolean getLogInStatus()
    {
        return logInStatus;
    }
}
