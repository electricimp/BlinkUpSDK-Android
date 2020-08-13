package com.electricimp.blinkup.sample.agent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.electricimp.blinkup.BlinkupController;
import com.electricimp.blinkup.ServerErrorHandler;

import java.util.Date;

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

    private TextView planIdView;
    private TextView agentUrlView;
    private TextView impeeIdView;
    private TextView claimedAtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        planIdView = (TextView) findViewById(R.id.plan_id);
        agentUrlView = (TextView) findViewById(R.id.agent_url);
        impeeIdView = (TextView) findViewById(R.id.impee_id);
        claimedAtView = (TextView) findViewById(R.id.claimed_at);

        blinkup = BlinkupController.getInstance();
        blinkup.intentBlinkupComplete = new Intent(
                this, BlinkupCompleteActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFields();
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        blinkup.handleActivityResult(this, requestCode, resultCode, data);
    }

    public void configure(View v) {
        blinkup.selectWifiAndSetupDevice(this, API_KEY, errorHandler);
    }

    public void reset(View v) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(
                this);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        updateFields();
    }

    private void updateFields() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(
                this);

        String planId = pref.getString("planId", null);
        if (planId == null) {
            planIdView.setText(R.string.unknown);
        } else {
            planIdView.setText(planId);
        }
        
        String agentURL = pref.getString("agentUrl", null);
        if (agentURL == null) {
            agentUrlView.setText(R.string.unknown);
        } else {
            agentUrlView.setText(agentURL);
        }

        String impeeId = pref.getString("impeeId", null);
        if (impeeId == null) {
            impeeIdView.setText(R.string.unknown);
        } else {
            impeeIdView.setText(impeeId);
        }

        long claimedAt = pref.getLong("claimedAt", -1);
        if (claimedAt < 0) {
            claimedAtView.setText(R.string.unknown);
        } else {
            claimedAtView.setText(new Date(claimedAt).toString());
        }
    }
}