package com.electricimp.blinkup.sample.direct;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.electricimp.blinkup.StaticIp;

import java.util.EnumSet;
import java.util.HashMap;

public class StaticIpForm extends LinearLayout {
  private final TextView ipAddressView;
  private final TextView netmaskView;
  private final TextView gatewayView;
  private final TextView dns1View;
  private final TextView dns2View;

  private final HashMap<StaticIp.Error, TextView> staticErrorMap = new HashMap<>();

  public StaticIpForm(Context context) {
    this(context, null);
  }

  public StaticIpForm(Context context, AttributeSet attrs) {
    super(context, attrs);

    LayoutInflater.from(context).inflate(R.layout.static_ip_form, this);
    ipAddressView = (TextView) findViewById(R.id.ip_address);
    netmaskView = (TextView) findViewById(R.id.netmask);
    gatewayView = (TextView) findViewById(R.id.gateway);
    dns1View = (TextView) findViewById(R.id.dns1);
    dns2View = (TextView) findViewById(R.id.dns2);

    setOrientation(VERTICAL);

    staticErrorMap.put(StaticIp.Error.INVALID_IP_ADDRESS, ipAddressView);
    staticErrorMap.put(StaticIp.Error.INVALID_NETMASK, netmaskView);
    staticErrorMap.put(StaticIp.Error.INVALID_GATEWAY, gatewayView);
    staticErrorMap.put(StaticIp.Error.INVALID_DNS1, dns1View);
    staticErrorMap.put(StaticIp.Error.INVALID_DNS2, dns2View);
  }
  
  public StaticIp getStaticIp() {
    boolean valid = true;

    StaticIp staticIp = new StaticIp();
    staticIp.ipAddress = ipAddressView.getText().toString().trim();
    staticIp.netmask = netmaskView.getText().toString().trim();
    staticIp.gateway = gatewayView.getText().toString().trim();
    staticIp.dns1 = dns1View.getText().toString().trim();
    staticIp.dns2 = dns2View.getText().toString().trim();

    EnumSet<StaticIp.Error> errors = staticIp.getErrors();
    if (!errors.isEmpty()) {
      for (StaticIp.Error error : errors) {
        TextView textView = staticErrorMap.get(error);
        textView.setError(getContext().getString(TextUtils.isEmpty(textView.getText()) ?
            R.string.missing : R.string.invalid_address));
      }
      valid = false;
    }

    return valid ? staticIp : null;
  }
}
