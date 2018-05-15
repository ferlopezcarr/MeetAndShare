package pad.meetandshare.actividades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import pad.meetandshare.R;

public class InfoApplication extends AppCompatActivity {

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_application);

        myWebView = (WebView) findViewById(R.id.info_aplication);
        myWebView.loadUrl("file:///android_asset/quienesSomos.html");
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
    }
}