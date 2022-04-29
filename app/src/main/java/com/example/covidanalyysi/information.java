package com.example.covidanalyysi;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class information extends Fragment
{
    TextView linkText;
    TextView linkText2;
    TextView linkText3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_information, container, false);
        linkText = v.findViewById(R.id.link1);
        linkText2 = v.findViewById(R.id.link2);
        linkText3 = v.findViewById(R.id.link3);

        linkText.setMovementMethod(LinkMovementMethod.getInstance());
        linkText2.setMovementMethod(LinkMovementMethod.getInstance());
        linkText3.setMovementMethod(LinkMovementMethod.getInstance());
        return v;
    }
}
