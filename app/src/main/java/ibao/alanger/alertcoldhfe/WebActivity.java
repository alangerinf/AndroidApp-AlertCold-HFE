package ibao.alanger.alertcoldhfe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class WebActivity extends AppCompatActivity {

    private WebView webview;
    private boolean isConnected = false;
    //private static final String basePath = "http://alertfarm.ibao.pe/";

    //private static final String dominio = "alertfarm.ibao.pe";
    //private static final String dominio = "alertfarmsf.ibao.pe";
    private static final String dominio = "alertcoldhortifrut.ibao.pe";

    private static final String basePath = "http://"+dominio+"/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildToolbar();
        setContentView(R.layout.activityweb);
        webview = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new MyWebViewClient());
        checkConexion();
        long time = new Date().getTime();
        String imei = getIMEI();
        String hash = imei+ "|" +time;
        RandomString rs = new RandomString(20);
        String url = basePath + "?a="+rs.nextString()+"&b="+imei.hashCode()+
                "&c="+hash.hashCode()+"&d="+time+rs.nextString()+"&e="+imei+rs.nextString()+
                "&f="+rs.nextString()+"&g="+rs.nextString()+"&h="+rs.nextString()+"&i="+imei;
        Log.e("URL","URL: "+url);
        webview.loadUrl(url);
    }

    private void buildToolbar(){
        try {
            ActionBar toobar = getSupportActionBar();
            //toobar.setTitle(toobar.getTitle() +  " ("+dominio+") ");
            toobar.setSubtitle(dominio);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private String getIMEI(){
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
            return imei;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "123456789012345";
    }

    private void checkConexion() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
                if (ni.getState() == NetworkInfo.State.CONNECTED) {
                    // record the fact that there is not connection
                    isConnected = true;
                    return;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        isConnected = false;
    }

    @Override
    public void onBackPressed() {
        // Check if there's history
        if (this.webview.canGoBack())
            this.webview.goBack();
        else
            super.onBackPressed();
    }

    private class MyWebViewClient extends WebViewClient {

        private long loadTime; // Web page loading time

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!isConnected) {
                String offlineMessageHtml = "No se puede conectar a la red.";
                view.loadData(offlineMessageHtml, "text/html", "utf-8");
                checkConexion();
                return true;
            }
            //if (Uri.parse(url).getHost().equals("alertfarm.ibao.pe")) {
            if (Uri.parse(url).getHost().equals(dominio)) {
                // This is my web site, so do not override; let my WebView load
                // the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch
            // another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

    }

}
