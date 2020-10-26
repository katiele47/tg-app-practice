package com.tgapp.mytranscribeglass;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

/* This service performs GATT connection and broadcasts */
public class BluetoothLeService extends Service {
    //Bluetooth Helper constants
    private static final int REQUEST_ENABLE_BT = 9; //requestCode parameter
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean mScanning;
    private Handler handler = new Handler();
    private static final long SCAN_PERIOD = 10000;

    // My code (app = server)
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private String bluetoothDeviceAddress;
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
    public final static String ACTION_GATT_SERVICES_ADDED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_ADDED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public final static String DEVICE_DOES_NOT_SUPPORT_UART =
            "com.nordicsemi.nrfUART.DEVICE_DOES_NOT_SUPPORT_UART";


//    public final static UUID TRANSCRIBE_GLASS_UUID = UUID.fromString("");

    int startMode;       // indicates how to behave if the service is killed
    IBinder binder;      // interface for clients that bind
    boolean allowRebind; // indicates whether onRebind should be used

    @Override
    public void onCreate() {
        // The service is being created
//        bluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        Log.i(TAG, "Ble service started");
        Toast.makeText(this, "BluetoothLe service starting", Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY; //mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return binder;
//        throw new UnsupportedOperationException("Not yet implemented");

    }
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return allowRebind;
    }

//    @Override
//    public void onRebind(Intent intent) {
//        // A client is binding to the service with bindService(),
//        // after onUnbind() has already been called
//    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        Log.i(TAG, "Ble service stopped");
        Toast.makeText(this, "BluetoothLe service destroyed", Toast.LENGTH_SHORT).show();
    }
}




    /**
     * Initialize the GATT server instance
     */
