package com.ghostx.ultimate;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.webkit.*;
import androidx.appcompat.app.AppCompatActivity;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        WebView wv = findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(new WebAppInterface(this), "Android");

        new Thread(() -> {
            Python py = Python.getInstance();
            PyObject server = py.getModule("ghost_server");
            server.callAttr("start_server");
        }).start();

        new Handler().postDelayed(() -> {
            wv.loadUrl("http://localhost:5000");
        }, 3000);
    }

    public class WebAppInterface {
        Activity mActivity;
        WebAppInterface(Activity activity) { mActivity = activity; }

        @android.webkit.JavascriptInterface
        public void runCommand(String cmd) {
            new Thread(() -> {
                try {
                    Process p = Runtime.getRuntime().exec(cmd);
                    p.waitFor();
                } catch (Exception e) { }
            }).start();
        }
    }
          }
