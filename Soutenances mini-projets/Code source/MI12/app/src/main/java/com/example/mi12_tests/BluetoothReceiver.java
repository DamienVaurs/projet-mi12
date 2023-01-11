package com.example.mi12_tests;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BluetoothReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Un récupère le périphérique bluetooth détecté durant le scan
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            // Et on affiche ses informations dans un toast et dans la fenêtre LogCat
            @SuppressLint("MissingPermission") String message = device.getName() + "-" + device.getAddress();
            System.out.println("test");
            System.out.println(message);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            Log.i("DebugBluetooth", message);
        }
    }
}
