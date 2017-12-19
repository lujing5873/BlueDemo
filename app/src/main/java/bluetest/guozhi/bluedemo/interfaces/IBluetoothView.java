package bluetest.guozhi.bluedemo.interfaces;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;


import bluetest.guozhi.bluedemo.activity.BaseActivity;

/**
 * Created by dell on 2017/8/23.
 */

public interface IBluetoothView {
  /**
   * 发现新设备
   * @param device  发现的设备
   */
  void scanSuccess(BluetoothDevice device);

  /**
   * 当前搜索状态
   * @param tag  0：搜索完成  1：正在搜索
   */
  void isScaning(int tag);
  /**
   * 蓝牙是否打开
   * @param tag  0：未打开 1：打开
   */
  void bluetoothIsOpen(int tag);

  /**
   * 蓝牙设备是否配对成功
   * @param tag 0：配对失败 1：配对成功
   */
  void blueBind(BluetoothDevice bluetoothDevice, int tag);

  void blueConnectSuccess();
  /**
   * 获取activity
   * @return
   */
  BaseActivity getActivity();

  BluetoothAdapter getBluethoothAdapter();
}
