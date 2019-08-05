package com.example.socketservice;

public enum Command {
  COMMAND_WAKE_UP("wakeUp", 1), COMMAND_FACE_FOLLOW("faceFollow", 2), COMMAND_SELF("turnRight",
      3), COMMAND_RELAX_TURN("disConnect", 4);

  private String name;
  private int type;

  Command(String name, int type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
