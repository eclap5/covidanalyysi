package com.example.covidanalyysi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


// This class implements the functionalities of fragment_favourites interface.
// Implementation made with ListView to show users favourites.
public class Favourites extends Fragment
{
    ListView listView;
    Button button;
    CovidData JSONData = CovidData.getInstance();
    HandleCSV handler = HandleCSV.getInstance();
    CredentialsDataBase credentialsDataBase = CredentialsDataBase.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);
        listView = (ListView)v.findViewById(R.id.listview);
        button = (Button) v.findViewById(R.id.button4);
        ArrayAdapter<HealthCareDistrict> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, JSONData.getFavouritesArray());
        listView.setAdapter(adapter);


        // When clicking an item in ListView the fragment is changed to HCDSelection and current HCDId is
        // changed to the healthcare district clicked from favourites.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Fragment fragment = null;
                try {
                    fragment = (Fragment) HCDSelection.class.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
                JSONData.setFavIndex(i);
                JSONData.setHCDId(JSONData.getFavouritesArray().get(i).getId());
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(((ViewGroup)getView().getParent()).getId(), fragment).addToBackStack(null).commit();
            }
        });


        // Favourites will be saved to CSV file when clicking the button.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (credentialsDataBase.getLogInStatus())
                {
                    handler.writeCSV(getActivity().getApplicationContext());
                    Toast.makeText(getContext(), "Suosikit tallennettu", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Kirjaudu sis????n tallentaaksesi suosikkeja", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }
}
