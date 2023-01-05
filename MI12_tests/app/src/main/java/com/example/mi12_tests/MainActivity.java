package com.example.mi12_tests;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 456;
    private static final int REQUEST_ENABLE_LOCATION = 457;
    private Button btnScan;

    public BluetoothAdapter bluetoothAdapter;
    public static boolean mScanning = false;

    private List<com.example.mi12_tests.Measurement> measurements = new ArrayList<>();

    private DeviceScanActivity ScanActivity;
    private DeviceScanActivity ScanActivity2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Si pas de module (d'interface) bluetooth sur le périphérique ...
        if ( bluetoothAdapter == null ) {
            Toast.makeText(
                    this,
                    "Bluetooth not supported on this deveice",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // Si le bluetooth n'est pas activé, on propose de l'activer
        if ( ! bluetoothAdapter.isEnabled() ) {
            // Demande à activer l'interface bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // On vérifie les autorisations pour la localisation
        //     Cette permission est requise sur les versions récentes d'Android, car on
        //     peut se localiser par triangulation avec différentes bornes bluetooth en
        //     fonction de la puissance des signaux reçus.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ) {
                requestPermissions(
                        new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                        REQUEST_ENABLE_LOCATION );
            }
        }
        // On enregistre un gestionnaire d'événements sur le bouton
        btnScan = findViewById( R.id.btnScan );
        //btnScan.setOnClickListener( btnScanLister );

        btnScan.setOnClickListener(new View.OnClickListener() {
            private BluetoothReceiver bluetoothReceiver = null;

            @Override
            public void onClick(View view) {
                ScanActivity = new DeviceScanActivity();
                ScanActivity.bluetoothAdapter = bluetoothAdapter;
                ScanActivity.makeBleInstance();
                mScanning = true;
                ScanActivity.scanLeDevice(true);
            }
        });




        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null){
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();
            Toast.makeText(this, R.string.turn_on_ble, Toast.LENGTH_SHORT).show();
        }

        //实例化扫描类
        ScanActivity = new DeviceScanActivity();
        ScanActivity.bluetoothAdapter = bluetoothAdapter;
        //实例化Ble类
        ScanActivity.makeBleInstance();
        //启动扫描
//        ScanActivity.scanLeDevice(true);
        //

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked MEASURE RSSI Button");
                // Ask the user to input the point coordinates
                EditText xInput = findViewById(R.id.x_input);
                EditText yInput = findViewById(R.id.y_input);
                double x = Double.parseDouble(xInput.getText().toString());
                double y = Double.parseDouble(yInput.getText().toString());
                System.out.println("Points : " + x + " " + y);
                Point point = new Point(x, y);
                // Ask the user to input the device address
                EditText addressInput = findViewById(R.id.address_input);
                String address = addressInput.getText().toString();
                System.out.println("Adresse : " + address);
                mScanning = true;
//                menu_scann.setVisibility[
//                ]2    (View.GONE);
                ScanActivity.scanLeDevice(true);
                // Create a measurement with the point and device address
                //Measurement measurement = new Measurement(point, address);
                // Add the measurement to the list
                //measurements.add(measurement);
            }
        });

    }
}