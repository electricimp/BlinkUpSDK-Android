package com.electricimp.blinkup.sample.token;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.electricimp.blinkup.BlinkupController;
import com.electricimp.blinkup.ServerErrorHandler;
import com.electricimp.blinkup.TokenAcquireCallback;

public class MainActivity extends Activity {
    private static final String API_KEY = YOUR_API_KEY;
    private static final int REQUEST_CODE = 10000;

    private ViewGroup container;
    private View tokenLabelView;
    private TextView tokenValueView;
    private View acquireButton;

    private BlinkupController blinkup;
    private ServerErrorHandler errorHandler = new ServerErrorHandler() {
        @Override
        public void onError(String errorMsg) {
            handleError(errorMsg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        container = (ViewGroup) findViewById(R.id.container);
        tokenLabelView = findViewById(R.id.token_label);
        tokenValueView = (TextView) findViewById(R.id.token);
        acquireButton = findViewById(R.id.acquire_button);

        blinkup = BlinkupController.getInstance();
    }

    public void configure(View v) {
        blinkup.selectWifi(this, API_KEY, REQUEST_CODE, errorHandler);
    }

    public void acquireSetupToken(View v) {
        acquireButton.setEnabled(false);
        blinkup.acquireSetupToken(this, API_KEY, new TokenAcquireCallback() {
            @Override
            public void onSuccess(String planID, String token) {
                tokenLabelView.setVisibility(View.VISIBLE);
                tokenValueView.setVisibility(View.VISIBLE);
                tokenValueView.setText(token);
                acquireButton.setEnabled(true);
            }
            @Override
            public void onError(String errorMsg) {
                handleError(errorMsg);
                acquireButton.setEnabled(true);
            }
        });
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE) {
            return;
        }
        if (resultCode != Activity.RESULT_OK) {
            // Error or cancel
            for (int i = 0; i < container.getChildCount(); ++i) {
                View child = container.getChildAt(i);
                child.setVisibility(
                        (child instanceof Button) ? View.VISIBLE : View.GONE);
            }

            // Error
            if (data != null) {
                String errorMsg = data.getStringExtra(
                        BlinkupController.FIELD_ERROR);
                if (!TextUtils.isEmpty(errorMsg)) {
                    Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            return;
        }

        if (data == null) {
            return;
        }

        Bundle extras = data.getExtras();
        if (extras == null) {
            return;
        }

        String[] keys  = {
                BlinkupController.FIELD_MODE,
                BlinkupController.FIELD_SSID,
                BlinkupController.FIELD_PASSWORD,
                BlinkupController.FIELD_PIN,
                BlinkupController.FIELD_TOKEN
        };

        for (int i = 0; i < keys.length; ++i) {
            View labelView = container.getChildAt(i*2);
            TextView valueView = (TextView) container.getChildAt(i*2+1);
            setValue(labelView, valueView, extras, keys[i]);
        }

        // After you transmitted the relevant fields to the Imp, call
        // blinkup.getTokenStatus(token, callback) to retrieve the agent url.
    }

    /**
     * Check if the Bundle contains the key.
     * If yes, set the value in valueView.
     * If no, hide both labelView and valueView.
     *
     * @param labelView
     * @param valueView
     * @param extras
     * @param key
     * @return The value for the key in the Bundle, or null if absent.
     */
    private String setValue(
            View labelView, TextView valueView, Bundle extras, String key) {
        if (!extras.containsKey(key)) {
            labelView.setVisibility(View.GONE);
            valueView.setVisibility(View.GONE);
            return null;
        }
        labelView.setVisibility(View.VISIBLE);
        valueView.setVisibility(View.VISIBLE);

        String value = extras.getString(key);
        if (value == null) {
            value = getString(R.string.value_null);
        } else {
            if (value.length() == 0) {
                value = getString(R.string.value_empty);
            }
        }

        valueView.setText(value);
        return value;
    }

    private void handleError(String errorMsg) {
        Toast.makeText(
                MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}