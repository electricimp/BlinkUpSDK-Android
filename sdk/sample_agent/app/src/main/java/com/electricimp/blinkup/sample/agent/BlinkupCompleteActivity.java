package com.electricimp.blinkup.sample.agent;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.electricimp.blinkup.BlinkupController;
import com.electricimp.blinkup.TokenStatusCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BlinkupCompleteActivity extends Activity {
    private BlinkupController blinkup;

    private View progressBar;
    private TextView status;

    private SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

    private TokenStatusCallback callback = new TokenStatusCallback() {
        @Override
        public void onSuccess(JSONObject json) {
            SharedPreferences pref
                = PreferenceManager.getDefaultSharedPreferences(
                    BlinkupCompleteActivity.this);
            SharedPreferences.Editor editor = pref.edit();

            try {
                String dateStr = json.getString("claimed_at");
                dateStr = dateStr.replace("Z", "+0:00");
                Date claimedAt = dateFormat.parse(dateStr);

                String planId = json.getString("plan_id");
                String agentUrl = json.getString("agent_url");

                String impeeId = json.getString("impee_id");
                if (impeeId != null) {
                    impeeId = impeeId.trim();
                }

                editor.putString("planId", planId);
                editor.putString("agentUrl", agentUrl);
                editor.putLong("claimedAt", claimedAt.getTime());
                editor.putString("impeeId", impeeId);
                editor.commit();

                finish();
            } catch (JSONException e) {
                onError(e.getMessage());
            } catch (ParseException e) {
                onError(e.getMessage());
            }
        }

        public void onError(String errorMsg) {
            progressBar.setVisibility(View.GONE);
            status.setText(errorMsg);
        }

        @Override
        public void onTimeout() {
            progressBar.setVisibility(View.GONE);
            status.setText(R.string.timeout);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blinkup_complete);

        progressBar = findViewById(R.id.progress_bar);
        status = (TextView) findViewById(R.id.status);

        blinkup = BlinkupController.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        blinkup.getTokenStatus(callback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        blinkup.cancelTokenStatusPolling();
    }
}