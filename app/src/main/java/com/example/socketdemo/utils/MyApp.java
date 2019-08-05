package com.example.socketdemo.utils;

import android.app.Application;

public class MyApp extends Application {

  @Override public void onCreate() {
    super.onCreate();
    ContextUtils.setContext(this);
  }
}
