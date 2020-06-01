package com.ti.homeautomation;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class LockActivity extends AppCompatActivity {
    private String TAG = "BluetoothActivityTag";
    private TextView titleTextView;
    private ProgressBar progressCircle;
    private ListView listView;
    private Switch aSwitch;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket mBTSocket;
    private Handler mHandler; // Our main handler that will receive callback notifications
    private ConnectThread mConnectThread; // bluetooth background worker thread to send and receive data
    private final int REQUEST_ENABLE_BT = 420;
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 69;
    private final int CONNECTING_STATUS = 13;
    private final int MESSAGE_READ = 33;
    private int btEnableAttempts = 0;
    private UUID MY_UUID = null;
    private UUID HARDCODED_UID = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        titleTextView = (TextView) findViewById(R.id.textViewTitle);
        progressCircle = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listView);
        aSwitch = (Switch) findViewById(R.id.door);
        getUUID();

        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    DealWithMessage(readMessage);
                }

                if (msg.what == CONNECTING_STATUS) {
                    if (msg.arg1 == 1) {
                        Toast.makeText(LockActivity.this, "Connected to Device: " + (String) (msg.obj) + "\nSending test message", Toast.LENGTH_LONG).show();
                        listView.setVisibility(View.GONE);
                        aSwitch.setVisibility(View.VISIBLE);
                    }
                    else
                        Toast.makeText(LockActivity.this, "Connection Failed", Toast.LENGTH_LONG).show();
                }
            }
        };

        bluetoothSetup();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (aSwitch.isChecked()) {
                    mConnectThread.setDoorRequest(true);
                } else {
                    mConnectThread.setDoorRequest(false);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnectThread.cancel();
    }

    protected void DealWithMessage(String messageString) {
        messageString = messageString.replaceAll("\\D+","");
        int message = Integer.parseInt(messageString);
        Toast.makeText(this, Integer.toString(message), Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    private void getUUID() {
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if(Build.VERSION.SDK_INT >= 26) {
            try {
                MY_UUID = UUID.fromString(tManager.getImei()); // unsupported Android 10...
            } catch(Exception e) {
                MY_UUID = HARDCODED_UID; // urat, dar soarta
            }
        }
        else {
            MY_UUID = UUID.fromString(tManager.getDeviceId());
        }
        Log.d(TAG, "UUID set to: " + MY_UUID.toString());
    }

    private void setLoading(boolean isItLoading) {
        if(isItLoading) {
            listView.setVisibility(View.INVISIBLE);
            progressCircle.setVisibility(View.VISIBLE);
            titleTextView.setText("Please wait while getting paired devices");
        }
        else {
            listView.setVisibility(View.VISIBLE);
            progressCircle.setVisibility(View.INVISIBLE);
            titleTextView.setText("Choose the device from the list");
        }
    }

    private void getAdapter() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private void enableAdapter() {
        btEnableAttempts++;
        if(btEnableAttempts <= 1) {
            titleTextView.setText("Please enable bluetooth.");
        }
        else {
            titleTextView.setText("I  SAID  P L E A S E  enable  BLUETOOTH. It's a must...");
        }
        Intent bluetoothEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(bluetoothEnableIntent, REQUEST_ENABLE_BT);
    }

    private void bluetoothSetup() {
        setLoading(true);
        getAdapter();
        if(bluetoothAdapter == null) {
            listView.setVisibility(View.INVISIBLE);
            progressCircle.setVisibility(View.INVISIBLE);
            titleTextView.setText("N-ai Bluetooth, n-ai parte");
            Toast.makeText(this, "Pare rau daca e emulator..", Toast.LENGTH_LONG).show();
            return;
        }
        if(!bluetoothAdapter.isEnabled()) {
            enableAdapter();
        }
        else {
            getPairedDevices();
        }
    }

    private void getPairedDevices() {
        setLoading(true);
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        Log.d(TAG, "Got devices: " + devices.toString());
        List deviceNames = new ArrayList<BluetoothDevice>();
        for(BluetoothDevice device : devices) {
            Log.d(TAG, device.getName());
            deviceNames.add(device.getName() + '\n' + device.getAddress());
        }
        ArrayAdapter<BluetoothDevice> bluetoothDeviceArrayAdapter = new ArrayAdapter<BluetoothDevice>(this, R.layout.listview_item, deviceNames);
        listView.setAdapter(bluetoothDeviceArrayAdapter);
        listView.setOnItemClickListener(new ListViewClickListener());
        setLoading(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode != RESULT_OK) {
                enableAdapter();
            } else {
                getPairedDevices();
            }
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        UUID devUuid = MY_UUID;
        BluetoothSocket bt = null;
        for(ParcelUuid puid : device.getUuids()) {
            devUuid = puid.getUuid();

            boolean ok = false;
            try {
                bt = device.createInsecureRfcommSocketToServiceRecord(devUuid);
//                bt = device.createRfcommSocketToServiceRecord(devUuid);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection", e);
            }
            try{
                bt.connect();
                ok = true;
            } catch(Exception e) {
                Log.e(TAG, "Failed connecting to socket with UUID " + devUuid);
                Log.e(TAG, e.getMessage());
            }

            if(ok) {
                break;
            }
        }
        return bt;
    }

    class ListViewClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(LockActivity.this, "Connecting...", Toast.LENGTH_SHORT).show();
            // Get the device MAC address, which is the last 17 chars in the View
            final String name = ((TextView) view).getText().toString().split("\n")[0].replace("\u0000", "");
            final String address = ((TextView) view).getText().toString().split("\n")[1].replace("\u0000", "");
            Log.d(TAG, "clicked MAC address: " + address);

            // Spawn a new thread to avoid blocking the GUI one
            new Thread()
            {
                public void run() {
                    boolean fail = false;
                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                    try {
                        mBTSocket = createBluetoothSocket(device);
                        Log.d(TAG, "Created socket");
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                    // Establish the Bluetooth socket connection.
                    Log.d(TAG, "Creating socket");
                    try {
                        if(!mBTSocket.isConnected()) {
                            throw new IOException();
                        }
                        Log.d(TAG, "Connected to socket");
                    } catch (Exception e) {
                        Log.e(TAG, "Failed connecting to socket");
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                        try {
                            fail = true;
                            mBTSocket.close();
                        } catch (IOException e2) {
                            Log.e(TAG, "Failed closing socket");
                        }
                    }
                    if (!fail) {
                        mConnectThread = new ConnectThread(mBTSocket);
                        mConnectThread.start();
                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                                .sendToTarget();
                    }
                }
            }.start();
        }
    }

    private class ConnectThread extends Thread {
        private static final String TAG = "ConnectThread";
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private volatile boolean doorRequest, doorState;
        private Profil profil = Profil.getInstance();

        public ConnectThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        /* Call this from the main activity to send data to the remote device */
        private void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        private String read(int len) {
            byte[] bytes = new byte[len];
            int n = 0;
            try {
                while (n < len) {
                    int available = mmInStream.available();
                    if (available > 0) {
                        int ret = mmInStream.read(bytes, n, available);
                        if (ret < 0) {
                            break;
                        }
                        n += ret;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String(bytes);
        }

        public void setDoorRequest(boolean stare) {
            doorRequest = true;
            doorState = stare;
        }

        public boolean getDoorState() {
            return doorState;
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }

        public void run() {
            String resp;
            boolean stareUsa = false;

//            write("LOGIN_REQUEST#" + profil.username  + "#"+ profil.password + "#");
//            resp = read(20);

//            write("GET_DATA#DOOR#STATE#TIMEOUT#" + profil.username);
//            resp = read(1);
//            stareUsa = (Integer.parseInt(resp) != 0);

            Log.d(TAG, "Stare usa");
            aSwitch.setChecked(stareUsa);
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                if (doorRequest) {
                    if (doorState) {
//                        write("#SET_DOOR#OPEN#");
//                        resp = read(20);
                        Log.d(TAG, "Door must open");
                    } else {
//                        write("#SET_DOOR#CLOSE#");
//                        resp = read(25);
                        Log.d(TAG, "Door must close");
                    }
                    doorRequest = false;
                }
                SystemClock.sleep(100);
            }
        }
    }
}
