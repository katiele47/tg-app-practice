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
//    private LeDeviceListAdapter leDeviceListAdapter;

    //STEP 1: Set up BLE --> check whether the glass supports BLE
    private void checkBtSupport(Activity context) {
        this.activity = context;
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, "BLE is not supported on this device", Toast.LENGTH_SHORT).show();
            context.finish();
        }
    }//
    //STEP 2: Ensure that BLE is enabled on device that supports BLE --> if disabled, request user to enable Bluetooth
    private void enableBt(Activity context) {
        // Get Bluetooth adapter
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else {
            Toast.makeText(activity, "Bluetooth is not available or disabled", Toast.LENGTH_SHORT).show();
        }
    }
    //STEP 3: Scan BLE devices and return results
    private void scanLeDevice() {
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
