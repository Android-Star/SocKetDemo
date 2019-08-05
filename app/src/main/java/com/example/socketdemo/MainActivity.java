package com.example.socketdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.socketdemo.utils.FileUtils;
import com.example.socketdemo.utils.JsonTool;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
  private static final int MY_PERMISSIONS_REQUEST = 500;
  private static final int PORT_NUMBER = 8888;
  private EditText etIp;
  private EditText etWakeupAngle, etWakeUPTurnSpeed, etFaceFollowAngle, etFaceFollowSpeed,
      etFaceFollowWait, etFaceFollowAngleTwo, etFaceFollowSpeedTwo, etTurnRelaxAngle,
      etTurnRelaxSpeed, etTurnRelaxWait, etTurnRelaxAngleTwo, etTurnRelaxSpeedTwo;
  private Button btnConnect, btnWakeUP, btnWakeUPSave, btnSelf, btnFaceFollow, btnFaceFollowSave,
      btnRelaxTurn, btnRelaxTurnSave;
  private RadioGroup rgWakeUp, rgFaceFollow, rgFaceFollowTwo, rgRelaxTurn, rgRelaxTurnTwo;
  private RadioButton rbWakeUpLeft, rbWakeUpRight, rbFaceFollowLeft, rbFaceFollowRight,
      rbFaceFollowLeftTwo, rbFaceFollowRightTwo, rbRelaxTurnLeft, rbRelaxTurnRight,
      rbRelaxTurnLeftTwo, rbRelaxTurnRightTwo;

  private String ipStr;
  private SendThread sendThread;

  private Bean commandBean;

  private String[] permissions = new String[] {
      Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();
    checkPermission();
  }

  private void makeDefaultValue() {
    Bean bean = new Bean();
    List<Bean.ItemBean> itemBeans = new ArrayList<>();
    itemBeans.add(makeWakeBean());
    itemBeans.add(makeFaceFollowBean());
    itemBeans.add(makeSelfBean());
    itemBeans.add(makeRelaxBean());
    bean.setList(itemBeans);
    FileUtils.saveSetting(JsonTool.toJson(bean));
    setValue(bean);
  }

  private Bean.ItemBean makeWakeBean() {
    //构建唤醒bean
    Bean.ItemBean itemBean = new Bean.ItemBean();
    itemBean.setTitle("唤醒");
    itemBean.setType(1);
    List<Bean.CommandBean> wakeCommandBeans = new ArrayList<>();
    Bean.CommandBean commandBean = new Bean.CommandBean();
    commandBean.setAngle(15);
    commandBean.setOrientation(1);
    commandBean.setSpeed(15);
    commandBean.setWaitTime(0);
    wakeCommandBeans.add(commandBean);
    itemBean.setCommandBeans(wakeCommandBeans);
    return itemBean;
  }

  private Bean.ItemBean makeFaceFollowBean() {
    //构建唤醒bean
    Bean.ItemBean itemBean = new Bean.ItemBean();
    itemBean.setTitle("人脸跟随");
    itemBean.setType(2);
    List<Bean.CommandBean> wakeCommandBeans = new ArrayList<>();
    Bean.CommandBean commandBean = new Bean.CommandBean();
    commandBean.setAngle(15);
    commandBean.setOrientation(1);
    commandBean.setSpeed(15);
    commandBean.setWaitTime(2000);

    Bean.CommandBean commandBean1 = new Bean.CommandBean();
    commandBean1.setAngle(15);
    commandBean1.setOrientation(2);
    commandBean1.setSpeed(15);
    commandBean1.setWaitTime(0);

    wakeCommandBeans.add(commandBean);
    wakeCommandBeans.add(commandBean1);
    itemBean.setCommandBeans(wakeCommandBeans);
    return itemBean;
  }

  private Bean.ItemBean makeSelfBean() {
    //构建self bean
    Bean.ItemBean itemBean = new Bean.ItemBean();
    itemBean.setTitle("介绍自己");
    itemBean.setType(3);
    List<Bean.CommandBean> wakeCommandBeans = new ArrayList<>();
    Bean.CommandBean commandBean = new Bean.CommandBean();
    wakeCommandBeans.add(commandBean);
    itemBean.setCommandBeans(wakeCommandBeans);
    return itemBean;
  }

  private Bean.ItemBean makeRelaxBean() {
    //构建随意转动bean
    Bean.ItemBean itemBean = new Bean.ItemBean();
    itemBean.setTitle("随意转动");
    itemBean.setType(4);
    List<Bean.CommandBean> wakeCommandBeans = new ArrayList<>();
    Bean.CommandBean commandBean = new Bean.CommandBean();
    commandBean.setAngle(15);
    commandBean.setOrientation(1);
    commandBean.setSpeed(15);
    commandBean.setWaitTime(2000);

    Bean.CommandBean commandBean1 = new Bean.CommandBean();
    commandBean1.setAngle(15);
    commandBean1.setOrientation(2);
    commandBean1.setSpeed(15);
    commandBean1.setWaitTime(0);

    wakeCommandBeans.add(commandBean);
    wakeCommandBeans.add(commandBean1);
    itemBean.setCommandBeans(wakeCommandBeans);
    return itemBean;
  }

  private void setValue(Bean bean) {
    List<Bean.ItemBean> list = bean.getList();
    if (list != null && !list.isEmpty()) {
      for (int i = 0; i < list.size(); i++) {
        Bean.ItemBean itemBean = list.get(i);
        Bean.CommandBean commandBean0 = itemBean.getCommandBeans().get(0);
        switch (itemBean.getType()) {
          case 1:
            //唤醒
            if (commandBean0.getOrientation() == 1) {
              rbWakeUpLeft.setChecked(true);
            } else {
              rbWakeUpRight.setChecked(true);
            }
            etWakeupAngle.setText(String.valueOf(commandBean0.getAngle()));
            etWakeUPTurnSpeed.setText(String.valueOf(commandBean0.getSpeed()));
            break;
          case 2:
            //人脸
            if (commandBean0.getOrientation() == 1) {
              rbFaceFollowLeft.setChecked(true);
            } else {
              rbFaceFollowRight.setChecked(true);
            }
            etFaceFollowAngle.setText(String.valueOf(commandBean0.getAngle()));
            etFaceFollowSpeed.setText(String.valueOf(commandBean0.getSpeed()));
            etFaceFollowWait.setText(String.valueOf(commandBean0.getWaitTime()));

            Bean.CommandBean commandBean1 = itemBean.getCommandBeans().get(1);
            if (commandBean1.getOrientation() == 1) {
              rbFaceFollowLeftTwo.setChecked(true);
            } else {
              rbFaceFollowRightTwo.setChecked(true);
            }
            etFaceFollowAngleTwo.setText(String.valueOf(commandBean1.getAngle()));
            etFaceFollowSpeedTwo.setText(String.valueOf(commandBean1.getSpeed()));

            break;
          case 3:
            //介绍
            break;
          case 4:
            //随意转动
            if (commandBean0.getOrientation() == 1) {
              rbRelaxTurnLeft.setChecked(true);
            } else {
              rbRelaxTurnRight.setChecked(true);
            }
            etTurnRelaxAngle.setText(String.valueOf(commandBean0.getAngle()));
            etTurnRelaxSpeed.setText(String.valueOf(commandBean0.getSpeed()));
            etTurnRelaxWait.setText(String.valueOf(commandBean0.getWaitTime()));

            Bean.CommandBean commandBean11 = itemBean.getCommandBeans().get(1);
            if (commandBean11.getOrientation() == 1) {
              rbRelaxTurnLeftTwo.setChecked(true);
            } else {
              rbRelaxTurnRightTwo.setChecked(true);
            }
            etTurnRelaxAngleTwo.setText(String.valueOf(commandBean11.getAngle()));
            etTurnRelaxSpeedTwo.setText(String.valueOf(commandBean11.getSpeed()));

            break;
          default:
            break;
        }
      }
    }
  }

  void initView() {
    etIp = findViewById(R.id.etIp);
    etWakeupAngle = findViewById(R.id.etWakeUPAngle);
    etWakeUPTurnSpeed = findViewById(R.id.etWakeUPTurnSpeed);
    etFaceFollowAngle = findViewById(R.id.etFaceFollowAngle);
    etFaceFollowSpeed = findViewById(R.id.etFaceFollowSpeed);
    etFaceFollowWait = findViewById(R.id.etFaceFollowWait);
    etFaceFollowAngleTwo = findViewById(R.id.etFaceFollowAngleTwo);
    etFaceFollowSpeedTwo = findViewById(R.id.etFaceFollowSpeedTwo);
    etTurnRelaxAngle = findViewById(R.id.etTurnRelaxAngle);
    etTurnRelaxSpeed = findViewById(R.id.etTurnRelaxSpeed);
    etTurnRelaxWait = findViewById(R.id.etTurnRelaxWait);
    etTurnRelaxAngleTwo = findViewById(R.id.etTurnRelaxAngleTwo);
    etTurnRelaxSpeedTwo = findViewById(R.id.etTurnRelaxSpeedTwo);

    btnConnect = findViewById(R.id.btnConnect);
    btnWakeUP = findViewById(R.id.btnWakeUP);
    btnWakeUPSave = findViewById(R.id.btnWakeUPSave);
    btnSelf = findViewById(R.id.btnSelf);
    btnFaceFollow = findViewById(R.id.btnFaceFollow);
    btnFaceFollowSave = findViewById(R.id.btnFaceFollowSave);
    btnRelaxTurn = findViewById(R.id.btnRelaxTurn);
    btnRelaxTurnSave = findViewById(R.id.btnRelaxTurnSave);

    rgWakeUp = findViewById(R.id.rgWakeUp);
    rgFaceFollow = findViewById(R.id.rgFaceFollow);
    rgFaceFollowTwo = findViewById(R.id.rgFaceFollowTwo);
    rgRelaxTurn = findViewById(R.id.rgRelaxTurn);
    rgRelaxTurnTwo = findViewById(R.id.rgRelaxTurnTwo);

    rbWakeUpLeft = findViewById(R.id.rbWakeUpLeft);
    rbWakeUpRight = findViewById(R.id.rbWakeUpRight);
    rbFaceFollowLeft = findViewById(R.id.rbFaceFollowLeft);
    rbFaceFollowRight = findViewById(R.id.rbFaceFollowRight);
    rbFaceFollowLeftTwo = findViewById(R.id.rbFaceFollowLeftTwo);
    rbFaceFollowRightTwo = findViewById(R.id.rbFaceFollowRightTwo);
    rbRelaxTurnLeft = findViewById(R.id.rbRelaxTurnLeft);
    rbRelaxTurnRight = findViewById(R.id.rbRelaxTurnRight);
    rbRelaxTurnLeftTwo = findViewById(R.id.rbRelaxTurnLeftTwo);
    rbRelaxTurnRightTwo = findViewById(R.id.rbRelaxTurnRightTwo);

    btnConnect.setOnClickListener(this);
    btnWakeUP.setOnClickListener(this);
    btnWakeUPSave.setOnClickListener(this);
    btnSelf.setOnClickListener(this);
    btnFaceFollow.setOnClickListener(this);
    btnFaceFollowSave.setOnClickListener(this);
    btnRelaxTurn.setOnClickListener(this);
    btnRelaxTurn.setOnClickListener(this);
    btnRelaxTurnSave.setOnClickListener(this);
    setRbListener();
  }

  private void setRbListener() {
    rbWakeUpLeft.setOnCheckedChangeListener(this);
    rbWakeUpRight.setOnCheckedChangeListener(this);
    rbFaceFollowLeft.setOnCheckedChangeListener(this);
    rbFaceFollowRight.setOnCheckedChangeListener(this);
    rbFaceFollowLeftTwo.setOnCheckedChangeListener(this);
    rbFaceFollowRightTwo.setOnCheckedChangeListener(this);
    rbRelaxTurnLeft.setOnCheckedChangeListener(this);
    rbRelaxTurnRight.setOnCheckedChangeListener(this);
    rbRelaxTurnLeftTwo.setOnCheckedChangeListener(this);
    rbRelaxTurnRightTwo.setOnCheckedChangeListener(this);
  }

  private void checkPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
          != PackageManager.PERMISSION_GRANTED
          || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
          != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
      } else {
        initData();
      }
    } else {
      initData();
    }
  }

  void initSocket() {
    new Thread() {
      @Override public void run() {
        //创建Socket对象
        Socket socket = null;
        try {
          socket = new Socket(ipStr, PORT_NUMBER);

          ReceiveThread receiveThread = new ReceiveThread(socket);
          receiveThread.start();
          sendThread = new SendThread(socket);
          sendThread.start();
          runOnUiThread(new Runnable() {
            @Override public void run() {
              Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
            }
          });
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {

    if (requestCode == MY_PERMISSIONS_REQUEST) {
      if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, "权限未通过", Toast.LENGTH_LONG).show();
        finish();
      } else {
        initData();
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  private void initData() {
    try {
      String fileStr = FileUtils.readInstallationFile();
      if (TextUtils.isEmpty(fileStr)) {
        makeDefaultValue();
      } else {
        commandBean = JsonTool.fromJson(fileStr, Bean.class);
        if (commandBean == null) {
          makeDefaultValue();
        } else {
          setValue(commandBean);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      makeDefaultValue();
    }
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnConnect:
        ipStr = etIp.getText().toString().trim();
        if (!isIpStr(ipStr)) {
          Toast.makeText(MainActivity.this, "请输入正确的ip地址", Toast.LENGTH_SHORT).show();
          return;
        }
        initSocket();
        break;
      case R.id.btnWakeUP:
        Bean.CommandBean wakeBean1 = commandBean.getList().get(0).getCommandBeans().get(0);
        if (sendThread != null) {
          sendThread.sendMsg("1#唤醒#" + wakeBean1.getAngle() + "#" + wakeBean1.getSpeed() + "#"
              + wakeBean1.getOrientation());
        }
        break;
      case R.id.btnWakeUPSave:
        int wakeAngle = Integer.valueOf(etWakeupAngle.getText().toString().trim());
        int wakeSpeed = Integer.valueOf(etWakeUPTurnSpeed.getText().toString().trim());
        Bean.CommandBean wakeCommandBean1 = commandBean.getList().get(0).getCommandBeans().get(0);
        wakeCommandBean1.setAngle(wakeAngle < 0 ? 15 : wakeAngle);
        wakeCommandBean1.setSpeed(wakeSpeed < 0 ? 15 : wakeSpeed);
        wakeCommandBean1.setOrientation(rbWakeUpLeft.isChecked() ? 1 : 2);
        FileUtils.saveSetting(JsonTool.toJson(commandBean));
        break;
      case R.id.btnFaceFollow:
        Bean.CommandBean faceBean1 = this.commandBean.getList().get(1).getCommandBeans().get(0);
        Bean.CommandBean faceBean2 = this.commandBean.getList().get(1).getCommandBeans().get(1);
        if (sendThread != null) {
          sendThread.sendMsg("2#人脸跟随#" + faceBean1.getAngle() + "#" + faceBean1.getSpeed() + "#"
              + faceBean1.getOrientation() + "#" + faceBean1.getWaitTime() + "#"
              + faceBean2.getAngle() + "#" + faceBean2.getSpeed() + "#"
              + faceBean2.getOrientation());
        }
        break;
      case R.id.btnFaceFollowSave:
        int faceAngle = Integer.valueOf(etFaceFollowAngle.getText().toString().trim());
        int faceSpeed = Integer.valueOf(etFaceFollowSpeed.getText().toString().trim());
        int faceAngleTwo = Integer.valueOf(etFaceFollowAngleTwo.getText().toString().trim());
        int faceSpeedTwo = Integer.valueOf(etFaceFollowSpeedTwo.getText().toString().trim());
        int waitFace = Integer.parseInt(etFaceFollowWait.getText().toString().trim());
        Bean.CommandBean faceCommandBean1 =
            this.commandBean.getList().get(1).getCommandBeans().get(0);
        faceCommandBean1.setAngle(faceAngle < 0 ? 15 : faceAngle);
        faceCommandBean1.setSpeed(faceSpeed < 0 ? 15 : faceSpeed);
        faceCommandBean1.setOrientation(rbFaceFollowLeft.isChecked() ? 1 : 2);
        faceCommandBean1.setWaitTime(waitFace);
        Bean.CommandBean faceCommandBean2 =
            this.commandBean.getList().get(1).getCommandBeans().get(1);
        faceCommandBean2.setAngle(faceAngleTwo < 0 ? 15 : faceAngleTwo);
        faceCommandBean2.setSpeed(faceSpeedTwo < 0 ? 15 : faceSpeedTwo);
        faceCommandBean2.setOrientation(rbFaceFollowLeftTwo.isChecked() ? 1 : 2);
        FileUtils.saveSetting(JsonTool.toJson(commandBean));
        break;
      case R.id.btnSelf:
        if (sendThread != null) sendThread.sendMsg("3#介绍自己");
        break;
      case R.id.btnRelaxTurn:
        Bean.CommandBean relaxBean1 = this.commandBean.getList().get(3).getCommandBeans().get(0);
        Bean.CommandBean relaxBean2 = this.commandBean.getList().get(3).getCommandBeans().get(1);
        if (sendThread != null) {
          sendThread.sendMsg("4#随意转动#" + relaxBean1.getAngle() + "#" + relaxBean1.getSpeed() + "#"
              + relaxBean1.getOrientation() + "#" + relaxBean1.getWaitTime() + "#"
              + relaxBean2.getAngle() + "#" + relaxBean2.getSpeed() + "#"
              + relaxBean2.getOrientation());
        }
        break;
      case R.id.btnRelaxTurnSave:
        int relaxAngle = Integer.valueOf(etTurnRelaxAngle.getText().toString().trim());
        int relaxSpeed = Integer.valueOf(etTurnRelaxSpeed.getText().toString().trim());
        int relaxAngleTwo = Integer.valueOf(etTurnRelaxAngleTwo.getText().toString().trim());
        int relaxSpeedTwo = Integer.valueOf(etTurnRelaxSpeedTwo.getText().toString().trim());
        int waitRelax = Integer.parseInt(etTurnRelaxWait.getText().toString().trim());
        Bean.CommandBean relaxCommandBean1 =
            this.commandBean.getList().get(3).getCommandBeans().get(0);
        relaxCommandBean1.setAngle(relaxAngle < 0 ? 15 : relaxAngle);
        relaxCommandBean1.setSpeed(relaxSpeed < 0 ? 15 : relaxSpeed);
        relaxCommandBean1.setOrientation(rbRelaxTurnLeft.isChecked() ? 1 : 2);
        relaxCommandBean1.setWaitTime(waitRelax);
        Bean.CommandBean relaxCommandBean2 =
            this.commandBean.getList().get(3).getCommandBeans().get(1);
        relaxCommandBean2.setAngle(relaxAngleTwo < 0 ? 15 : relaxAngleTwo);
        relaxCommandBean2.setSpeed(relaxSpeedTwo < 0 ? 15 : relaxSpeedTwo);
        relaxCommandBean2.setOrientation(rbRelaxTurnLeftTwo.isChecked() ? 1 : 2);
        FileUtils.saveSetting(JsonTool.toJson(commandBean));
        break;
      default:
        break;
    }
  }

  private boolean isIpStr(String ipStr) {
    String pattern =
        "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";

    Pattern r = Pattern.compile(pattern);
    Matcher m = r.matcher(ipStr);
    return m.matches();
  }

  @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

  }
}
