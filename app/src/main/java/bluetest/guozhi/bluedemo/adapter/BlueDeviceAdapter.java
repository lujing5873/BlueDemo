package bluetest.guozhi.bluedemo.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import bluetest.guozhi.bluedemo.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2017/8/24.
 */

public class BlueDeviceAdapter extends RecyclerView.Adapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<BluetoothDevice> list=new ArrayList<>();
    private int tag;
    private onRecyclerViewClick onRecyclerViewClick;

    public BlueDeviceAdapter(Context context, List<BluetoothDevice> list, onRecyclerViewClick onRecyclerViewClick , int tag) {
        this.context = context;
        this.onRecyclerViewClick=onRecyclerViewClick;
        this.tag=tag;
        layoutInflater = LayoutInflater.from(context);
        this.list=list;
    }
    public interface  onRecyclerViewClick{
        void onClick(View view, int position, int tag);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_bluedevice, parent,false);
        return new ViewHolder(view,onRecyclerViewClick,tag);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;

        BluetoothDevice device=list.get(position);
        String name=device.getName();
        if(device.getBondState()== BluetoothDevice.BOND_BONDING){
            if(TextUtils.isEmpty(name)){
                viewHolder.itemDeviceName.setText(list.get(position).getAddress()+"\n"+ context.getString(R.string.blue_binding));

            }else{
                viewHolder.itemDeviceName.setText(name+"\n"+ context.getString(R.string.blue_binding));
            }
        }else{
            if(TextUtils.isEmpty(name)){
                viewHolder.itemDeviceName.setText(list.get(position).getAddress());
            }else{
                viewHolder.itemDeviceName.setText(name);
            }
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder  extends RecyclerView.ViewHolder{
        @BindView(R.id.item_device_icon)
        ImageView itemDeviceIcon;
        @BindView(R.id.item_device_name)
        TextView itemDeviceName;

        ViewHolder(View view, final onRecyclerViewClick onRecyclerViewClick, final int tag) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewClick.onClick(v,getAdapterPosition(),tag);
                }
            });
        }
    }

}
