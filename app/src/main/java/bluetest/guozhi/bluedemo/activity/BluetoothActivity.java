package bluetest.guozhi.bluedemo.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import bluetest.guozhi.bluedemo.Contents;
import bluetest.guozhi.bluedemo.R;
import bluetest.guozhi.bluedemo.adapter.BlueDeviceAdapter;
import bluetest.guozhi.bluedemo.interfaces.IBluetoothView;
import bluetest.guozhi.bluedemo.presenter.BluetoothPresenter;
import bluetest.guozhi.bluedemo.utils.ToastUtils;
import bluetest.guozhi.bluedemo.widget.BlueDiverItemDecoration;
import bluetest.guozhi.bluedemo.widget.MaterialProgressDrawable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/23.
 */

public class BluetoothActivity extends BaseActivity<BluetoothPresenter> implements IBluetoothView, BlueDeviceAdapter.onRecyclerViewClick {
    @BindView(R.id.blue_open)
    TextView mOpen;
    @BindView(R.id.blue_switch)
    SwitchCompat mSwitch;
    @BindView(R.id.blue_ly1)
    LinearLayout mLinearLayout1;
    @BindView(R.id.blue_ly3)
    LinearLayout mLinearLayout3;
    @BindView(R.id.blue_ly4)
    LinearLayout mLinearLayout4;
    @BindView(R.id.blue_v4)
    View mView4;
    @BindView(R.id.blue_v3)
    View mView3;
    @BindView(R.id.blue_t1)
    TextView mName;
    @BindView(R.id.blue_recycler_bind)
    RecyclerView mRecyclerBind;
    @BindView(R.id.blue_recycler_find)
    RecyclerView mRecyclerFind;
    @BindView(R.id.blue_ib_serch)
    ImageView mSerch;
    @BindView(R.id.blue_main)
    LinearLayout mainLayout;
    private MaterialProgressDrawable mProgress;
    private final int CIRCLE_BG_LIGHT = 0xFFFFFFFF;
    boolean start = false;
    private List<BluetoothDevice> bindList = new ArrayList<>();
    private List<BluetoothDevice> unBindList = new ArrayList<>();
    private BlueDeviceAdapter bindDeviceAdapter;
    private BlueDeviceAdapter unBindDeviceAdapter;
    private final int BINDTAG = 0;
    private final int UNBINDTAG = 1;
    private int bindPosition=-1;

    @Override
    public void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mProgress = new MaterialProgressDrawable(this, mSerch);
        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
        //圈圈颜色,可以是多种颜色
        mProgress.setColorSchemeColors(Color.GRAY);
        //设置圈圈的各种大小
        mProgress.updateSizes(MaterialProgressDrawable.LARGE);
        resetProgress();
        mSerch.setImageDrawable(mProgress);
        mRecyclerBind.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerFind.setLayoutManager(new LinearLayoutManager(this));
        bindDeviceAdapter = new BlueDeviceAdapter(this, bindList, this, BINDTAG);
        unBindDeviceAdapter = new BlueDeviceAdapter(this, unBindList, this, UNBINDTAG);
        mRecyclerBind.setAdapter(bindDeviceAdapter);
        mRecyclerFind.setAdapter(unBindDeviceAdapter);
        mRecyclerBind.setItemAnimator(new DefaultItemAnimator());
        mRecyclerFind.setItemAnimator(new DefaultItemAnimator());
        mRecyclerBind.addItemDecoration(new BlueDiverItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerFind.addItemDecoration(new BlueDiverItemDecoration(this, DividerItemDecoration.VERTICAL));
        mSwitch.setFocusable(false);
        mSwitch.setClickable(false);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_bluetooth;
    }

    @Override
    public void setPresenter() {
        presenter = new BluetoothPresenter(this);
        initBlue();
    }


    @Override
    public void scanSuccess(BluetoothDevice device) {
        unBindList.add(device);
        unBindDeviceAdapter.notifyDataSetChanged();

    }

    @Override
    public void isScaning(int tag) {
        if (tag == Contents.SUCCESS) { //
//            Toast.makeText(this, R.string.scan_over, Toast.LENGTH_SHORT).show();
            unBindDeviceAdapter.notifyDataSetChanged();
            mProgress.stop();
            resetProgress();
            start = false;
        }
    }

