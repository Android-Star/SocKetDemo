package com.example.socketdemo;

import java.io.Serializable;
import java.util.List;

public class Bean implements Serializable {
  private List<ItemBean> list;

  public List<ItemBean> getList() {
    return list;
  }

  public void setList(List<ItemBean> list) {
    this.list = list;
  }

  static class ItemBean {
    private String title;
    private int type;  //1唤醒2人脸跟随3介绍自己4随意转动
    private List<CommandBean> commandBeans;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public int getType() {
      return type;
    }

    public void setType(int type) {
      this.type = type;
    }

    public List<CommandBean> getCommandBeans() {
      return commandBeans;
    }

    public void setCommandBeans(List<CommandBean> commandBeans) {
      this.commandBeans = commandBeans;
    }
  }

  static class CommandBean {
    private long waitTime;
    private int orientation;//1左2右
    private int speed;
    private int angle;

    public long getWaitTime() {
      return waitTime;
    }

    public void setWaitTime(long waitTime) {
      this.waitTime = waitTime;
    }

    public int getOrientation() {
      return orientation;
    }

    public void setOrientation(int orientation) {
      this.orientation = orientation;
    }

    public int getSpeed() {
      return speed;
    }

    public void setSpeed(int speed) {
      this.speed = speed;
    }

    public int getAngle() {
      return angle;
    }

    public void setAngle(int angle) {
      this.angle = angle;
    }
  }
}
