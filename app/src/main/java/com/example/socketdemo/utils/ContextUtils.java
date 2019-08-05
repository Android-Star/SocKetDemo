package com.example.socketdemo.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import java.util.List;

public class ContextUtils {
  private static Context appContext;
  private static final String TAG = "ContextUtils";

  private ContextUtils() {

  }

  public static Context getContext() {
    return appContext;
  }

  public static Application getApplication() {
    return (Application) appContext;
  }

  public static void setContext(Context context) {
    ContextUtils.appContext = context.getApplicationContext();
  }

  public static String getMyProcessName() {
    int pid = android.os.Process.myPid();
    ActivityManager activityManager =
        (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
    if (activityManager != null) {
      List<ActivityManager.RunningAppProcessInfo> processInfoList =
          activityManager.getRunningAppProcesses();
      if (processInfoList != null && processInfoList.size() > 0) {
        for (ActivityManager.RunningAppProcessInfo appProcess : processInfoList) {
          if (appProcess.pid == pid) {
            return appProcess.processName;
          }
        }
      }
    }
    return "";
  }
}
