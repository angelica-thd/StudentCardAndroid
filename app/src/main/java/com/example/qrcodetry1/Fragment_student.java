package com.example.qrcodetry1;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Fragment_student extends Fragment {
    FloatingActionButton fab;
   // ImageView photoID;
    EditText email,username,name,pass,pass_conf;
    private StudentAdminRequest studentAdminRequest;
    private static final int PICK_IMG = 2345;
    private Uri imgURI;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_student, container, false);
        studentAdminRequest = new StudentAdminRequest(view.getContext());
        email = (EditText) view.findViewById(R.id.email_st);
        username = (EditText) view.findViewById(R.id.username_st);
        name = (EditText) view.findViewById(R.id.fname_st);
        pass_conf = (EditText) view.findViewById(R.id.pass_st);
        pass = (EditText) view.findViewById(R.id.pass_conf_st);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Log.i("click","click");
            if(!email.getText().toString().equals("")
                    || !email.getText().toString().equals(null)
                    || !username.getText().toString().equals("")
                    || !username.getText().toString().equals(null)
                    || !name.getText().toString().equals("")
                    || !name.getText().toString().equals(null)
                    || !pass.getText().toString().equals("")
                    || !pass.getText().toString().equals(null)
                    || !pass.getText().toString().equals("")
                    || !pass_conf.getText().toString().equals(null)) {

               if(!pass.getText().toString().equals(pass_conf.getText().toString())){
                   Toast.makeText(view.getContext(),R.string.invalid_pass,Toast.LENGTH_LONG).show();
               }else
                     studentAdminRequest.signup_user(username.getText().toString(),name.getText().toString(),email.getText().toString(),pass.getText().toString(),pass_conf.getText().toString());

            }else {
                Toast.makeText(view.getContext(), R.string.blankField, Toast.LENGTH_SHORT).show();
            }

        });
      /*  photoID = (ImageView) view.findViewById(R.id.photo_id);
        photoID.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,PICK_IMG);
        }); */
        // Inflate the layout for this fragment
        return view;

    }
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMG && resultCode == RESULT_OK && data != null && data.getData()!=null){
            imgURI = data.getData();
            photoID.setImageURI(imgURI);
            photoID.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round));
        }
    } */


}