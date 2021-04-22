package com.example.qrcodetry1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class Fragment_admin extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        Button button = (Button) view.findViewById(R.id.login_btn);
        button.setOnClickListener(v -> {
            startActivity(new Intent(view.getContext(), Login_or_Reg.class));
        });
        // Inflate the layout for this fragment
        return view;
    }
}