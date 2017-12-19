package bluetest.guozhi.bluedemo;

import android.os.Environment;

import java.io.File;

/**
 * Created by wangcheng on 2016-12-11.
 */

public class Contents {


    /**
     * 发现设备（未绑定）
     */
    public static  final int Bluetooth_ACTION_FOUND=0;
    /**
     * 搜索完成
     */
    public static  final int Bluetooth_ACTION_DISCOVERY_FINISHED=1;
    /**
     * 正在配对
     */
    public static  final int Bluetooth_ACTION_BOND_BONDING=2;
    /**
     * 完成配对
     */
    public static  final int Bluetooth_BOND_BONDED=3;
    /**
     * 取消配对
     */
    public static  final int Bluetooth_BOND_NONE=4;
    /**
     * 连接成功
     */
    public static  final int Bluetooth_ACTION_ACL_CONNECTED=5;
    /**
     * 连接断开 失败
     */
    public static  final int Bluetooth_ACTION_ACL_DISCONNECTED=6;
    /**
     * 蓝牙打开
     */
    public static  final int Bluetooth_ACTION_STATE_ENABLE =7;
    /**
     * 蓝牙关闭
     */
    public static  final int Bluetooth_ACTION_STATE_DISENABLE =8;
    public static  final int SUCCESS=1;
    public static  final int FAILED=0;

    /**
     * 复位打印机
     */
    public static final byte[] RESET = {0x1b, 0x40};

    /**
     * 左对齐
     */
    public static final byte[] ALIGN_LEFT = {0x1b, 0x61, 0x00};

    /**
     * 中间对齐
     */
    public static final byte[] ALIGN_CENTER = {0x1b, 0x61, 0x01};

    /**
     * 右对齐
     */
    public static final byte[] ALIGN_RIGHT = {0x1b, 0x61, 0x02};

    /**
     * 选择加粗模式
     */
    public static final byte[] BOLD = {0x1b, 0x45, 0x01};

    /**
     * 取消加粗模式
     */
    public static final byte[] BOLD_CANCEL = {0x1b, 0x45, 0x00};

    /**
     * 宽高加倍
     */
    public static final byte[] DOUBLE_HEIGHT_WIDTH = {0x1d, 0x21, 0x11};

    /**
     * 宽加倍
     */
    public static final byte[] DOUBLE_WIDTH = {0x1d, 0x21, 0x10};

    /**
     * 高加倍
     */
    public static final byte[] DOUBLE_HEIGHT = {0x1d, 0x21, 0x01};

    /**
     * 字体不放大
     */
    public static final byte[] NORMAL = {0x1d, 0x21, 0x00};

    /**
     * 设置默认行间距
     */
    public static final byte[] LINE_SPACING_DEFAULT = {0x1b, 0x32};
    /**
     * 归零
     */
    public static  final byte[] LINE_SPACING_0PX={0X1b,0X33,0X00};
}
