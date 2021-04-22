package com.example.qrcodetry1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;


public class Register extends AppCompatActivity {
    private SQLiteDatabase db;
    private EditText fname,id,email;
    private StringBuilder inputValue = new StringBuilder();
    private ImageView QRimg;
    private TextView qrText;
    private Button logout;
    private QR qr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        fname= findViewById(R.id.fname);
        id = findViewById(R.id.id);
        email = findViewById(R.id.email);
        QRimg = findViewById(R.id.imageView);
        qrText = findViewById(R.id.qrTxt);
        logout = findViewById(R.id.logout2);
        qr = new QR();
        db = openOrCreateDatabase("TMPdb",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS credentials(id text, fname text,email varchar(255))");

        logout.setOnClickListener((view)->
            startActivity(new Intent(this,Login_or_Reg.class)));

    }

    public void save(View view) throws WriterException {
        db.execSQL("INSERT INTO credentials VALUES('"+id.getText().toString()+"','"+fname.getText().toString()+"','"+email.getText().toString()+"')");

        Toast.makeText(this,"Credentials saved.",Toast.LENGTH_LONG).show();
        inputValue.append(id.getText().toString()).append(fname.getText().toString());
        qrText.setText("Your QR code:");
        qr.generate_QR(this,inputValue.toString(),QRimg);
        fname.setText(null);
        id.setText(null);
        email.setText(null);
    }
}