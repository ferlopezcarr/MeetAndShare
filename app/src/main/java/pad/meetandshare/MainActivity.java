package pad.meetandshare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    WebView myWebView;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebView = (WebView)findViewById(R.id.login);
        myWebView.loadUrl("file:///android_asset/login.html");
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);

    }
}
