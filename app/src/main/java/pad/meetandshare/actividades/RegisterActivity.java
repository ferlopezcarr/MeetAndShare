package pad.meetandshare.actividades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import pad.meetandshare.R;

/**
 * Created by ferlo on 15/04/2018.
 */

public class RegisterActivity extends AppCompatActivity {

    WebView myWebView;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myWebView = (WebView)findViewById(R.id.register);
        myWebView.loadUrl("file:///android_asset/register.html");
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
    }
}
