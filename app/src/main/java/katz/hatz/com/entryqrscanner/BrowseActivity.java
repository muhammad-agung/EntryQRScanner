package katz.hatz.com.entryqrscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.prefs.Preferences;

public class BrowseActivity extends AppCompatActivity {

    WebView wvMyPage;
    Button btnScan;
    TextView url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        wvMyPage = findViewById(R.id.webViewMyPage);
        btnScan = findViewById(R.id.btnScan);
        url = findViewById(R.id.URL);



        wvMyPage.setWebViewClient(new WebViewClient());
        wvMyPage.getSettings().setJavaScriptEnabled(true);
        wvMyPage.getSettings().setAllowFileAccess(false);

        Intent intentReceived = getIntent();
        String value = intentReceived.getStringExtra("Value");
        wvMyPage.loadUrl(value);
        url.setText(value);


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

    }

    private void scanCode() {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null){
            if(result.getContents() != null){
                String outcome = result.getContents();
                Intent intent = new Intent(BrowseActivity.this,BrowseActivity.class);
                intent.putExtra("Value",outcome);
                startActivity(intent);

            }else {
                Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.wvMyPage.canGoBack()) {
            this.wvMyPage.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    //    @Override
//    protected void onPause() {
//        super.onPause();
//
//        String webUrl = wvMyPage.getUrl();
//
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(BrowseActivity.this);
//
//        SharedPreferences.Editor prefEdit = prefs.edit();
//
//        prefEdit.putString("CurrentURL", webUrl);
//        prefEdit.commit();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//
//        String current = prefs.getString("CurrentURL", "No Value!");
//        wvMyPage.loadUrl(current);
//    }
}
