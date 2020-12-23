package nanni.star.radiotv.Activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import nanni.star.radiotv.R;

public class NewsActivity extends AppCompatActivity {

    private WebView webView;
    private static final String URL =  "https://www.facebook.com/nannistarRadio/";
    private Button backButton;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
            try{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Check Internet");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            initView();
            webView.loadUrl(URL);
            setNewsView();
            initListeners();
        }

    }

   public void initView(){
       webView = findViewById(R.id.webview);
       webView.getSettings().setBuiltInZoomControls(true);
       webView.setBackgroundColor(0);
       backButton = findViewById(R.id.newsBackButton);
    }

   public void initListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

   public void setNewsView(){

        progressDialog = new ProgressDialog(this);
       progressDialog.setMessage("Loading Please wait...");
       progressDialog.setCancelable(false);
       progressDialog.show();

       webView.getSettings().setJavaScriptEnabled(true);
       webView.setWebViewClient(new WebViewClient() {
           public void onPageFinished(WebView view, String url){
               try{
                   progressDialog.dismiss();
               }catch (Exception e){
                   e.printStackTrace();
               }
           }
       });

   }


}
