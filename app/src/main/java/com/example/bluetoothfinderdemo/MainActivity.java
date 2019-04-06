package com.example.bluetoothfinderdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView textView;
    Button button;
    BluetoothAdapter bluetoothAdapter;
    ArrayList<String>bluetoothDevices = new ArrayList<>();
    ArrayList<String>bluetoothAddresses = new ArrayList<>();

    ArrayAdapter arrayAdapter;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                textView.setText("finished");
                button.setEnabled(true);
            }else if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));

                if (!bluetoothAddresses.contains(address)){
                    bluetoothAddresses.add(address);
                    String deviceString = "";
                    if (name == null || name.equals("")){
                        deviceString = address + " - RSSI" + rssi + "dBm";
                    }else{
                        deviceString = name+ " - RSSI" + rssi + "dBm";
                    }
                        bluetoothDevices.add(deviceString);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };
    public void searchClicked (View view){
        textView.setText("Searching...");
        button.setEnabled(false);
        bluetoothDevices.clear();
        bluetoothAddresses.clear();
        bluetoothAdapter.startDiscovery();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bluetoothDevices);

        listView.setAdapter(arrayAdapter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);


    }
}
