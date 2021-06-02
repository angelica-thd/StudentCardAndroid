package com.example.qrcodetry1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class StudentInfoReg extends AppCompatActivity {
    private StudentAdminRequest studentAdminRequest;
    private WebView webView;
    private String htmlSource,auth_token;

    private String weburl,userAgent,photocookie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info_reg);
        Context activity = this;
        studentAdminRequest = new StudentAdminRequest(this);
        WebView webView = findViewById(R.id.webView2);

        auth_token = getIntent().getStringExtra("auth_token");
        userAgent = "{'User-Agent':\"Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.27 Safari/537.17\"}";
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();

        webView.setWebViewClient(new StudentInfoReg.MyWebViewClient());
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString(userAgent);
        webView.addJavascriptInterface(new StudentInfoReg.LoadListener(), "HTMLOUT");

        String eduURL = "https://submit-academicid.minedu.gov.gr";
        //String eduURL = "https://wayf.grnet.gr/?entityID=https%3A%2F%2Fsubmit-paso.minedu.gov.gr%2Fshibboleth&return=https%3A%2F%2Fsubmit-academicid.minedu.gov.gr%2FShibboleth.sso%2FLogin%3FSAMLDS%3D1%26target%3Dss%253Amem%253Adc613920d8b66c5370bdfae91d09047dd2d9a14113691ae19739c63d79672a9b";
        String dummyURL = "https://www.google.com";
        String targetURL = "https://submit-academicid.minedu.gov.gr/Secure/Students/Default.aspx";
        String dummyTarget = "https://https://www.w3schools.com/html/html_scripts.asp";


        webView.loadUrl(eduURL);      //replace occasionally with dummyURL to avoid too many pings on the edu URL
        Log.i("scroll", String.valueOf(webView.getScrollX())+ " " + String.valueOf(webView.getScrollY()));

        /* if url is google, the html page is stored in this .txt file
        HtmlReader reader = new HtmlReader();
        String htmlFile = reader.readFromFile(activity);
        try {
            reader.readHTML(htmlFile,activity);
        } catch (IOException e) {
            e.printStackTrace();
        } */

    }



    private class MyWebViewClient extends WebViewClient {
        String dummyURL = "https://www.google.com";
        String eduURL = "https://submit-academicid.minedu.gov.gr";
        String targetURL = "https://submit-academicid.minedu.gov.gr/Secure/Students/Default.aspx";

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            weburl = url;
            Log.i("webView_URL_activity", url);
            if (eduURL.contains(url)) {
                // This is my website, so do not override; let my WebView load the page
                Toast.makeText(view.getContext(), "starting point", Toast.LENGTH_LONG).show();
                view.scrollTo(view.getWidth(), view.getHeight());
            } else if (targetURL.contains(url)) {
                Toast.makeText(view.getContext(), "reached target", Toast.LENGTH_LONG).show();
                view.setOnTouchListener((v, event) -> true);        //disable clicking on webview
            }
            return false;
        }

        public void onPageFinished(WebView view, String url) {
            if (url.contains(targetURL)) {
                view.scrollTo(0, 2 * view.getHeight());
                view.setDrawingCacheEnabled(true);
                Bitmap bitmap = view.getDrawingCache();
                new Intent(StudentInfoReg.this, MainStudent.class).putExtra("photo_id", bitmap);
                //view.loadUrl("javascript:window.open(document.getElementById('ctl00_ctl00_cphMain_cphSecureMain_ucDeliveredToStudentApplication_dxStudentApplicationPreview_ucApplicationView_imgPhoto').getAttribute('src'))");

                view.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].outerHTML+'</html>');");

            }
        }
    }


    public class LoadListener {
        @JavascriptInterface
        public void processHTML(String html) throws IOException {
            htmlSource = html;
            //Log.i("result",html);
            HtmlReader reader = new HtmlReader();
            reader.readHTML(html,auth_token,studentAdminRequest, StudentInfoReg.this);

           /* if url is dummyURL write .txt file
            try{
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(activity.openFileOutput("config.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(html);
                outputStreamWriter.close();
            }catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }*/

        }
    }
}


