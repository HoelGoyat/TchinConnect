package org.esaip.tchinconnect;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import org.esaip.tchinconnect.OnBluetoothDeviceClickedListener;
import org.esaip.tchinconnect.models.Card;
import org.esaip.tchinconnect.models.DAO.AppDatabase;
import org.esaip.tchinconnect.models.DAO.CardDao;
import org.esaip.tchinconnect.models.DAO.UserDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SettingActivity extends AppCompatActivity implements OnBluetoothDeviceClickedListener {

    // Element de la vue activity_main
    private Button scan;
    private RecyclerView recyclerView;

    private CardDao cardDao;


    // Constante pour permissions
    private static final int PERMISSION_REQUEST_BLUETOOTH = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private static final int PERMISSION_REQUEST_LOCATION = 3;

    // InitData variable

    private Handler mHandler;
    private List<BluetoothDevice> mBluetoothDeviceList = new ArrayList<>();
    private MyBluetoothDeviceAdapter mBluetoothDeviceAdapter;

    // Temps de scan en S
    private static final long SCAN_PERIOD = 1000 * 4;


    // Variable pour la gestion de la détection des appareils
    private MyBluetoothScanCallBack mBluetoothScanCallBack = new MyBluetoothScanCallBack();


    // Variables pour afficher les messages
    static Toast toast = null;

    // Variable device pour initier la connexion
    private String deviceNameToConnect;
    private String deviceAddressToConnect;


    // Service Bluetooth
    private BluetoothLeService mBluetoothLeService;

    // Statut de la connection : déconnecté par défaut
    private String mConnectionState = BluetoothLeService.ACTION_GATT_DISCONNECTED;

    // Queue des messages input
    private Queue<byte[]> sendQueue = new LinkedList<>();

    //Nb de paquet à recevoir
    int nbPaquet=0;
    String Receive = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "tchinDB").allowMainThreadQueries().build();
        cardDao = db.cardDao();
        initView();
        requestPermissions();
        initData();
        initService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initReceiver();
        scanLeDevice(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("MainActivity", "unregisterReceiver()");
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_BLUETOOTH) {
            if (resultCode == Activity.RESULT_CANCELED) {
                showMsg("enable_bluetooth_error");
                return;
            } else if (resultCode == Activity.RESULT_OK) {
                if (mBluetoothDeviceList != null) {
                    mBluetoothDeviceList.clear();
                }
                scanLeDevice(true);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Initialiser les éléments de la vue
    private void initView(){
        this.scan = (Button) findViewById(R.id.scan);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    // Demander toutes les permissions pour le bluetooth
    private void requestPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Bluetooth permissions
            if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_ADMIN, android.Manifest.permission.BLUETOOTH},
                        PERMISSION_REQUEST_BLUETOOTH);
            }

            // Storage permission
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_STORAGE);
            }

            // Location permissions
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_LOCATION);
            }
        }
    }


    // Permet de vérifier si la demande de permission a bien fonctionnée
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_BLUETOOTH:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Bluetooth permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case PERMISSION_REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case PERMISSION_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }


    // Initialise la liste et lance le scan
    private void initData(){
        mHandler = new Handler();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        mBluetoothDeviceAdapter = new MyBluetoothDeviceAdapter(mBluetoothDeviceList, this);
        recyclerView.setAdapter(mBluetoothDeviceAdapter);


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBluetoothDeviceList != null) {
                    mBluetoothDeviceList.clear();
                }
                scanLeDevice(true);
            }
        });
    };


    // Lance le scan de SCAN_PERIOD si true, sinon arrête le scan
    private void scanLeDevice(boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BluetoothScan.stopScan();
                }
            }, SCAN_PERIOD);
            Log.i("Main activity", "Start le scan");
            BluetoothScan.startScan(true, mBluetoothScanCallBack, this);
        } else {
            BluetoothScan.stopScan();
        }
    }

    // Gère les évènements de Scan Bluetooth
    private class MyBluetoothScanCallBack implements BluetoothScan.BluetoothScanCallBack {
        @Override
        public void onLeScanInitFailure(int failureCode) {
            Log.i("MainActivity", "onLeScanInitFailure()");
            switch (failureCode) {
                case BluetoothScan.SCAN_FEATURE_ERROR :
                    showMsg("scan_feature_error");
                    break;
                case BluetoothScan.SCAN_ADAPTER_ERROR :
                    showMsg("scan_adapter_error");
                    break;
                default:
                    showMsg("unKnow_error");
            }
        }

        @Override
        public void onLeScanInitSuccess(int successCode) {
            Log.i("MainActivity", "onLeScanInitSuccess()");
            switch (successCode) {
                case BluetoothScan.SCAN_BEGIN_SCAN :
                    Log.i("MainActivity","successCode : " + successCode);
                    break;
                case BluetoothScan.SCAN_NEED_ENADLE :
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, PERMISSION_REQUEST_BLUETOOTH);
                    break;
                case BluetoothScan.AUTO_ENABLE_FAILURE :
                    showMsg("auto_enable_bluetooth_error");
                    break;
                default:
                    showMsg("unKnow_error");
            }
        }

        // Réeption des devices bluetooth détectés, on garde ceux ayant un nom défini
        @Override
        public void onLeScanResult(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if(!mBluetoothDeviceList.contains(device) && device != null) {
                if(!TextUtils.isEmpty(device.getName())){
                    mBluetoothDeviceList.add(device);
                    mBluetoothDeviceAdapter.notifyDataSetChanged();
                }


            }
        }
    }


    // Connection au device via un click
    @Override
    public void onBluetoothDeviceClicked(String name, String address) {

        Log.i("MainActivity","Attempt to connect device : " + name + "(" + address + ")");
        deviceNameToConnect = name;
        deviceAddressToConnect = address;

        if (mBluetoothLeService != null) {
            if (mBluetoothLeService.connect(deviceAddressToConnect)) {
                showMsg("Attempt to connect device : " + name);
                mConnectionState = BluetoothLeService.ACTION_GATT_CONNECTING;

            }
        }else{
            Log.w("MainActivity", "mBluetoothLeService is null");
        }
    };


    // Evenements liés au service de connexion BLE
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("MainActivity","Unable to initialize Bluetooth");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    // Initialisation du service de connexion BLE
    private void initService() {
        Log.i("MainActivity", "initService()");

        if (mBluetoothLeService == null) {
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        }
    }

    private void initReceiver() {
        Log.i("MainActivity", "initReceiver()");
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);

        registerReceiver(mGattUpdateReceiver, intentFilter);
    }


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.i("MainActivity", "ACTION_GATT_CONNECTED!!!");
                showMsg("Connected device ..");

                mConnectionState = BluetoothLeService.ACTION_GATT_CONNECTED;

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.i("MainActivity", "ACTION_GATT_DISCONNECTED!!!");
                showMsg("disconnected");
                mConnectionState = BluetoothLeService.ACTION_GATT_DISCONNECTED;

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                mBluetoothLeService.getSupportedGattServices();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                final byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);


                //showMsg("Got string : " + new String(data));

                if (data != null && data.length > 0) {
                    final StringBuilder stringBuilder = new StringBuilder(data.length);
                    for (byte byteChar : data) {
                        char b = (char) byteChar;
                        stringBuilder.append(b);
                    }

                    Log.i("MainActivity", "Get string : " + stringBuilder.toString());

                    if(stringBuilder.toString().equals("init")){
                        //showMsg(stringBuilder.toString());
                        Card userCard = cardDao.getCardUser();
                        String ProfilToSend = "$" + userCard.getName() +
                                "#" + userCard.getSurname() +
                                "#" + userCard.getEmail() +
                                "#" + userCard.getJob() +
                                "#" + userCard.getJobDescription() +
                                "#" + userCard.getImage();
                        sendQueue.add(ProfilToSend.getBytes());
                        btSendBytes();
                    }

                    if(nbPaquet >0){
                        if((nbPaquet-1) ==0){
                            Receive += stringBuilder.toString();
                            // Traitement du message
                            traitementMessage(Receive);
                            Receive = "";
                            //Indique la récupération des profils
                            sendQueue.add("end".getBytes());
                            btSendBytes();
                        }else{
                            nbPaquet --;
                            Receive += stringBuilder.toString();
                        }
                    }

                    if(stringBuilder.toString().startsWith("!")){
                        nbPaquet = Integer.parseInt(stringBuilder.toString().split("!")[1]);
                    }

                    if(nbPaquet == 0){
                        showMsg("Carte de visite recu depuis le Verre Connecté !");
                        SettingActivity.this.setResult(Activity.RESULT_OK, new Intent());
                        finish(); // retour a l'acceuil
                    }



//                    if(stringBuilder.toString().startsWith("!")){
//                        // Traitement de la donnée
//                        showMsg(stringBuilder.toString());
//                        String profils = msg.split("!")[0];
//                        if(traitementMessage(stringBuilder.toString())){
//
//                            //Déconnexion
//                        }
//                    }
                }

            }
        }
    };



    // Send message to the device
    public void btSendBytes() { // byte[] data
        if (mBluetoothLeService != null &&
                mConnectionState.equals(BluetoothLeService.ACTION_GATT_CONNECTED)) {

            // Set the write complete listener, permet de gérer l'envoie
            mBluetoothLeService.setOnWriteCompleteListener(new BluetoothLeService.OnWriteCompleteListener() {
                @Override
                public void onWriteComplete(BluetoothGattCharacteristic characteristic, int status) {
                    Log.d("MyApp", "Write complete with status " + status);
                    if(!sendQueue.isEmpty()){
                        btSendBytes();
                    }
                }
            });

            //Log.d("MyApp", "Byte array value: " + Arrays.toString(data));
            // mBluetoothLeService.writeCharacteristic(data);
            if(!sendQueue.isEmpty()) {
                mBluetoothLeService.writeCharacteristic(sendQueue.peek());
                sendQueue.remove();
            }
        }
    }

    // Affiche un msg sur l'écran de l'application
    public static void showMsg(String msg) {
        try {
            if (toast == null) {
                toast = Toast.makeText(MyApplication.context(), msg, Toast.LENGTH_SHORT);
            } else {
                toast.setText(msg);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean traitementMessage(String msg){
        try{

            Log.i("MainActivity", "Get string : " + msg);

            String[] profils_array = msg.split("\\$");
            profils_array = Arrays.copyOfRange(profils_array, 1, profils_array.length);
            Profil profil;
            Card cardReceived;
            for (String elements : profils_array) {
                String[] element = elements.split("#");
                profil = new Profil(element[0], element[1], element[2], element[3], element[4], element[5]);
                cardReceived = new Card(null,element[0], element[1], element[2], element[3], element[4], element[5]);
                cardDao.insert(cardReceived);
                Log.i("MainActivity", "First Name : " + profil.getFirstName());
                Log.i("MainActivity", "Picture : " + profil.getPicture());

            }
            return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}