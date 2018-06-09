package fruitbasket.com.bodyfit.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fruitbasket.com.bodyfit.Conditions;
import fruitbasket.com.bodyfit.R;
import fruitbasket.com.bodyfit.analysis.ExerciseAnalysisTask;
import fruitbasket.com.bodyfit.analysis.GroupExerciseScore;
import fruitbasket.com.bodyfit.analysis.SingleExerciseAnalysis;
import fruitbasket.com.bodyfit.data.DataSet;
import fruitbasket.com.bodyfit.data.DataUnit;
import fruitbasket.com.bodyfit.data.DataUnitForSQL;
import fruitbasket.com.bodyfit.data.StorageData;


public class BluetoothService extends Service {

    private static final String TAG = "BluetoothService";
    private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String DEVICE_NAME = "Smart Glove";
    private static final UUID SMART_GLOVE_SERVICE_UUID = UUID.fromString("0000eb20-0000-1000-8000-00805f9b34fb");
    private static final UUID SMART_GLOVE_CHARACTERISTIC_UUID = UUID.fromString("0000eb21-0000-1000-8000-00805f9b34fb");

    byte[] dataBuf = new byte[32];
    volatile int lastId = 0;

    private double start,end;
    private boolean isBegin = false;

    private long itemsNumber = 0;//记录从蓝牙设备中已经读取的数据条目数量
    private int localItemsNumber = 0;//记录从蓝牙设备中已经读取的数据条目数量。但值过大时，它会被清0；
    private double startTime;//记录从蓝牙接收数据的起始时间
    private double localStartTime;//记录从蓝牙接收数据的起始时间，但会定量清0
    private double currentTime;
    private double itemsPreSecond;

    private Bundle bundle = new Bundle();
    private DataUnit dataUnit;
    private int loadSize = 0;
    private DataUnit[] dataUnits = new DataUnit[Conditions.MAX_SAMPLE_NUMBER];//5组数据

    private SingleExerciseAnalysis analysis = new SingleExerciseAnalysis();
    private GroupExerciseScore groupExerciseScore = new GroupExerciseScore();
    private ExecutorService processExecutor = Executors.newSingleThreadExecutor();//创建线程池

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private ArrayList<BluetoothDevice> deviceArrayList; //附近的蓝牙设备列表
    private ArrayList<String> deviceNameList;///
    private ArrayAdapter arrayAdapter;

    private String bluetoothAddress;

    private Handler handler;
    //private StorageData writeData = new StorageData();

    public BluetoothService() {
    }

