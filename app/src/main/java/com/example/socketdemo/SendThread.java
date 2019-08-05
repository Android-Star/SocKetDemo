package com.example.socketdemo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SendThread extends Thread {
  private Socket socket;

  private OutputStream outputStream;
  private PrintWriter printWriter;

  private volatile boolean sendMsg;
  private volatile String msg;

  public void sendMsg(String msg) {
    this.msg = msg;
    this.sendMsg = true;
  }

  public SendThread(Socket socket) {
    this.socket = socket;
  }

  @Override public void run() {
    outputStream = null;//获取一个输出流，向服务端发送信息
    try {
      outputStream = socket.getOutputStream();
      printWriter = new PrintWriter(outputStream);//将输出流包装成打印流

      while (true) {
        if (printWriter != null && sendMsg) {
          sendMsg = false;
          printWriter.println(msg);
          printWriter.flush();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void close() {
    try {
      socket.shutdownOutput();//关闭输出流
      socket.shutdownInput();
      //关闭相对应的资源
      printWriter.close();
      outputStream.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
