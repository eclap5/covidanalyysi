package com.example.covidanalyysi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


// This class implements the functionalities for fragment_login interface.
public class Login extends Fragment
{
    Button bt_login;
    Button bt_register;
    Button bt_logout;

    EditText et_username;
    EditText et_password;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        bt_login = (Button) v.findViewById(R.id.bt_login);
        bt_register = (Button) v.findViewById(R.id.bt_register);
        bt_logout = (Button) v.findViewById(R.id.logout);
        et_username = (EditText) v.findViewById(R.id.et_username);
        et_password = (EditText) v.findViewById(R.id.et_password);


        CredentialsDataBase credentialsDataBase = CredentialsDataBase.getInstance();


        // This button changes the fragment to fragment_register interface.
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!credentialsDataBase.getLogInStatus())
                {
                    Fragment fragment = null;
                    try {
                        fragment = (Fragment) register.class.newInstance();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    }
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();
                }
                else
                {
                    Toast.makeText(getActivity(), "Olet jo rekisteröitynyt", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // This button click checks the credentials from credentials database and logs user in if credentials are correct.
        // If user log in is complete, the system retrieves user spesific favourites from CSV file and displays them in "Suosikit" tab.
        // After log in the fragment will change to home page.
        // Incorrect credentials will be notified by toast notification.
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Keyboard will close automatically when clicked.
                if (et_username.hasFocus() || et_password.hasFocus())
                {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }

                if (!credentialsDataBase.getLogInStatus())
                {
                    credentialsDataBase.readCredentials(getActivity().getApplicationContext());

                    String username = et_username.getText().toString();
                    String password = et_password.getText().toString();
                    String credentials[];

                    for (int i = 0; i < credentialsDataBase.getUsernameArray().size(); i++)
                    {
                        credentials = credentialsDataBase.getUsernameArray().get(i).split(";");
                        if (username.equals(credentials[0]) && password.equals(credentials[1]))
                        {
                            credentialsDataBase.setLogInStatus(true);
                            break;
                        }
                    }
                    if (credentialsDataBase.getLogInStatus())
                    {
                        credentialsDataBase.setUsername(username);

                        handleCSV handler = handleCSV.getInstance();
                        handler.getFavouritesFromCSV(getActivity().getApplicationContext());

                        Toast.makeText(getActivity(), "Kirjautuminen onnistui", Toast.LENGTH_SHORT).show();

                        Fragment fragment = null;
                        try {
                            fragment = (Fragment) home.class.newInstance();
                        } catch (IllegalAccessException | java.lang.InstantiationException e) {
                            e.printStackTrace();
                        }
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();
                        getActivity().setTitle("CovidAnalyysi");
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Virheellinen käyttäjätunnus tai salasana", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Olet jo kirjautunut sisään", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }
}