    public static int getIntFromByteArrayRaw(byte[] data, int start, int len) {
        int val = 0;
        for (int i = start + len - 1; i >= start; i--) {
            val <<= 8;
            val += (data[i] & 0xFF);
        }
        return val;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceArrayList = new ArrayList<BluetoothDevice>();
        deviceNameList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_device_list_item, deviceNameList);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind()");
        turnOnBluetooth();
        startDiscovery();
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind()");
        disconnectToDevice();
        stopDiscovery();
        turnOffBluetooth();
        return super.onUnbind(intent);
    }

    public ArrayList<BluetoothDevice> getDeviceArrayList() {
        return deviceArrayList;
    }

    public ArrayAdapter getArrayAdapter() {
        return arrayAdapter;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void turnOnBluetooth() {
        Log.d(TAG, "turnOnBluetooth()");
        if (bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (bluetoothAdapter.isEnabled() == false) {
            ///应修改提示
            //若蓝牙没开，则会执行这里，请求用户开启蓝牙。
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void turnOffBluetooth() {
        Log.d(TAG, "turnOffBluetooth()");
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled() == false) {
            bluetoothAdapter.disable();
        }
    }

    public void startDiscovery() {
        Log.d(TAG, "startDiscovery()");
        if (bluetoothAdapter.isEnabled() == true && bluetoothAdapter.isDiscovering() == false) {
            deviceArrayList.clear();
            deviceNameList.clear();
            arrayAdapter.notifyDataSetChanged();

            //registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            //bluetoothAdapter.startDiscovery();
            bluetoothAdapter.startLeScan(leScanCallback);
        }
    }

    public void stopDiscovery() {
        if (bluetoothAdapter.isDiscovering() == true) {
            bluetoothAdapter.stopLeScan(leScanCallback);
            //unregisterReceiver(discoveryResult);
        }

    }

    public void connectToDevice() {
        if (bluetoothAddress != null) {
            final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(this.bluetoothAddress);
            if (device == null) {
                Log.w(TAG, "Device not found.  Unable to connect.");
                return;
            }

            mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
            Log.d(TAG, "Trying to create a new connection.");
        } else {
            Log.e(TAG, "bluetoothAddress==null.at connectToDevice()");
        }
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "Connected to GATT server.");
                Log.i(TAG, "Attempting to start service discovery");
                mBluetoothGatt.discoverServices();
                //mDataListener.onConnectStateChanged(true);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "Disconnected from GATT server.");
                //mDataListener.onConnectStateChanged(false);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService service = gatt.getService(SMART_GLOVE_SERVICE_UUID);
                if (service != null) {
                    BluetoothGattCharacteristic characteristic = service.getCharacteristic(SMART_GLOVE_CHARACTERISTIC_UUID);
                    if (characteristic != null) {
                        gatt.setCharacteristicNotification(characteristic, true);
                        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                                UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mBluetoothGatt.writeDescriptor(descriptor);
                    }
                }
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //mDataListener.onData(characteristic.getValue());
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (!isBegin){
                start = System.currentTimeMillis();
                isBegin = true;
            }
            byte[] data = characteristic.getValue();
            int id = getIntFromByteArrayRaw(data, 0, 2);
            if (id != ((lastId + 1) & 0xFFFF)) {
                Log.i(TAG, "--------------> last = " + lastId + ", new = " + id);
            }
            if ((id & 0x1) == 0) {
                System.arraycopy(data, 0, dataBuf, 0, data.length);
            } else if (id == ((lastId + 1) & 0xFFFF)) {

                System.arraycopy(data, 0, dataBuf, 16, data.length);
                dataUnit = new DataUnit(dataBuf);

                //储存数据到储存卡

                /*try {
                    writeData.outputData(dataUnit);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                if (loadSize < dataUnits.length - 1) {
                    dataUnits[loadSize] = dataUnit;

                    DataUnitForSQL dataUnitForSQL=new DataUnitForSQL(dataUnit.getTime(),dataUnit.getAx(),dataUnit.getAy(),
                            dataUnit.getAz(),dataUnit.getGx(),dataUnit.getGy(),dataUnit.getGz(),dataUnit.getMx(),
                            dataUnit.getMy(),dataUnit.getMz(),dataUnit.getP1(),dataUnit.getP2(),dataUnit.getP3());
                    dataUnitForSQL.save();

                    ++loadSize;

                } else {
                    dataUnits[loadSize] = dataUnit;

                    /*DataUnitForSQL dataUnitForSQL=new DataUnitForSQL(dataUnit.getTime(),dataUnit.getAx(),dataUnit.getAy(),
                            dataUnit.getAz(),dataUnit.getGx(),dataUnit.getGy(),dataUnit.getGz(),dataUnit.getMx(),
                            dataUnit.getMy(),dataUnit.getMz(),dataUnit.getP1(),dataUnit.getP2(),dataUnit.getP3());
                    dataUnitForSQL.save();*/

                    loadSize = 0;
                    DataSet dataSet = new DataSet(dataUnits);
                    end = System.currentTimeMillis();
                    Log.i(TAG,"BlueTeeth"+ (end - start) + "");
                    isBegin = false;
                    //这里analysis可能会产生一个处理延迟或处理缺失的问题
                    if (analysis.addToSet(dataSet) == false) {
                        //这里将数据的处理放到一个新的线程中
                        Log.e(TAG, "将数据的处理放到一个新的线程中");
                        ExerciseAnalysisTask exerciseAnalysisTask = new ExerciseAnalysisTask(analysis, groupExerciseScore, handler);
                        exerciseAnalysisTask.setContext(BluetoothService.this);
                        processExecutor.execute(exerciseAnalysisTask);
                    }
                }

                /*++itemsNumber;
                ++localItemsNumber;
                currentTime = System.currentTimeMillis();
                itemsPreSecond = localItemsNumber / ((currentTime - localStartTime) / 1000);///注意计算结果
                if (localItemsNumber > 500) {
                    localItemsNumber = 0;
                    localStartTime = currentTime;
                }

                //Log.d(TAG,"currentTime=="+currentTime);
                //Log.d(TAG,"startTime=="+startTime);
                //Log.d(TAG,"itemsNumber=="+itemsNumber);
                //Log.d(TAG,"itemsPreSecond=="+itemsPreSecond);

                Message message = new Message();
                message.what = Conditions.MESSAGE_BLUETOOTH_TEST;

                bundle.putDouble(Conditions.JSON_KEY_ITEMS_PRE_SECOND, itemsPreSecond);
                bundle.putDouble(Conditions.TIME, dataUnit.getTime());
                bundle.putDouble(Conditions.AX, dataUnit.getAx());
                bundle.putDouble(Conditions.AY, dataUnit.getAy());
                bundle.putDouble(Conditions.AZ, dataUnit.getAz());
                bundle.putDouble(Conditions.GX, dataUnit.getGx());
                bundle.putDouble(Conditions.GY, dataUnit.getGy());
                bundle.putDouble(Conditions.GZ, dataUnit.getGz());
                bundle.putDouble(Conditions.MX, dataUnit.getMx());
                bundle.putDouble(Conditions.MY, dataUnit.getMy());
                bundle.putDouble(Conditions.MZ, dataUnit.getMz());
                bundle.putDouble(Conditions.P1, dataUnit.getP1());
                bundle.putDouble(Conditions.P2, dataUnit.getP2());
                bundle.putDouble(Conditions.P3, dataUnit.getP3());
                message.setData(bundle);
                handler.sendMessage(message);*/
            }

            lastId = id;
        }
    };

    public void connectToDevice(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
        connectToDevice();
    }

    public void disconnectToDevice() {
        Log.i(TAG, "disconnectToDevice()");
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt = null;
        }
    }

    public class MyBinder extends Binder {
        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            String name = device.getName();
            if (name != null && name.equals(DEVICE_NAME)) {
                for (BluetoothDevice dev : deviceArrayList) {
                    if (dev.getAddress().equals(device.getAddress())) {
                        return;
                    }
                }
                deviceArrayList.add(device);
                deviceNameList.add(name);
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };
}