//    private void startServer() {
//        //open Gatt Server
//        bluetoothGattServer = bluetoothManager.openGattServer(this, gattServerCallback);
//        if (bluetoothGattServer == null) {
//            Log.w(TAG, "Unable to create GATT server");
//            return;
//        }
//    }
//    /**
//     * STEP 1 & 2: Initializes a reference to the local Bluetooth adapter.
//     */
//    public boolean initBluetoothLE(Activity context) {
//        /* STEP 1: Set up BLE --> check whether the glass supports BLE */
//        this.activity = context;
//        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//            Toast.makeText(context, "BLE is not supported on this device", Toast.LENGTH_SHORT).show();
//            context.finish();
//        }
//        /*  STEP 2: Ensure that BLE is enabled on device that supports BLE --> if disabled, request user to enable Bluetooth */
//        if (bluetoothManager == null) {
//            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
//            if (bluetoothManager ==  null) {
//                Log.e(TAG, "Unable to initialize BluetoothManager.");
//                return false;
//            }
//        }
//        // Get Bluetooth adapter
//        bluetoothAdapter = bluetoothManager.getAdapter();
//        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
//            Log.i(TAG, "Unable to obtain a BluetoothAdapter. Now requesting user to enable BLE");
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }
//        else {
//            Toast.makeText(activity, "Bluetooth is not available or disabled", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//    /**
//     * Connects to the GATT client - Bluetooth LE device.
//     *
//     * @param address The device address of the destination device.
//     *
//     * @return Return true if the connection is initiated successfully. The connection result
//     *         is reported asynchronously through the
//     *         {@code BluetoothGattServerCallback#onConnectionStateChange(android.bluetooth.BluetoothGattServer, int, int)}
//     *         callback.
//     */
//    public boolean connectClient(final String address) {
//        if (bluetoothAdapter == null || address == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
//            return false;
//        }
//
//        // Previously connected device.  Try to reconnect.
//        if (bluetoothDeviceAddress != null && address.equals(bluetoothDeviceAddress)
//                && bluetoothGattServer != null) {
//            Log.d(TAG, "Trying to use an existing bluetoothGattServer for connection.");
//            //not sure
//            /*
//                A BluetoothGattServerCallback#onConnectionStateChange callback will be
//                invoked when the connection state changes as a result of this function
//                bluetoothGattServer.connect(...,...)
//            */
//            if (bluetoothGattServer.connect(device, false)) {
//                connectionState = STATE_CONNECTING;
//                Log.d(TAG, "Server is connecting to remote device");
//                return true;
//            } else {
//                return false;
//            }
//        }
//
//        final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
//        if (device == null) {
//            Log.w(TAG, "Device not found.  Unable to connect.");
//            return false;
//        }
//        // We want to directly connect to the device, so we are setting the autoConnect
//        // parameter to false.
//        //------ NOT APPLICABLE -----
//        /*
//            b = device.connectGatt(this, false, mGattCallback);
//            Log.d(TAG, "Trying to create a new connection.");
//            mBluetoothDeviceAddress = address;
//            mConnectionState = STATE_CONNECTING;
//        */
//        return true;
//    }
//    /**
//     * Disconnect with a remove device
//     */
//    private void disconnectServer() {
//        if (bluetoothAdapter ==  null || bluetoothGattServer == null) {
//            Log.w(TAG, "GATT server was not initially initiated");
//        }
//        bluetoothGattServer.cancelConnection(device);
//    }
//    /**
//     * Shut down the GATT server.
//     */
//    private void stopServer() {
//        if (bluetoothGattServer == null) {
//            return;
//        }
//        Log.w(TAG, "bluetoothGattServer closed");
//        bluetoothDeviceAddress = null;
//        bluetoothGattServer.close();
//    }
//    /*@Override
//    public IBinder onBind(Intent intent) {
//        return mBinder;
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        // After using a given device, you should make sure that BluetoothGatt.close() is called
//        // such that resources are cleaned up properly.  In this particular example, close() is
//        // invoked when the UI is disconnected from the Service.
//        close();
//        return super.onUnbind(intent);
//    }*/
//
//    /* -------------------------------------------------------------- */
//
//    // Implements callback methods for GATT events that the app cares about.  For example,
//    // connection change and services added --> what service?
//
//    /* BluetoothGattServerCallback methods defined by the BLE API*/
//    private final BluetoothGattServerCallback gattServerCallback =
//            new BluetoothGattServerCallback() {
//                //overriden methods
//                @Override
//                public void onConnectionStateChange (BluetoothDevice device,
//                                                     int status,
//                                                     int newState) {
//                    String intentAction;
//                    if(newState == BluetoothProfile.STATE_CONNECTED) {
//                        intentAction = ACTION_GATT_CONNECTED;
//                        connectionState = STATE_CONNECTED;
//                        broadcastUpdate(intentAction);
//                        Log.i(TAG, "Connected to GATT server.");
//                        Log.i(TAG, "Attempting to start adding services:" );
////                                bluetoothGattServer.addService());
//                        //how do I know what services to add?
//                    }
//                    else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                        intentAction = ACTION_GATT_DISCONNECTED;
//                        connectionState = STATE_DISCONNECTED;
//                        broadcastUpdate(intentAction);
//                        Log.i(TAG, "Disconnected from GATT server.");
//                    }
//                };
//                /*
//                    A method analogous to onServicesDiscoverd of BluetoothGatt
//                    Indicates whether a local service has been added successfully
//                */
//                @Override
//                public void onServiceAdded(int status, BluetoothGattService service) {
//                    if (status == BluetoothGatt.GATT_SUCCESS){
//                        Log.w(TAG, "bluetoothGattServer = " + bluetoothGattServer);
//                        broadcastUpdate(ACTION_GATT_SERVICES_ADDED);
//                    }
//                    else {
//                        Log.w(TAG, "onServiceAdded received: " + status);
//                    }
//                }
//                /*
//                    A remote client has requested to read a local characteristic
//                 */
//
//                @Override
//                public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
//                    //Send a response to a read request to a remote device
//                    //--- my draft code, not totally sure what status, offs
//                    int status =  1;
//                    byte[] data = characteristic.getValue();
//                    boolean resSent = bluetoothGattServer.sendResponse(device, requestId, status, offset, data);
//                    if(resSent == true) {
//                        broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
//                    }
//                }
//            };
//
//    //populate data to app
//    private void broadcastUpdate(final String action) {
//        final Intent intent = new Intent(action);
//        sendBroadcast(intent);
//    }
//
//    private void broadcastUpdate(final String action,
//                                 final BluetoothGattCharacteristic characteristic) {
//        final Intent intent = new Intent(action);
//        /* if(UUID_CAPTION_TRANSCRIBE.equals(characteristic.getUuid()))) {
//              to be coded.....
//                }
//                 For all other profiles, writes the data formatted in HEX.
//         */
//        final byte[] data = characteristic.getValue();
//        if (data != null && data.length > 0) {
//            final StringBuilder stringBuilder = new StringBuilder(data.length);
//            for (byte byteChar : data)
//                stringBuilder.append(String.format("%02X ", byteChar));
//            intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
//                    stringBuilder.toString());
//        }
//        sendBroadcast(intent);
//    }
//    // Handles various events fired by the Service.
//    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//            switch (action) {
//                case BluetoothLeService.ACTION_GATT_CONNECTED:
////                connected = true;
////                updateConnectionState(R.string.connected);
////                invalidateOptionsMenu();
//                case BluetoothLeService.ACTION_GATT_DISCONNECTED:
////                connected = false;
////                updateConnectionState(R.string.disconnected);
////                invalidateOptionsMenu();
////                clearUI();
//                case BluetoothLeService.ACTION_GATT_SERVICES_ADDED:
//                    // Show all the supported services and characteristics on the
//                    // user interface.
////                displayGattServices(bluetoothLeService.getSupportedGattServices());
//                case BluetoothLeService.ACTION_DATA_AVAILABLE:
////                    displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
//            }
//        }
//    };
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//}
