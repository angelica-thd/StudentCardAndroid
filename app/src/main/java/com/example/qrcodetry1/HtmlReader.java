package com.example.qrcodetry1;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
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

    public void readHTML(String url,String auth_token, StudentAdminRequest request,Context context) throws  IOException{
       // Log.i("url",url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder stringBuilder = new StringBuilder();
                Document doc = Jsoup.parse(url);
               // Log.i("doc",doc.toString());
                String title = doc.title();

                //build json object instead
                //put try catch on every field to prevent failure
                //try changing id

                String greekFname = doc.getElementById("ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_lblGreekFirstName").text();
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

                Student student = new Student
                        .Builder(greekFname,greekLname,latinFname,latinLname,address,zipCode,city,prefecture,institution,school,department,academicAddress,academicZipCode,academicPrefecture,academicCity,studentshipType,studentNumber,studentAMKA,entryDate,academicPhoto)
                        .build();
                Log.i("student", student.toString());
                //dosnt work
                byte[] photobytes = academicPhoto.getBytes();

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
                        .append("\n").append(photobytes.toString());
                Log.i("info",stringBuilder.toString());

                StringBuilder name = new StringBuilder().append(student.getGreekFname()).append(" ").append(student.getGreekLname()).append("/\n").append(student.getLatinFname()).append(" ").append(student.getLatinLname());
                request.signup_student(auth_token,name.toString(),student.getInstitution(),student.getDepartment(),student.getStudentNumber(),student);


            }
        }).start();

    }

}
