package com.electricimp.blinkup.sample.direct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.electricimp.blinkup.BlinkupController;
import com.electricimp.blinkup.Proxy;
import com.electricimp.blinkup.ServerErrorHandler;
import com.electricimp.blinkup.StaticIp;

public class TypeSsidActivity extends AppCompatActivity {
    private BlinkupController blinkup;
    private ServerErrorHandler errorHandler = new ServerErrorHandler() {
        @Override
        public void onError(String errorMsg) {
            Toast.makeText(
                    TypeSsidActivity.this, errorMsg, Toast.LENGTH_SHORT)
                    .show();
            blinkupButton.setEnabled(true);
        }
    };

    private EditText ssidView;
    private EditText passwordView;

    private CompoundButton useProxySwitch;
    private ProxyForm proxyForm;

    private CompoundButton useStaticIpSwitch;
    private StaticIpForm staticIpForm;

    private View blinkupButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_ssid);

        ssidView = (EditText) findViewById(R.id.ssid);
        passwordView = (EditText) findViewById(R.id.password);

        useProxySwitch = (CompoundButton) findViewById(R.id.use_proxy);
        proxyForm = (ProxyForm) findViewById(R.id.proxy_form);

        useStaticIpSwitch = (CompoundButton) findViewById(R.id.use_static_ip);
        staticIpForm = (StaticIpForm) findViewById(R.id.static_ip_form);

        blinkupButton = findViewById(R.id.blinkup_button);

        useProxySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                proxyForm.setVisibility(checked ? View.VISIBLE : View.GONE);
            }
        });
        useStaticIpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                staticIpForm.setVisibility(checked ? View.VISIBLE : View.GONE);
            }
        });

        blinkupButton = findViewById(R.id.blinkup_button);

        blinkup = BlinkupController.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        blinkupButton.setEnabled(true);
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        blinkup.handleActivityResult(this, requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }

    public void sendBlinkup(View v) {
        boolean valid = true;

        String ssid = ssidView.getText().toString().trim();
        if (ssid.length() == 0) {
            ssidView.setError(getString(R.string.ssid_empty_error));
            valid = false;
        }
        String password = passwordView.getText().toString();

        Proxy proxy = null;
        if (useProxySwitch.isChecked()) {
            proxy = proxyForm.getProxy();
            if (proxy == null) {
                valid = false;
            }
        }

        StaticIp staticIp = null;
        if (useStaticIpSwitch.isChecked()) {
            staticIp = staticIpForm.getStaticIp();
            if (staticIp == null) {
                valid = false;
            }
        }

        if (!valid) {
            return;
        }

        blinkupButton.setEnabled(false);

        blinkup.setupDevice(
            this, ssid, password, MainActivity.API_KEY, proxy, staticIp, errorHandler);
    }
}