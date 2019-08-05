package com.example.socketdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveThread extends Thread {
  private Socket socket;

  private BufferedReader bufferedReader;
  private InputStreamReader inputStreamReader;
  private InputStream inputStream;

  public ReceiveThread(Socket socket) {
    this.socket = socket;
  }

  @Override public void run() {
    while (true) {
      inputStream = null; //获取一个输入流，接收服务端的信息
      try {
        inputStream = socket.getInputStream();
        inputStreamReader = new InputStreamReader(inputStream);//包装成字符流，提高效率
        bufferedReader = new BufferedReader(inputStreamReader);//缓冲区
        String info = "";
        String temp = null;//临时变量
        while ((temp = bufferedReader.readLine()) != null) {
          info += temp;
          System.out.println("客户端接收服务端发送信息：" + info);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void close() {

    try {
      socket.shutdownOutput();//关闭输出流
      socket.shutdownInput();
      //关闭相对应的资源
      bufferedReader.close();
      inputStream.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
