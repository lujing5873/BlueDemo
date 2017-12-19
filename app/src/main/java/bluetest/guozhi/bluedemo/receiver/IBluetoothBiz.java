package bluetest.guozhi.bluedemo.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.io.IOException;
import java.util.Set;

/**
 * Created by dell on 2017/8/23.
 */

public interface IBluetoothBiz {
    /**
     *搜索成功 返回搜索到的新设备
     * @param device
     */
    void scanSuccess(BluetoothDevice device);

    /**
     * 搜索广播回调
     * @param device
     * @param type  广播类型
     */
    void scanRecevier(BluetoothDevice device, int type);

    /**
     * 添加回调类
     * @param bluePresenter
     */
    void addIBluePresenter(IBluePresenter bluePresenter);

    /**
     * 移除回调类
     * @param bluePresenter
     */
    void removeIBluePresenter(IBluePresenter bluePresenter);

    /**
     * 更改蓝牙状态
     */
    void changeBluethooth();

    /**
     * 开始搜索
     */
    void startSerch();

    /**
     * 绑定设备
     * @param bluetoothDevice
     */
    void bindDevice(BluetoothDevice bluetoothDevice);

    /**
     * 连接设备
     * @param bluetoothDevice
     */
    void connect(final BluetoothDevice bluetoothDevice);

    /**
     * 获取蓝牙adapter
     * @return
     */
    BluetoothAdapter getBluetoothAdapter();

    /**
     * 設置藍牙adapter
     * @param bluetoothAdapter
     */
    void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter);

    /**
     * 获取已经绑定的蓝牙设备
     * @return
     */
    Set<BluetoothDevice> getBindDevice();

    /**
     * 蓝牙是否打开
     * @return
     */
    boolean isEnable();

    /**
     * 取消搜索
     */
    void cancleSerch();

    /**
     * 打印
     * @param b
     * @param isNewLine
     * @throws IOException
     */
    void print(byte[] b, boolean isNewLine) throws IOException;
}
