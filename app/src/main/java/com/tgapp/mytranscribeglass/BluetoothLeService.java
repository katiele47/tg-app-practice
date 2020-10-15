package com.tgapp.mytranscribeglass;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.UUID;

/* This service performs GATT connection and broadcasts */
public class BluetoothLeService extends Service {


    // my code (app = server)
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGattServer bluetoothGattServer;
    private Activity activity;
    private BluetoothDevice device;
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    // Inherited BluetoothProfile constants
    private int connectionState = STATE_DISCONNECTED;//arbitrary variable
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    /* These are self defined action constants that are to be assigned to intents*/
    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

/*
  public final static UUID UUID_CAPTION_TRANSCRIBE =
       UUID.fromString(SampleGattAttributes.<something>);
*/

    /**
     * Initialize the GATT server instance
     */
    private void startServer() {
        //open Gatt Server
        bluetoothGattServer = bluetoothManager.openGattServer(this, gattServerCallback);
        if (bluetoothGattServer == null) {
            Log.w(TAG, "Unable to create GATT server");
            return;
        }
    }
    /**
     * Shut down the GATT server.
     */
    private void stopServer() {
        if (bluetoothGattServer == null) return;
        bluetoothGattServer.close();
    }

    /* A BluetoothGattServerCallback#onConnectionStateChange callback will be invoked when the connection state changes as a result of this function*/
//    boolean connectToGatt = device.connect(this, false);

    /* BluetoothGattServerCallback methods defined by the BLE API*/
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




    //populate data to app
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        /* if(UUID_CAPTION_TRANSCRIBE.equals(characteristic.getUuid()))) {
              to be coded.....
                }
                 For all other profiles, writes the data formatted in HEX.
         */
        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for (byte byteChar : data)
                stringBuilder.append(String.format("%02X ", byteChar));
            intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
                    stringBuilder.toString());
        }
        sendBroadcast(intent);
    }

    // Handles various events fired by the Service.
    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
//                connected = true;
//                updateConnectionState(R.string.connected);
//                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
//                connected = false;
//                updateConnectionState(R.string.disconnected);
//                invalidateOptionsMenu();
//                clearUI();
            } else if (BluetoothLeService.
                    ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the
                // user interface.
//                displayGattServices(bluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
