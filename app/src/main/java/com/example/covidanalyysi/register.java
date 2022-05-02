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


// This class implements the functionalities for fragment_register interface.
public class register extends Fragment
{
    Button bt_register;
    EditText et_username;
    EditText et_password;
    EditText et_password2;

    DatabaseHelper databasehelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        bt_register = (Button) v.findViewById(R.id.bt_registerReg);
        et_username = (EditText) v.findViewById(R.id.et_usernameReg);
        et_password = (EditText) v.findViewById(R.id.et_passwordReg);
        et_password2 = (EditText) v.findViewById(R.id.et_passwordReg2);

        databasehelper = new DatabaseHelper(getActivity());


        // Button click checks the username from database and creates new user.
        // Fragment will be changed to home page.
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String confirm_password = et_password2.getText().toString();

                if(username.equals("") || password.equals("") || confirm_password.equals(""))
                {
                    Toast.makeText(getActivity(), "Täytä vaaditut kentät", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (password.equals(confirm_password)) {
                        Boolean checkusername = databasehelper.CheckUsername(username);
                        if (checkusername == true) {
                            Boolean insert = databasehelper.Insert(username, password);
                            if (insert == true) {
                                Toast.makeText(getActivity(), "Rekisteröity", Toast.LENGTH_SHORT).show();
                                et_username.setText("");
                                et_password.setText("");
                                et_password2.setText("");
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
                            }
                        } else {
                            Toast.makeText(getActivity(), "Käyttäjänimi varattu", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Salasanat eivät täsmää", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return v;
    }
}
