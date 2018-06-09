package fruitbasket.com.bodyfit.bluetooth;

import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import org.litepal.crud.DataSupport;

import fruitbasket.com.bodyfit.data.DataUnitForSQL;
import fruitbasket.com.bodyfit.test.TestService;

public abstract class BluetoothFragment extends Fragment {
//    private static final String TAG="BluetoothFragment";

    private Intent intentToBluetoothService;
    private BluetoothService bluetoothService;

    private Intent intentToTestService;
    private TestService testService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.i(TAG, "onCreate()");
        intentToBluetoothService = new Intent(this.getContext(), BluetoothService.class);
        intentToTestService = new Intent(this.getContext(), TestService.class);
    }

    /**
     * 点击按钮后，显示蓝牙搜索列表对话框的函数
     */
    private void getBluetoothAddressByDialog() {
        if (bluetoothService != null) {
            new AlertDialog
                    .Builder(getContext())
                    .setTitle("Bluetooth List")
                    .setAdapter(bluetoothService.getArrayAdapter(),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, final int which) {
                                    bluetoothService.stopDiscovery();
                                    bluetoothService.setBluetoothAddress(
                                            bluetoothService.getDeviceArrayList()
                                                    .get(which)
                                                    .getAddress());
                                    bluetoothService.connectToDevice();
                                }
                            })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface arg0) {
                            bluetoothService.stopDiscovery();
                        }
                    }).show();
        }
    }


    /**
     * 启动蓝牙等一系列工作
     */
    protected void startWork() {
        this.getActivity().bindService(intentToBluetoothService, bluetoothServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 关闭蓝牙等一系列工作
     */
    protected void stopWork() {
        this.getActivity().unbindService(bluetoothServiceConnection);
    }

    /**
     * 更新用户界面
     *
     * @param what   指明更新界面的哪一部分
     * @param bundle 附带更新界面的数据
     */
    protected abstract void updateUI(int what, Bundle bundle);

    private ServiceConnection bluetoothServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.i(TAG, "onServiceConnected()");
            DataSupport.deleteAll(DataUnitForSQL.class);
            bluetoothService = ((BluetoothService.MyBinder) service).getService();
            bluetoothService.setHandler(handler);
            getBluetoothAddressByDialog();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            Log.i(TAG,"onServiceDisconnected()");
            bluetoothService = null;
        }
    };

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
//            Log.i(TAG,"handleMessage()");
            updateUI(msg.what, msg.getData());
        }
    };

    protected void startTest() {
        this.getActivity().bindService(intentToTestService, testServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection testServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DataSupport.deleteAll(DataUnitForSQL.class);
            testService = ((TestService.MyBinder) service).getService();
            testService.setHandler(handler);
            testService.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            testService = null;
        }
    };
}