package com.example.socketservice;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
  private TextView tvIp;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tvIp = findViewById(R.id.tvIp);
    initSocket();
  }

  @Override protected void onResume() {
    super.onResume();
    setIp();
  }

  private void setIp() {
    //获取wifi服务
    WifiManager wifiManager =
        (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    //判断wifi是否开启
    if (!wifiManager.isWifiEnabled()) {
      wifiManager.setWifiEnabled(true);
    }
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    int ipAddress = wifiInfo.getIpAddress();
    String ip = intToIp(ipAddress);
    tvIp.setText("IP地址：" + ip);
  }

  private String intToIp(int i) {
    return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
  }

  private void initSocket() {
    new MyService().start();
  }
}
