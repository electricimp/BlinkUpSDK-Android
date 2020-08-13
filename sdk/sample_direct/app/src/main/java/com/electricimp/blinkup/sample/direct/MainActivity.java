package com.electricimp.blinkup.sample.direct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.electricimp.blinkup.BlinkupController;
import com.electricimp.blinkup.ServerErrorHandler;

public class MainActivity extends AppCompatActivity {
    static final String API_KEY = YOUR_API_KEY;

    private BlinkupController blinkup;
    private ServerErrorHandler errorHandler = new ServerErrorHandler() {
        @Override
        public void onError(String errorMsg) {
            Toast.makeText(
                    MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        blinkup = BlinkupController.getInstance();
        blinkup.intentBlinkupComplete = new Intent(
                this, BlinkupCompleteActivity.class);
        blinkup.intentClearComplete = new Intent(
                this, ClearCompleteActivity.class);
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        blinkup.handleActivityResult(this, requestCode, resultCode, data);
    }

    public void defaultNetwork(View v) {
        blinkup.selectWifiAndSetupDevice(this, API_KEY, errorHandler);
    }

    public void customSsid(View v) {
        Intent intent = new Intent(this, TypeSsidActivity.class);
        startActivity(intent);
    }

    public void customWps(View v) {
        Intent intent = new Intent(this, TypeWpsActivity.class);
        startActivity(intent);
    }

    public void ethernet(View v) {
        Intent intent = new Intent(this, EthernetActivity.class);
        startActivity(intent);
    }

    public void clearSavedData(View v) {
        blinkup.clearSavedData(this);
        Toast.makeText(this, R.string.clear_done, Toast.LENGTH_SHORT).show();
    }
}