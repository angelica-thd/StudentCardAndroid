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
                    student_info.put("photoURL", doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_imgPhoto").attr("src"));
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }


               /* String greekFname = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblGreekFirstName").text();
                String greekLname = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblGreekLastName").text();
                String latinFname = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblLatinFirstName").text();
                String latinLname = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblLatinLastName").text();
                String address = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblAddress").text();
                String zipCode = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblZipCode").text();
                String city = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblCity").text(); //kallikratikos dhmos
                String prefecture = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblPrefecture").text(); //perifereiakh enothta
                String institution = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentInstitution").text();    //idryma
                String school = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentSchool").text();  //sxolh
                String department = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentDepartment").text();
                String academicAddress = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentAcademicAddress").text();
                String academicZipCode = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentAcademicZipCode").text();
                String academicPrefecture = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentAcademicPrefecture").text();
                String academicCity = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblStudentAcademicCity").text();
                String studentshipType = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_ucAcademicInfoView_lblStudentshipType").text(); //proptyxiakos/metaptyxiakos
                String studentNumber = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_ucAcademicInfoView_lblStudentNumber").text();
                String studentAMKA = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_ucAcademicInfoView_lblAMKA").text();
                String entryDate = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_ucAcademicInfoView_lblEntryDate").text();
                String academicPhoto = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_imgPhoto").attr("src");

                //Student student = new Student
                  //      .Builder(greekFname,greekLname,latinFname,latinLname,address,zipCode,city,prefecture,institution,school,department,academicAddress,academicZipCode,academicPrefecture,academicCity,studentshipType,studentNumber,studentAMKA,entryDate,academicPhoto)
                   //     .build();

                //dosnt work
                //byte[] photobytes = academicPhoto.getBytes();

                //Bitmap bmp = BitmapFactory.decodeByteArray(photobytes,0,photobytes.length);
               // ImageView image=new ImageView(context);
                //image.setImageBitmap(bmp);

                stringBuilder.append("\n\n").append("ΣΤΟΙΧΕΙΑ ΦΟΙΤΗΤΗ")
                        .append("\n").append("ΌΝΟΜΑ(ΕΛΛΗΝΙΚΑ): ").append(greekFname)
                        .append("\n").append("ΕΠΙΘΕΤΟ(ΕΛΛΗΝΙΚΑ): ").append(greekLname)
                        .append("\n").append("ΟΝΟΜΑ(ΛΑΤΙΝΙΚΑ): ").append(latinFname)
                        .append("\n").append("ΕΠΙΘΕΤΟ(ΛΑΤΙΝΙΚΑ): ").append(latinLname)
                        .append("\n").append("ΔΙΕΥΘΥΝΣΗ: ").append(address)
                        .append("\n").append("Τ.Κ.: ").append(zipCode)
                        .append("\n").append("ΠΟΛΗ: ").append(city)
                        .append("\n").append("ΚΑΛΛΙΚΡΑΤΙΚΟΣ ΔΗΜΟΣ: ").append(prefecture)
                        .append("\n").append("ΙΔΡΥΜΑ: ").append(institution)
                        .append("\n").append("ΣΧΟΛΗ: ").append(school)
                        .append("\n").append("ΤΜΗΜΑ: ").append(department)
                        .append("\n").append("ΙΔΙΟΤΗΤΑ ΦΟΙΤΗΤΗ: ").append(studentshipType)
                        .append("\n").append("ΑΡΙΘΜΟΣ ΜΗΤΡΩΟΥ: ").append(studentNumber)
                        .append("\n").append("ΑΜΚΑ: ").append(studentAMKA)
                        .append("\n").append("ΗΜΕΡΟΜΗΝΙΑ ΕΙΣΑΓΩΓΗΣ: ").append(entryDate)
                        .append("\n\n").append("ΣΤΟΙΧΕΙΑ ΠΑΝΕΠΙΣΤΗΜΙΟΥ ")
                        .append("\n").append("ΔΙΕΥΘΥΝΣΗ: ").append(academicAddress)
                        .append("\n").append("Τ.Κ.: ").append(academicZipCode)
                        .append("\n").append("ΚΑΛΛΙΚΡΑΤΙΚΟΣ ΔΗΜΟΣ: ").append(academicPrefecture)
                        .append("\n").append("ΠΟΛΗ: ").append(academicCity)
                        .append("\n\n").append("PHOTO")
                        .append("\n").append(photobytes.toString()); */

                Log.i("student", student_info.toString());
                request.setPhotourl(doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_imgPhoto").attr("src"));
                try {
                    String base64_img = downloadPhoto(context, student_info.getString("photoURL"));
                    student_info.remove("photoURL");
                    student_info.put("photoURL", base64_img);
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
            Log.i("base64", encodedImage);
        }else{
            downloadPhoto(context,downloadUrlOfImage);
           Log.i("download","not found photo");
        }
        return encodedImage;
    }

}
