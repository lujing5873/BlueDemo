package bluetest.guozhi.bluedemo.receiver;

import android.bluetooth.BluetoothDevice;

/**
 * Created by dell on 2017/8/25.
 */

public interface IBluePresenter {
    void scanSuccess(BluetoothDevice device);
    void scanRecevier(BluetoothDevice device, int type);
    void blueConnectSuccess();
}
