package com.example.socketservice;

import android.os.Handler;
import android.os.Looper;

public class DealCommandUtils {
  private static final String TAG = "socketTag";

  private static DealCommandUtils instance = new DealCommandUtils();
  private Handler handler = new Handler(Looper.getMainLooper());

  public static DealCommandUtils getInstance() {
    return instance;
  }

  public void turnAndWakeUp(int orientation, int angle, int speed) {
  }

  public void followFace(final int orientation, final int angle, final int speed, int waitTime,
      final int orientationTwo, final int angleTwo, final int speedTwo) {
    LogMgr.d(TAG,
        "第一次转动方向：" + orientation + ",转动角度：" + angle + ",转动速度：" + speed + ",等待时间" + waitTime);
    if (angle <= 0 || speed <= 0) {
      return;
    }
    if (angleTwo <= 0 || speedTwo <= 0) {
      return;
    }
    handler.postDelayed(new Runnable() {
      @Override public void run() {
        LogMgr.d(TAG, "第二次转动方向：" + orientationTwo + ",转动角度：" + angleTwo + ",转动速度：" + speedTwo);
      }
    }, waitTime);
  }

  public void relaxTurn(final int orientation, final int angle, final int speed, int waitTime,
      final int orientationTwo, final int angleTwo, final int speedTwo) {
    LogMgr.d(TAG,
        "第一次转动方向：" + orientation + ",转动角度：" + angle + ",转动速度：" + speed + ",等待时间" + waitTime);
    if (angle <= 0 || speed <= 0) {
      return;
    }
    if (angleTwo <= 0 || speedTwo <= 0) {
      return;
    }
    handler.postDelayed(new Runnable() {
      @Override public void run() {
        LogMgr.d(TAG, "第二次转动方向：" + orientationTwo + ",转动角度：" + angleTwo + ",转动速度：" + speedTwo);
      }
    }, waitTime);
  }

  public void playTTS() {
  }
}
