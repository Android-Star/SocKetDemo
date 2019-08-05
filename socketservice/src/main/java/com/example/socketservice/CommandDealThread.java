package com.example.socketservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class CommandDealThread extends Thread {
  public static final String SOCKET_TAG = "socketTag";
  private Socket socket;

  private InputStream inputStream;
  private InputStreamReader inputStreamReader;
  private BufferedReader bufferedReader;

  private PrintWriter printWriter;
  private OutputStream outputStream;

  public CommandDealThread(Socket socket) {
    this.socket = socket;
  }

  @Override public void run() {
    inputStream = null;
    try {
      inputStream = socket.getInputStream();
      inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
      bufferedReader = new BufferedReader(inputStreamReader);
      String line = null;
      while ((line = bufferedReader.readLine()) != null) {
        LogMgr.d(SOCKET_TAG,
            "设备收到命令：" + line + ",当前客户端ip为：" + socket.getInetAddress().getHostAddress());
        dealCommand(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void dealCommand(String line) {
    String[] split = line.split("#");
    if (split != null) {
      int type = Integer.parseInt(split[0]);
      if (Command.COMMAND_WAKE_UP.getType() == type) {
        if (split.length == 5) {
          int wakeOrientation = Integer.parseInt(split[4]);
          DealCommandUtils.getInstance()
              .turnAndWakeUp(wakeOrientation, Integer.parseInt(split[2]),
                  Integer.parseInt(split[3]));
        }
      } else if (Command.COMMAND_FACE_FOLLOW.getType() == type) {
        if (split.length == 9) {
          int orientation1 = Integer.parseInt(split[4]);
          int orientation2 = Integer.parseInt(split[8]);
          DealCommandUtils.getInstance()
              .followFace(orientation1, Integer.parseInt(split[2]), Integer.parseInt(split[3]),
                  Integer.parseInt(split[5]), orientation2, Integer.parseInt(split[6]),
                  Integer.parseInt(split[7]));
        }
      } else if (Command.COMMAND_SELF.getType() == type) {
        DealCommandUtils.getInstance().playTTS();
      } else if (Command.COMMAND_RELAX_TURN.getType() == type) {
        if (split.length == 9) {
          int orientation1 = Integer.parseInt(split[4]);
          int orientation2 = Integer.parseInt(split[8]);
          DealCommandUtils.getInstance()
              .relaxTurn(orientation1, Integer.parseInt(split[2]), Integer.parseInt(split[3]),
                  Integer.parseInt(split[5]), orientation2, Integer.parseInt(split[6]),
                  Integer.parseInt(split[7]));
        }
      }
    }
  }

  private String getStr(String sample) {
    String[] split = sample.split("#");
    if (split != null) {
      if (split.length == 1) {
        return split[0];
      } else {
        return split[split.length - 1];
      }
    } else {
      return "";
    }
  }

  public void sendMsg(final String msgStr) {
    new Thread(new Runnable() {
      @Override public void run() {
        outputStream = null; //获取一个输出流，向服务端发送信息
        try {
          outputStream = socket.getOutputStream();
          printWriter = new PrintWriter(outputStream);//将输出流包装成打印流
          printWriter.print(msgStr);
          printWriter.flush();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  public void close() {
    //关闭相对应的资源
    try {
      socket.shutdownInput();//关闭输入流
      socket.shutdownOutput();//关闭输出流
      printWriter.close();
      outputStream.close();
      bufferedReader.close();
      inputStream.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
