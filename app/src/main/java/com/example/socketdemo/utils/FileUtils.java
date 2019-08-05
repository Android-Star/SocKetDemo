package com.example.socketdemo.utils;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUtils {
  private static final String COMMAND_PATH =
      Environment.getExternalStorageDirectory() + File.separator + "unisound" + File.separator;

  static {
    File file = new File(COMMAND_PATH);
    if (!file.exists()) {
      boolean result = file.mkdirs();
      if (!result) {
        Log.d(FileUtils.class.getSimpleName(), "创建路径出问题");
        throw new RuntimeException("mkdir exception");
      }
    }
  }

  public static void saveSetting(String accessToken) {

    String filePath = COMMAND_PATH + "COMMAND";
    File file = new File(filePath);
    if (file.exists()) {
      file.delete();
    }
    FileOutputStream fileOutputStream = null;
    try {
      file.createNewFile();
      fileOutputStream = new FileOutputStream(file);
      fileOutputStream.write(accessToken.getBytes());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fileOutputStream != null) {
        try {
          fileOutputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }


  public static String readInstallationFile() throws IOException {
    File installation=new File(COMMAND_PATH + "COMMAND");
    RandomAccessFile f = new RandomAccessFile(installation, "r");
    byte[] bytes = new byte[(int) f.length()];
    f.readFully(bytes);
    f.close();
    return new String(bytes);
  }
}
