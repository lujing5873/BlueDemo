package bluetest.guozhi.bluedemo.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.support.annotation.RequiresApi;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

import bluetest.guozhi.bluedemo.utils.RxUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/25.
 */

public class BluetoothBiz implements IBluetoothBiz {
    private List<IBluePresenter> list=new ArrayList<>();
    private BluetoothSocket socket = null;
    private OutputStream outputStream;


    private BluetoothAdapter bluetoothAdapter;
    private BluetoothBiz(){
    }
    private static class LazyHolder {
        private static final IBluetoothBiz INSTANCE = new BluetoothBiz();
    }
    public static final IBluetoothBiz getInstance() {
        return LazyHolder.INSTANCE;
    }
    public  void addIBluePresenter(IBluePresenter bluePresenter){
            if(bluePresenter!=null){
                list.add(bluePresenter);
            }
    }
    public void removeIBluePresenter(IBluePresenter bluePresenter){
        list.remove(bluePresenter);
    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    @Override
    public Set<BluetoothDevice> getBindDevice() {
        if(bluetoothAdapter!=null){
            return  bluetoothAdapter.getBondedDevices();
        }
        return null;
    }

    @Override
    public boolean isEnable() {
        if(bluetoothAdapter!=null){
            return bluetoothAdapter.isEnabled();
        }
        return false;
    }

    @Override
    public void cancleSerch() {
        if(bluetoothAdapter!=null){
             bluetoothAdapter.cancelDiscovery();
        }
    }

    @Override
    public void print(byte[] b,boolean isNewLine) throws IOException {
        if(outputStream!=null) {


            if (isNewLine) {
                outputStream.write(b);
                outputStream.write("\n".getBytes());
                outputStream.flush();
            } else {
                outputStream.write(b);
                outputStream.flush();
            }
        }
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }



    public void changeBluethooth(){
        if(bluetoothAdapter==null){
            return;
        }
        if(!bluetoothAdapter.isEnabled()){
            //不做提示，强行打开
            bluetoothAdapter.enable();
        }else{
            bluetoothAdapter.disable();
        }
    }

    public void startSerch(){
        if(bluetoothAdapter==null){
            return;
        }
        bluetoothAdapter.startDiscovery();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void bindDevice(BluetoothDevice bluetoothDevice){
        bluetoothDevice.createBond();
    }
    public void connect(final BluetoothDevice bluetoothDevice) {
        if(outputStream!=null){
            try {
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Subscriber<OutputStream> outputStreamSubscriber=new Subscriber<OutputStream>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(OutputStream outputStream) {
                if(outputStream!=null){
                    for(IBluePresenter presenter:list){
                        presenter.blueConnectSuccess();
                    }
                }
            }
        };
        RxUtil.fromCallable(new Callable<BluetoothSocket>() {
            @Override
            public BluetoothSocket call() throws Exception {
                socket=bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")); //值是固定值   蓝牙热敏打印机的话
                socket.connect();
                return socket;
            }
        }).map(new Func1<BluetoothSocket, OutputStream>() {
            @Override
            public OutputStream call(BluetoothSocket bluetoothSocket) {
                try {
                    outputStream=bluetoothSocket.getOutputStream();
                    return outputStream;
                } catch (Throwable t) {
                    throw Exceptions.propagate(t);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(outputStreamSubscriber);

    }


    @Override
    public void scanSuccess(BluetoothDevice device) {
        for(IBluePresenter presenter:list){
            presenter.scanSuccess(device);
        }
    }

    @Override
    public void scanRecevier(BluetoothDevice device, int type) {
        for(IBluePresenter presenter:list){
            presenter.scanRecevier(device,type);
        }
    }

}
