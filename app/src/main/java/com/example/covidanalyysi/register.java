package com.example.covidanalyysi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        bt_register = (Button) v.findViewById(R.id.bt_registerReg);
        et_username = (EditText) v.findViewById(R.id.et_usernameReg);
        et_password = (EditText) v.findViewById(R.id.et_passwordReg);
        et_password2 = (EditText) v.findViewById(R.id.et_passwordReg2);

        CredentialsDataBase credentialsDataBase = CredentialsDataBase.getInstance();

        // Button click checks the username from credential database and creates new user.
        // If required fields are not filled or requirements are not met the app will infrom user with toast notification.
        // Fragment will be changed to home page.
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // Keyboard will close automatically when clicked.
                if (et_username.hasFocus() || et_password.hasFocus() || et_password2.hasFocus())
                {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }

                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String confirm_password = et_password2.getText().toString();

                if(username.equals("") || password.equals("") || confirm_password.equals(""))
                {
                    Toast.makeText(getActivity(), "Täytä vaaditut kentät", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (password.equals(confirm_password))
                    {
                        if (checkPassword(password))
                        {
                            if (!credentialsDataBase.getUsernameArray().contains(username))
                            {
                                credentialsDataBase.setUsername(username);
                                credentialsDataBase.setPassword(password);
                                credentialsDataBase.writeCredentials(getActivity().getApplicationContext());
                                credentialsDataBase.setLogInStatus(true);

                                Toast.makeText(getActivity(), "Rekisteröity", Toast.LENGTH_SHORT).show();

                                et_username.setText("");
                                et_password.setText("");
                                et_password2.setText("");

                                Fragment fragment = null;
                                try
                                {
                                    fragment = (Fragment) home.class.newInstance();
                                } catch (IllegalAccessException e)
                                {
                                    e.printStackTrace();
                                } catch (java.lang.InstantiationException e)
                                {
                                    e.printStackTrace();
                                }
                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                transaction.replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();
                                getActivity().setTitle("CovidAnalyysi");
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Käyttäjänimi varattu", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Salasanan täytyy sisältää yksi iso kirjain, yksi pieni kirjain," +
                                    " yksi numero, yksi erikoismerkki ja olla vähintään 12 merkkiä pitkä", Toast.LENGTH_LONG).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Salasanat eivät täsmää", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return v;
    }


    // Password will be checked for length of 12 characters and uppercase, lowercase, number and special character.
    // If required features are not met the method returns false.
    public boolean checkPassword(String password)
    {
        if (password.length() < 12)
        {
            return false;
        }

        if (!(password.contains("@") || password.contains("#")
                || password.contains("!") || password.contains("~")
                || password.contains("$") || password.contains("%")
                || password.contains("^") || password.contains("&")
                || password.contains("*") || password.contains("(")
                || password.contains(")") || password.contains("-")
                || password.contains("+") || password.contains("/")
                || password.contains(":") || password.contains(".")
                || password.contains(",") || password.contains("<")
                || password.contains(">") || password.contains("?")
                || password.contains("|")))
        {
            return false;
        }

        int count = 0;
        for (int i = 65; i <= 90; i++)
        {
            char c = (char) i;
            String upperCase = Character.toString(c);
            if (password.contains(upperCase))
            {
                count = 1;
            }
        }
        if (count == 0)
        {
            return false;
        }

        count = 0;
        for (int i = 97; i <= 122; i++)
        {
            char c = (char) i;
            String lowerCase = Character.toString(c);
            if (password.contains(lowerCase))
            {
                count = 1;
            }
        }
        if (count == 0)
        {
            return false;
        }

        for (int i = 0; i < 10; i++)
        {
            count = 0;
            if (password.contains(String.valueOf(i)));
            {
                count = 1;
            }

        }
        if (count == 0)
        {
            return false;
        }

        return true;
    }
}
