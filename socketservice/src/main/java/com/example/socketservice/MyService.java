package com.example.socketservice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyService extends Thread {
  public static final String SOCKET_TAG = "socketTag";

  public void run() {
    Socket socket = null;   //侦听并接受到此套接字的连接,返回一个Socket对象
    try {
      final ServerSocket serverSocket = new ServerSocket(8888);
      LogMgr.d(SOCKET_TAG, "服务端启动");

      while (true) {
        socket = serverSocket.accept();
        LogMgr.d(SOCKET_TAG, "已接收到客户端连接");
        CommandDealThread commandDealThread = new CommandDealThread(socket);
        commandDealThread.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
