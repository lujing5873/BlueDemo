package bluetest.guozhi.bluedemo.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import bluetest.guozhi.bluedemo.Contents;


/**
 * Created by dell on 2017/8/23.
 */

public class BluetoothReceiver extends BroadcastReceiver {
    public IBluetoothBiz getBluetoothBiz() {
        return bluetoothBiz;
    }
    public void setBluetoothBiz(IBluetoothBiz bluetoothBiz) {
        this.bluetoothBiz = bluetoothBiz;
    }
    private IBluetoothBiz bluetoothBiz;

    private BluetoothReceiver(){}
    private static class LazyHolder{
        private static final BluetoothReceiver Instance=new BluetoothReceiver();
    }
    public static final BluetoothReceiver getInstance(){
        return LazyHolder.Instance;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(bluetoothBiz==null){
            return;
        }
        String action = intent.getAction();
        //找到设备
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent
                    .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) { //发现设备
                bluetoothBiz.scanSuccess(device);
            }
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED//搜索完成
                .equals(action)) {
            bluetoothBiz.scanRecevier(null, Contents.Bluetooth_ACTION_DISCOVERY_FINISHED);

        } //状态改变时
        else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_BONDING://正在配对
                    bluetoothBiz.scanRecevier(device,Contents.Bluetooth_ACTION_BOND_BONDING);
                    break;
                case BluetoothDevice.BOND_BONDED://配对结束
                    bluetoothBiz.scanRecevier(device,Contents.Bluetooth_BOND_BONDED);
                    break;
                case BluetoothDevice.BOND_NONE://取消配对/未配对
                    bluetoothBiz.scanRecevier(device,Contents.Bluetooth_BOND_NONE);
                default:
                    break;
            }
        }else  if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){//连接成功
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            bluetoothBiz.scanRecevier(device,Contents.Bluetooth_ACTION_ACL_CONNECTED);
        } else if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)){
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            bluetoothBiz.scanRecevier(device,Contents.Bluetooth_ACTION_ACL_DISCONNECTED);//连接断开
        }else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
            switch (state){
                case 10://蓝牙关闭
                    bluetoothBiz.scanRecevier(null,Contents.Bluetooth_ACTION_STATE_DISENABLE);
                    break;
                case 12: //蓝牙打开
                    bluetoothBiz.scanRecevier(null,Contents.Bluetooth_ACTION_STATE_ENABLE);
                    break;
            }
        }
    }
}
