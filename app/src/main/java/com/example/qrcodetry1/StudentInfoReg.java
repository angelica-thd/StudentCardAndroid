package com.example.qrcodetry1;

import android.annotation.SuppressLint;
import android.content.Context;
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
    private Context activity;
    private Student student;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info_reg);

        Context activity = this;
        studentAdminRequest = new StudentAdminRequest(this);
        WebView webView = findViewById(R.id.webView2);

        auth_token = getIntent().getStringExtra("auth_token");

        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();

        webView.setWebViewClient(new StudentInfoReg.MyWebViewClient());
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new StudentInfoReg.LoadListener(), "HTMLOUT");

        String eduURL = "https://submit-academicid.minedu.gov.gr";
        String dummyURL = "https://www.google.com";
        String targetURL = "https://submit-academicid.minedu.gov.gr/Secure/Students/Default.aspx";
        String dummyTarget = "https://https://www.w3schools.com/html/html_scripts.asp";


        webView.loadUrl(eduURL);      //replace occasionally with dummyURL to avoid too many pings on the edu URL


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

            Log.i("webView_URL_activity", url);
            if (eduURL.contains(url)) {
                // This is my website, so do not override; let my WebView load the page
                Toast.makeText(view.getContext(), "starting point", Toast.LENGTH_LONG).show();
            } else if (targetURL.contains(url)) {
                Toast.makeText(view.getContext(), "reached target", Toast.LENGTH_LONG).show();
                view.setOnTouchListener((v, event) -> true);        //disable clicking on webview
            }
            return false;
        }

        public void onPageFinished(WebView view, String url) {
            if (url.contains(targetURL)){
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
            reader.readHTML(html,auth_token,studentAdminRequest, activity);

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


