package com.example.covidanalyysi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    EditText et_username;
    EditText et_password;

    DatabaseHelper databasehelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        bt_login = (Button) v.findViewById(R.id.bt_login);
        bt_register = (Button) v.findViewById(R.id.bt_register);
        et_username = (EditText) v.findViewById(R.id.et_username);
        et_password = (EditText) v.findViewById(R.id.et_password);

        databasehelper = new DatabaseHelper(getActivity());


        // This button changes the fragment to fragment_register interface.
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                try {
                    fragment = (Fragment) register.class.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(((ViewGroup)getView().getParent()).getId(), fragment).addToBackStack(null).commit();
            }
        });


        // This button click checks the credentials and logs user in if credentials are correct.
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                Boolean checklogin = databasehelper.CheckLogin(username, password);
                if(checklogin == true){
                    Toast.makeText(getActivity(), "Kirjautuminen onnistui", Toast.LENGTH_SHORT).show();
                    Fragment fragment = null;
                    try {
                        fragment = (Fragment) home.class.newInstance();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    }
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(((ViewGroup)getView().getParent()).getId(), fragment).addToBackStack(null).commit();
                    getActivity().setTitle("CovidAnalyysi");
                }else{
                    Toast.makeText(getActivity(), "Virheellinen käyttäjätunnus tai salasana", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }
}
