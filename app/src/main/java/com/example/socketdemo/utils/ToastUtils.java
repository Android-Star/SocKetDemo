package com.example.socketdemo.utils;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

  private static Toast toast;

  private ToastUtils() {

  }

  private static void prepare() {
    if (!isInUiThread()) {
      Looper.prepare();
    }
  }

  private static void loop() {
    if (!isInUiThread()) {
      Looper.loop();
    }
  }

  public static void showLongToast(Context context, String text) {
    prepare();
    if (context == null) {
      context = ContextUtils.getContext();
    }
    if (toast != null) {
      toast.cancel();
      toast = null;
    }
    toast = new Toast(context);
    toast.setGravity(Gravity.BOTTOM, 0, 0);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.show();
    loop();
  }

  public static void showLongToast(Context context, int resId) {
    showLongToast(context, context.getResources().getString(resId));
  }

  public static void showLongToast(int resId) {
    showLongToast(ContextUtils.getContext(),
        ContextUtils.getContext().getResources().getString(resId));
  }

  public static void showLongToast(String text) {
    showLongToast(ContextUtils.getContext(), text);
  }

  public static void cancelToast() {
    prepare();
    try {
      if (toast != null) {
        toast.cancel();
        toast = null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    loop();
  }

  private static boolean isInUiThread() {
    return Looper.myLooper() == Looper.getMainLooper();
  }
}
