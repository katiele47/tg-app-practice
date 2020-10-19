package com.tgapp.mytranscribeglass;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.getSystemService;

public class BluetoothHelper {
    private static final int REQUEST_ENABLE_BT = 9; //requestCode parameter
    private BluetoothLeScanner bluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
    private boolean mScanning;
    private Handler handler = new Handler();
    private static final long SCAN_PERIOD = 10000;
    private Activity activity;
    public static final String TAG = BluetoothHelper.class.getSimpleName();
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
//    private LeDeviceListAdapter leDeviceListAdapter;


    /**
     * STEP 1 & 2: Initializes a reference to the local Bluetooth adapter.
     */
    public boolean initialize(Activity context) {
        /* STEP 1: Set up BLE --> check whether the glass supports BLE */
        this.activity = context;
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, "BLE is not supported on this device", Toast.LENGTH_SHORT).show();
            context.finish();
        }
        /*  STEP 2: Ensure that BLE is enabled on device that supports BLE --> if disabled, request user to enable Bluetooth */
        if (bluetoothManager == null) {
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager ==  null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        // Get Bluetooth adapter
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.i(TAG, "Unable to obtain a BluetoothAdapter. Now requesting user to enable BLE");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else {
            Toast.makeText(activity, "Bluetooth is not available or disabled", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * Connects to the GATT client - Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattServerCallback#onConnectionStateChange(android.bluetooth.BluetoothGattServer, int, int)}
     *         callback.
     */
    public boolean connect(final String address) {
        //see Nordic github, research how to connect server to client
        // and maybe put entire code in this file into BluetoothLeService
        // for easier reference

        //boolean connectToGatt = device.connect(this, false);
        return true;
    }

    /* STEP 3: Scan BLE devices and return results */
    public void scanLeDevice() {
        if (!mScanning) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                    Log.d("BLE scanning log","Stop scanning");
                }
            }, SCAN_PERIOD);

            mScanning = true;
            bluetoothLeScanner.startScan(leScanCallback);
            Log.d("BLE scanning log","Start scanning");
        } else {
            mScanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
            Log.d("BLE scanning log","Stop scanning");
        }
    }
    //return scan results using the BluetoothAdapter.LeScanCallback interface
    private ScanCallback leScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    /*leDeviceListAdapter.addDevice(result.getDevice());
                    leDeviceListAdapter.notifyDataSetChanged();*/
                }
            };

    //-------- old v2 code for classic bluetooth
//    private BluetoothAdapter.LeScanCallback leScanCallback =
//            new BluetoothAdapter.LeScanCallback() {
//                @Override
//                public void onLeScan(final BluetoothDevice device, int rssi,
//                                     byte[] scanRecord) {
//                    Log.d("btlog","scanned");
//                    final AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
//                    builderSingle.setTitle("Select Your Device");
//
//                    final List<BluetoothDevice> devices = new ArrayList<>();
//                    ArrayAdapter<BluetoothDevice> arrayAdapter = null;
//                    if (devices.size() < 1) {
//                        arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.select_dialog_singlechoice);
//                        arrayAdapter.add(device);
//                    } else {
//                        try {
//                            arrayAdapter.add(device);
//                            arrayAdapter.notifyDataSetChanged();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//
//                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Log.d("btlog","clicked");
//                            Log.d("btlog",String.valueOf(devices.size()));
//                            connectWithLEDevice(devices.get(which));
//                            dialog.dismiss();
//                        }
//                    });
//
//                    builderSingle.show();
//                }
//            };


}
