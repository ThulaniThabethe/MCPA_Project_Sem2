package com.example.mcpa_project_sem22;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide the status bar to give the web content maximum screen space
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // Set the content view to the layout we just created containing only the WebView
        setContentView(R.layout.activity_main_webview);

        webView = findViewById(R.id.main_webview);
        
        // Enable JavaScript, which is essential for your HTML/CSS/JS application
        webView.getSettings().setJavaScriptEnabled(true);
        
        // This setting forces all links/navigation to stay inside the app (in the WebView)
        webView.setWebViewClient(new WebViewClient());
        
        // Load your Home Page (index.html) from the 'assets' folder
        // The path 'file:///android_asset/' is the standard way to access the assets directory
        webView.loadUrl("file:///android_asset/index.html");
    }

    /**
     * Handles the Android back button press.
     * If the WebView has history (i.e., you navigated away from index.html), 
     * it goes back in web history instead of closing the app.
     */
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // If there's no web history, use the default back behavior (usually closing the app/activity)
            super.onBackPressed();
        }
    }
}