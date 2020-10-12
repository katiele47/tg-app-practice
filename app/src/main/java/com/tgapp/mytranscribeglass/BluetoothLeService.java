package com.tgapp.mytranscribeglass;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.UUID;

public class BluetoothLeService extends Service {

    // my code (app = server)
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device;

    private final static String TAG = BluetoothLeService.class.getSimpleName();

    // Inherited BluetoothProfile constants
    private int connectionState = STATE_DISCONNECTED;//arbitrary variable
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

//  public final static UUID UUID_CAPTION_TRANSCRIBE =
//       UUID.fromString(SampleGattAttributes.<something>);

    //BluetoothGattServerCallback methods defined by the BLE API
    private final BluetoothGattServerCallback gattServerCallback =
            new BluetoothGattServerCallback() {
                //overriden methods
                @Override
                public void onConnectionStateChange (BluetoothDevice device,
                                                     int status,
                                                     int newState) {
                    String intentAction;
                    if(newState == BluetoothProfile.STATE_CONNECTED) {
                        intentAction = ACTION_GATT_CONNECTED;
                        connectionState = STATE_CONNECTED;
                        broadcastUpdate(intentAction);
                        Log.i(TAG, "Connected to GATT server.");
//                        Log.i(TAG, "Attempting to start service discovery:" +
//                                bluetoothGatt.discoverServices());
                    }
                    else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        intentAction = ACTION_GATT_DISCONNECTED;
                        connectionState = STATE_DISCONNECTED;
                        broadcastUpdate(intentAction);
                        Log.i(TAG, "Disconnected from GATT server.");
                    }
                };

                @Override
                public void onCharacteristicWriteRequest (BluetoothDevice device,
                                                          int requestId,
                                                          BluetoothGattCharacteristic characteristic,
                                                          boolean preparedWrite,
                                                          boolean responseNeeded,
                                                          int offset,
                                                          byte[] value) {
                    if (/* condition */ 1+1 == 2) {
                        // broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                    }
                }
            };

    //connect to BLE client devices
    BluetoothGattServer bluetoothGattServer = bluetoothManager.openGattServer(this, gattServerCallback);
    boolean connectToGatt = device.connect(this, false);

    //populate data to app
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
//        if(UUID_CAPTION_TRANSCRIBE.equals(characteristic.getUuid()))) {
//
//        }
        //to be coded.....
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
