package com.electricimp.blinkup.sample.direct;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.electricimp.blinkup.Proxy;

import java.util.EnumSet;

public class ProxyForm extends LinearLayout {
  private final TextView serverView;
  private final TextView portView;
  private final TextView usernameView;
  private final TextView passwordView;

  public ProxyForm(Context context) {
    this(context, null);
  }

  public ProxyForm(Context context, AttributeSet attrs) {
    super(context, attrs);

    LayoutInflater.from(context).inflate(R.layout.proxy_form, this);

    serverView = (TextView) findViewById(R.id.server);
    portView = (TextView) findViewById(R.id.port);
    usernameView = (TextView) findViewById(R.id.username);
    passwordView = (TextView) findViewById(R.id.password);

    setOrientation(VERTICAL);
  }
  
  public Proxy getProxy() {
    boolean valid = true;

    Proxy proxy = new Proxy();
    proxy.server = serverView.getText().toString();
    String port = portView.getText().toString().trim();
    try {
      proxy.port = Integer.parseInt(port);
    } catch (NumberFormatException e) {
      portView.setError(getContext().getString(R.string.invalid_port));
      valid = false;
    }
    proxy.username = usernameView.getText().toString();
    proxy.password = passwordView.getText().toString();

    EnumSet<Proxy.Error> errors = proxy.getErrors();
    if (!errors.isEmpty()) {
      for (Proxy.Error error : errors) {
        switch (error) {
          case INVALID_PORT:
            portView.setError(getContext().getString(R.string.invalid_port));
            break;
          case MISSING_SERVER:
            serverView.setError(getContext().getString(R.string.missing));
            break;
          case MISSING_USERNAME:
            usernameView.setError(getContext().getString(R.string.missing));
            break;
        }
      }
      valid = false;
    }

    if (TextUtils.isEmpty(port)) {
      portView.setError(getContext().getString(R.string.missing));
      valid = false;
    }

    return valid ? proxy : null;
  }
}
