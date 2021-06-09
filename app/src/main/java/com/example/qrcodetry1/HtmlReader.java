package com.example.qrcodetry1;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.webkit.CookieManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HtmlReader {

    String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public void readHTML(String url,String auth_token, StudentAdminRequest request, Context context) throws  IOException{
       // Log.i("url",url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = Jsoup.parse(url);


                //build json object instead
                //put try catch on every field to prevent failure
                //try changing id

                JSONObject student_info = new JSONObject();
                try {
                    student_info.put("greekFname",  doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblGreekFirstName").text());
                    student_info.put("greekLname", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblGreekLastName").text());
                    student_info.put("latinFname", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblLatinFirstName").text());
                    student_info.put("latinLname", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblLatinLastName").text());
                    student_info.put("address", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblAddress").text());
                    //student_info.put("zipCode", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblZipCode").text());
                    student_info.put("city", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblCity").text());
                    //student_info.put("prefecture", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblPrefecture").text());
                    student_info.put("institution", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentInstitution").text());
                    student_info.put("school", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentSchool").text());
                    student_info.put("department", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentDepartment").text());
                    student_info.put("academicAddress",  doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentAcademicAddress").text());
                   // student_info.put("academicZipCode",  doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentAcademicZipCode").text());
                   // student_info.put("academicPrefecture",  doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentAcademicPrefecture").text());
                    student_info.put("academicCity", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentAcademicCity").text());
                    student_info.put("studentshipType",doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_ucAcademicInfoView_lblStudentshipType").text());
                    student_info.put("studentNumber", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_ucAcademicInfoView_lblStudentNumber").text());
                   // student_info.put("studentAMKA", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_ucAcademicInfoView_lblAMKA").text());
                    student_info.put("entryDate",doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_ucAcademicInfoView_lblEntryDate").text());
                    student_info.put("photo", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_imgPhoto").attr("src"));
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }



                Log.i("student", student_info.toString());
                request.setPhotourl(doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_imgPhoto").attr("src"));
                try {
                    String base64_img = downloadPhoto(context, student_info.getString("photo"));
                    student_info.remove("photo");
                    student_info.put("photo", base64_img);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                request.signup_student(auth_token,student_info);
            }
        }).start();

    }


    private String downloadPhoto(Context context,String downloadUrlOfImage){
        String encodedImage = null;
        Log.i("download","here");
        try {
            String cookie =  CookieManager.getInstance().getCookie(downloadUrlOfImage);
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("photo_id")
                    .addRequestHeader("Cookie",cookie)
                    .addRequestHeader("User-Agent", "{'User-Agent':\"Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.27 Safari/537.17\"}")
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + "photo_id.jpg");
            dm.enqueue(request);
            Log.i("download","image downloaded");
        } catch (Exception ex) {
            Log.i("download","image not downloaded");
        }

        String pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File imgFile = new  File(pictures+"/photo_id.jpg");
        Log.i("path",imgFile.getAbsolutePath());
        if(imgFile.exists()) {
            Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Log.i("download","found photo");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();
            encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            //Log.i("base64", encodedImage);
        }else{
            downloadPhoto(context,downloadUrlOfImage);
           Log.i("download","not found photo");
        }
        return encodedImage;
    }

}
