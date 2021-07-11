package com.example.qrcodetry1;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
    private String htmlSource, auth_token;
    private int pressed, count;
    private String weburl, userAgent, eduURL;
    private ProgressBar progressBar;
    private ConstraintLayout layout;


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

        eduURL = "https://submit-academicid.minedu.gov.gr";

        pressed = 0;
        count = 0;
        layout = findViewById(R.id.mainLayout);
        layout.setAlpha((float) 0.8);
        progressBar.setVisibility(View.VISIBLE);
        webView.setVisibility(View.INVISIBLE);
        webView.loadUrl(eduURL);

    }


    private class MyWebViewClient extends WebViewClient {
        String eduURL = "https://submit-academicid.minedu.gov.gr";
        String targetURL = "https://submit-academicid.minedu.gov.gr/Secure/Students/Default.aspx";

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            weburl = url;

            Log.i("webView_URL_activity", url);
            if (targetURL.contains(url)) {
                int progress = Math.round(count / 2);
                if (progress < 1) {
                    progressBar.setProgress(progress);
                    layout.setAlpha((float) 0.8);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setAlpha(1);
                } else if (url.contains("https://wayf.grnet.gr/?entityID=")) {
                    int progress_2 = Math.round(count / 1);
                    if (progress_2 < 1) {
                        progressBar.setProgress(progress);
                        layout.setAlpha((float) 0.8);
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setAlpha(1);
                    }
                }else {
                        layout.setAlpha(1);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    view.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        

        public void onPageFinished(WebView view, String url) {
            if (url.contains(eduURL)) {
                count += 1;
                if(count == 1) view.loadUrl("javascript:(function(){bcs_btn = document.getElementsByClassName('squarebutton')[1].click();})();");

            }
            if (url.contains("https://wayf.grnet.gr/?entityID="))  view.setVisibility(View.VISIBLE);

                if (url.contains(targetURL)) {
                    pressed += 1;
                    count += 1;
                    view.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].outerHTML+'</html>');");
                }
            }
        }


        public class LoadListener {
            @JavascriptInterface
            public void processHTML(String html) throws IOException {
                htmlSource = html;
                HtmlReader reader = new HtmlReader();
                reader.readHTML(html, auth_token, StudentInfoReg.this, pressed);

            }
        }
    }


