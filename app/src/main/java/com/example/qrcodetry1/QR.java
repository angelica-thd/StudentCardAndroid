package com.example.qrcodetry1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

import static android.content.Context.WINDOW_SERVICE;


public class QR {
    String path = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    public void generate_QR(Context context, String input, ImageView QRimg) throws WriterException {

        if (input.length() > 0) {
            WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(input, null, QRGContents.Type.TEXT, smallerDimension);
            try {

                bitmap = qrgEncoder.encodeAsBitmap();
                QRimg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new AlertDialog.Builder(context).setMessage("Fill your credentials first,please.");
        }
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            boolean save = new QRGSaver().save(path, "QRcode", bitmap, QRGContents.ImageType.IMAGE_JPEG);
            String result = save ? "QR saved" : "QR  not saved";
            //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            Log.i("qrsave",result);

        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

        public void scan_QR(Context context){
            IntentIntegrator integrator = new IntentIntegrator((Activity)context);
            integrator = integrator.setCaptureActivity(CaptureAct.class);
            integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scanning QR");
            integrator.initiateScan();
        }
    

    
}