    @Override
    public void bluetoothIsOpen(int tag) {
        mLinearLayout1.setEnabled(true);
        mOpen.setTextColor(Color.BLACK);
        initBlue();
    }

    @Override
    public void blueBind(BluetoothDevice bluetoothDevice, int tag) {
        if (tag == Contents.FAILED) {//绑定失败
            bindPosition=-1;
            unBindDeviceAdapter.notifyDataSetChanged();
        } else {
            unBindList.remove(bindPosition);
            unBindDeviceAdapter.notifyItemRemoved(bindPosition);
            resetBindView(true);
            bindPosition=-1;
        }
    }

    @Override
    public void blueConnectSuccess() {
        ToastUtils.showShort(this,getResources().getString(R.string.print_connect));
    }

    @Override
    protected void onDestroy() {
        presenter.destory();
        super.onDestroy();
    }

    @Override
    public BluetoothActivity getActivity() {
        return this;
    }

    @Override
    public BluetoothAdapter getBluethoothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }


    @OnClick({ R.id.blue_ly1, R.id.blue_ly2, R.id.blue_ib_serch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.blue_ly1:
                mLinearLayout1.setEnabled(false);
                mOpen.setTextColor(getResources().getColor(R.color.date_color));
                boolean isEnable = presenter.isEnable();
                resetUnbindView(!isEnable);
                resetBindView(!isEnable);
                if (isEnable) {
                    mSwitch.setChecked(false);
                    presenter.cancelDiscovery();
                    mSerch.setEnabled(false);
                } else {
                    mSwitch.setChecked(true);

                }
                presenter.changeBluethooth();
                break;
            case R.id.blue_ly2:
                break;
            case R.id.blue_ib_serch:
                if(bindPosition!=-1){
                    return;
                }
                if (start) {
                    presenter.cancelDiscovery();
                    mProgress.stop();
                    resetProgress();
                    start = false;
                } else {
                    resetBindView(true);
                    resetUnbindView(true);
                    mProgress.start();
                    start = true;
                    presenter.startDiscovery();
                }
                break;
        }
    }

    private void resetProgress() {
        //圈圈的旋转角度
        mProgress.setProgressRotation(0.5f);
        //圈圈周长，0f-1F
        mProgress.setStartEndTrim(0f, 0.9f);
        //箭头大小，0f-1F
        mProgress.setArrowScale(1);
        //透明度，0-255
        mProgress.setAlpha((int) (255));
    }

    public void initBlue() {
        boolean isEnable = presenter.isEnable();
        resetUnbindView(isEnable);
        resetBindView(isEnable);
        if (isEnable) { //蓝牙打开了
            mSerch.setEnabled(true);
            mSwitch.setChecked(true);
            presenter.startDiscovery();
            mProgress.start();
            start = true;
        } else {
            start=false;
            mSerch.setEnabled(false);
        }
        mainLayout.invalidate();
    }

    public void resetUnbindView(boolean isEnable) {
        unBindList.clear();
        unBindDeviceAdapter.notifyDataSetChanged();
        if (isEnable) {
            mLinearLayout4.setVisibility(View.VISIBLE);
            mView4.setVisibility(View.VISIBLE);
        } else {
            mLinearLayout4.setVisibility(View.GONE);
            mView4.setVisibility(View.GONE);
        }

    }

    public void resetBindView(boolean isEnable) {
        Set<BluetoothDevice> s = presenter.getBind();
        if (s != null && s.size() > 0) {
            bindList.clear();
            bindList.addAll(s);
            bindDeviceAdapter.notifyDataSetChanged();
        }

        if (isEnable && s.size() > 0) {
            mLinearLayout3.setVisibility(View.VISIBLE);
            mView3.setVisibility(View.VISIBLE);
        } else {
            mLinearLayout3.setVisibility(View.GONE);
            mView3.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View view, int position, int tag) {
        presenter.cancelDiscovery();
        switch (tag) {
            case BINDTAG:
                presenter.connect(bindList.get(position));
                break;
            case UNBINDTAG:
                bindPosition = position;
                presenter.bindDevice(unBindList.get(position));
                unBindDeviceAdapter.notifyDataSetChanged();
                break;
        }
    }

}
