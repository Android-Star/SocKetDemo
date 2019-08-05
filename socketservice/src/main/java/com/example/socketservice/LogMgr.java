package com.example.socketservice;

import android.util.Log;

public class LogMgr {

  private final static int SETTING_LEVEL = 1;

  private final static int ERROR = 1;
  private final static int WARN = 2;
  private final static int INFO = 3;
  private final static int DEBUG = 4;
  private final static int VERBOSE = 5;

  private LogMgr() {
  }

  public static void v(String tag, String message) {
    if (getLevel() <= VERBOSE) {
      Log.v(tag, message + ",thread:" + Thread.currentThread());
    }
  }

  public static void d(String tag, String format, Object... args) {
    if (getLevel() <= DEBUG) {
      Log.d(tag, String.format(format, args) + ",thread:" + Thread.currentThread());
    }
  }

  public static void d(String tag, String message) {
    if (getLevel() <= DEBUG) {
      Log.d(tag, message + ",thread:" + Thread.currentThread());
    }
  }

  public static void i(String tag, String message) {
    if (getLevel() <= INFO) {
      Log.i(tag, message + ",thread:" + Thread.currentThread());
    }
  }

  public static void w(String tag, String message) {
    if (getLevel() <= WARN) {
      Log.w(tag, message + ",thread:" + Thread.currentThread());
    }
  }

  public static void e(String tag, String message) {
    if (getLevel() <= ERROR) {
      Log.e(tag, message + ",thread:" + Thread.currentThread());
    }
  }

  public static void e(String tag, String format, Object... args) {
    if (getLevel() <= ERROR) {
      Log.e(tag, String.format(format, args) + ",thread:" + Thread.currentThread());
    }
  }

  private static int getLevel() {
    return SETTING_LEVEL;
  }
}
