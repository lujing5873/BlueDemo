package bluetest.guozhi.bluedemo.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import bluetest.guozhi.bluedemo.Contents;
import bluetest.guozhi.bluedemo.interfaces.IBluetoothView;
import bluetest.guozhi.bluedemo.receiver.BluetoothBiz;
import bluetest.guozhi.bluedemo.receiver.BluetoothReceiver;
import bluetest.guozhi.bluedemo.receiver.IBluePresenter;
import bluetest.guozhi.bluedemo.receiver.IBluetoothBiz;
import bluetest.guozhi.bluedemo.utils.BlueUtils;
import rx.Observable;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/23.
 */

public class BluetoothPresenter extends BasePresenter implements IBluePresenter {
    private IBluetoothView bluetoothView;
    private IBluetoothBiz bluetoothBiz;
    private BluetoothReceiver bluetoothReceiver;
    private Set<BluetoothDevice> set=new HashSet<>();
    public BluetoothPresenter(IBluetoothView bluetoothView){
        bluetoothBiz= BluetoothBiz.getInstance();
        this.bluetoothView=bluetoothView;

        if(bluetoothBiz.getBluetoothAdapter()==null){
            bluetoothBiz.setBluetoothAdapter(bluetoothView.getBluethoothAdapter());
            bluetoothReceiver=BluetoothReceiver.getInstance();
            bluetoothReceiver.setBluetoothBiz(bluetoothBiz);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            bluetoothView.getActivity().registerReceiver(bluetoothReceiver,intentFilter);
        }
        bluetoothBiz.addIBluePresenter(this);
    }
    public void changeBluethooth(){
        bluetoothBiz.changeBluethooth();

    }
    public void startDiscovery(){
        bluetoothBiz.startSerch();
    }

    public void bindDevice(BluetoothDevice bluetoothDevice){
        bluetoothBiz.bindDevice(bluetoothDevice);
    }
    public void connect(BluetoothDevice bluetoothDevice){
        bluetoothBiz.connect(bluetoothDevice);
    }

    /**
     * 光栅打印图片
     * @param id
     */
    public void printImg(final int id){
        Schedulers.io().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds=false;
                Bitmap bitmap=BitmapFactory.decodeResource(bluetoothView.getActivity().getResources(),id,options);
                if(bitmap==null){
                    System.out.println("bitmap为空");
                    return;
                }
                bitmap=compressPic(bitmap);
                byte [][] bytes= BlueUtils.draw2PxPoint(bitmap);
                byte[] data = new byte[8];
                int k = 0;
                data[k++] = 0x1D;
                data[k++] = 0x76;
                data[k++] = 0x30;
                data[k++] = 0x00;
                data[k++] = (byte) (bitmap.getWidth()/8%256); //xl
                data[k++] = (byte) (bitmap.getWidth()/256); //xh
                data[k++]=(byte)(bitmap.getHeight()%256); //yl
                data[k++]=(byte)(bitmap.getHeight()/256);//yh
                try {
                    bluetoothBiz.print(Contents.RESET,false);
                    bluetoothBiz.print(Contents.LINE_SPACING_DEFAULT,false);
                    bluetoothBiz.print(data,false);
                    for(int i=0;i<bytes.length;i++){
                        bluetoothBiz.print(bytes[i],false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public boolean isEnable(){
        return  bluetoothBiz.isEnable();
    }
    public Set<BluetoothDevice> getBind(){
        return bluetoothBiz.getBindDevice();
    }
    public void cancelDiscovery(){
        set.clear();
        bluetoothBiz.cancleSerch();
    }
    public void destory(){
        bluetoothBiz.removeIBluePresenter(this);
    }
    @Override
    public void scanSuccess(BluetoothDevice device) {
        if(set.add(device)){
            bluetoothView.scanSuccess(device);
        }
    }

    @Override
    public void scanRecevier(BluetoothDevice bluetoothDevice, int type) {
        switch (type){
            case Contents.Bluetooth_ACTION_DISCOVERY_FINISHED://结束搜索
                    set.clear();
                    bluetoothView.isScaning(Contents.SUCCESS);//
                break;
            case Contents.Bluetooth_ACTION_BOND_BONDING: //正在配对
                break;
            case Contents.Bluetooth_BOND_BONDED://完成配对
                bluetoothView.blueBind(bluetoothDevice,Contents.SUCCESS);
                break;
            case Contents.Bluetooth_BOND_NONE://取消配对
                bluetoothView.blueBind(bluetoothDevice,Contents.FAILED);
                break;
            case Contents.Bluetooth_ACTION_ACL_CONNECTED: //连接成功
                break;
            case Contents.Bluetooth_ACTION_ACL_DISCONNECTED://连接失败
                break;
            case Contents.Bluetooth_ACTION_STATE_ENABLE://打开蓝牙
                bluetoothView.bluetoothIsOpen(Contents.SUCCESS);
                break;
            case Contents.Bluetooth_ACTION_STATE_DISENABLE://关闭蓝牙
                bluetoothView.bluetoothIsOpen(Contents.FAILED);
                break;
        }
    }

    @Override
    public void blueConnectSuccess() {
        bluetoothView.blueConnectSuccess();
    }

    /**
     * 对图片进行压缩（去除透明度）
     *
     */
    public static Bitmap compressPic(Bitmap bitmap) {
        // 获取这个图片的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 指定调整后的宽度和高度
        int newWidth = 240;
        int newHeight = 240;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmap, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }
}
