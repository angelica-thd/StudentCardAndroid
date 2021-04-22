package com.example.qrcodetry1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class WebViewFrag extends Fragment {

    private StudentAdminRequest studentAdminRequest;
    private WebView webView;
    private String htmlSource;
    private Context activity;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_web_viwe, container, false);
        activity = view.getContext();
        WebView webView = view.findViewById(R.id.webView);

        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();

        webView.setWebViewClient(new MyWebViewClient());
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new LoadListener(), "HTMLOUT");

        String eduURL = "https://submit-academicid.minedu.gov.gr";
        String dummyURL = "https://www.google.com";
        String targetURL = "https://submit-academicid.minedu.gov.gr/Secure/Students/Default.aspx";
        String dummyTarget = "https://https://www.w3schools.com/html/html_scripts.asp";

        webView.clearCache(true);
        webView.loadUrl(eduURL);      //replace occasionally with dummyURL to avoid too many pings on the edu URL

       /* HtmlReader reader = new HtmlReader();
        String htmlFile = reader.readFromFile(activity);
        try {
            reader.readHTML(htmlFile,activity);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        if(webView.getUrl().contains(eduURL)){
            Log.i("herer","here");
           Log.i("scrollX: ", String.valueOf(webView.getScrollX()));
           Log.i("scrollY: ", String.valueOf(webView.getScrollY()));
        }


        // Inflate the layout for this fragment
        return view;

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
            //Log.i("htmlString", htmlSource);
            try{
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(activity.openFileOutput("config.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(html);
                outputStreamWriter.close();
            }catch (IOException e) {
                 Log.e("Exception", "File write failed: " + e.toString());
           }
            HtmlReader reader = new HtmlReader();
            String htmlFile = reader.readFromFile(activity);
            //reader.readHTML(htmlFile,activity);
        }
    }
}





