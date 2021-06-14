package com.example.qrcodetry1;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.IOException;

public class StudentInfoReg extends AppCompatActivity {
    private StudentAdminRequest studentAdminRequest;
    private WebView webView;
    private String htmlSource,auth_token;
    private int pressed, count;
    private String weburl,userAgent;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info_reg);
        studentAdminRequest = new StudentAdminRequest(this);
        WebView webView = findViewById(R.id.webView2);
        progressBar = findViewById(R.id.progress_view);
        progressBar.setVisibility(View.INVISIBLE);
        webView.setVisibility(View.VISIBLE);
        auth_token = getIntent().getStringExtra("auth_token");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("prev_user_auth_token",auth_token);

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

        pressed = 0;
        count = 0;
        webView.scrollTo((int) (1.5*webView.getWidth())+5, (int) (webView.getHeight()/2.6));
        webView.loadUrl(eduURL);      //replace occasionally with dummyURL to avoid too many pings on the edu URL

    }



    private class MyWebViewClient extends WebViewClient {
        String eduURL = "https://submit-academicid.minedu.gov.gr";
        String targetURL = "https://submit-academicid.minedu.gov.gr/Secure/Students/Default.aspx";

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            weburl = url;
            ConstraintLayout layout = findViewById(R.id.mainLayout);
            Log.i("webView_URL_activity", url);
             if (targetURL.contains(url)) {
                int progress =  Math.round(count/2);
                if(progress<1){
                    progressBar.setProgress(progress);
                    layout.setAlpha((float) 0.8);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setAlpha(1);
                }
                else {
                    layout.setAlpha(1);
                    progressBar.setVisibility(View.INVISIBLE);
                }

                view.setVisibility(View.INVISIBLE);
                view.setOnTouchListener((v, event) -> true);        //disable clicking on webview
            }
            return false;
        }

        public void onPageFinished(WebView view, String url) {
            if (url.contains(eduURL)){
                view.scrollTo((int) (1.5*view.getWidth())+5, (int) (view.getHeight()/2.6));
                count+=1;
            }

            if (url.contains(targetURL)) {
                pressed+=1;
                count+=1;
                view.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].outerHTML+'</html>');");
            }
        }
    }


    public class LoadListener {
        @JavascriptInterface
        public void processHTML(String html) throws IOException {
            htmlSource = html;
            HtmlReader reader = new HtmlReader();
            reader.readHTML(html,auth_token,StudentInfoReg.this,pressed);

        }
    }
}


