package com.electricimp.blinkup.sample.strings;

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

        // Custom strings
        blinkup.stringIdChooseWiFiNetwork = getString(
                R.string.custom_choose_wifi_network);
        blinkup.stringIdWpsInfo = getString(R.string.custom_wps_info);
        blinkup.stringIdClearDeviceSettings = getString(
                R.string.custom_clear_device_settings);
        blinkup.stringIdClearWireless = getString(
                R.string.custom_clear_wireless);
        blinkup.stringIdSendBlinkUp = getString(R.string.custom_send_blinkup);
        blinkup.stringIdBlinkUpDesc = getString(R.string.custom_blinkup_desc);
        blinkup.stringIdLegacyMode = getString(R.string.custom_legacy_mode);
        blinkup.stringIdLegacyModeDesc = getString(
                R.string.custom_legacy_mode_desc);
        blinkup.stringIdOk = getString(R.string.custom_ok);
        blinkup.stringIdSsidHint = getString(R.string.custom_ssid_hint);
        blinkup.stringIdPasswordHint = getString(R.string.custom_password_hint);
        blinkup.stringIdRememberPassword = getString(
                R.string.custom_remember_password);
        blinkup.stringIdWpsPinHint = getString(R.string.custom_wps_pin_hint);
        blinkup.stringIdChangeNetwork = getString(
                R.string.custom_change_network);
        blinkup.stringIdConnectUsingWps = getString(
                R.string.custom_connect_using_wps);
        blinkup.stringIdCountdownDesc = getString(
                R.string.custom_countdown_desc);
        blinkup.stringIdLowFrameRateTitle = getString(
                R.string.custom_low_frame_rate_title);
        blinkup.stringIdLowFrameRateDesc = getString(
                R.string.custom_low_frame_rate_desc);
        blinkup.stringIdLowFrameRateGoToSettings = getString(
                R.string.custom_low_frame_rate_go_to_settings);
        blinkup.stringIdLowFrameRateProceedAnyway = getString(
                R.string.custom_low_frame_rate_proceed_anyway);

        // Show legacy mode to show the customized strings
        blinkup.showLegacyMode = true;
    }

    public void configure(View v) {
        blinkup.selectWifiAndSetupDevice(this, API_KEY, errorHandler);
    }
}