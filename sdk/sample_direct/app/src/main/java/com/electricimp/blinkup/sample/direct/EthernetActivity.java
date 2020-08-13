package com.electricimp.blinkup.sample.direct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.electricimp.blinkup.BlinkupController;
import com.electricimp.blinkup.Proxy;
import com.electricimp.blinkup.ServerErrorHandler;
import com.electricimp.blinkup.StaticIp;

public class EthernetActivity extends AppCompatActivity {
    private BlinkupController blinkup;
    private ServerErrorHandler errorHandler = new ServerErrorHandler() {
        @Override
        public void onError(String errorMsg) {
            Toast.makeText(
                EthernetActivity.this, errorMsg, Toast.LENGTH_SHORT)
                .show();
            blinkupButton.setEnabled(true);
        }
    };

    private CompoundButton useProxySwitch;
    private ProxyForm proxyForm;

    private CompoundButton useStaticIpSwitch;
    private StaticIpForm staticIpForm;

    private View blinkupButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ethernet);

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

        blinkup = BlinkupController.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        blinkupButton.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        blinkup.handleActivityResult(this, requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }

    public void sendBlinkup(View v) {
        boolean valid = true;

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
            this, MainActivity.API_KEY, proxy, staticIp, errorHandler);
    }
}