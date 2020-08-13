package com.electricimp.blinkup.sample.countdown;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.electricimp.blinkup.BlinkupController;
import com.electricimp.blinkup.ServerErrorHandler;

public class MainActivity extends Activity {
    private static final String API_KEY = YOUR_API_KEY;

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

        // Start count down from 5
        blinkup.countdownSeconds = 5;
    }

    public void configure(View v) {
        blinkup.selectWifiAndSetupDevice(this, API_KEY, errorHandler);
    }
}