package org.esaip.tchinconnect;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.UUID;

public class BluetoothLeService extends Service {

    // Constante pour indiquer le statut de la connection
    public final static String ACTION_GATT_CONNECTING = "action_gatt_connecting";
    public final static String ACTION_GATT_CONNECTED = "action_gatt_connected";
    public final static String ACTION_GATT_DISCONNECTED = "action_gatt_disconnected";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "action_gatt_services_discovered";
    public final static String ACTION_DATA_AVAILABLE = "action_data_available";
    public final static String EXTRA_DATA = "extra_data";

    // Constante pour indiquer l'UUID du device avec lequel on veut se connecter
    public final static UUID UUID_NOTIFY =
            UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8");

    // Characteristic du device à lire
    public BluetoothGattCharacteristic mNotifyCharacteristic;

    // Gestionnaire Bluetooth pour obtenir le BluetoothAdapter
    private BluetoothManager mBluetoothManager;

    // Adapteur Bluetooth utilisé pour gérer les connexions BLE
    private BluetoothAdapter mBluetoothAdapter;

    // Adresse Mac du device à connecter
    private String mBluetoothDeviceAddress;

    // Objet qui représente la connexion BLE
    private BluetoothGatt mBluetoothGatt;

    // Utilisé pour lier le service à d'autres composants de l'application
    private final IBinder mBinder = new LocalBinder();

    // Class qui fournit une méthode pour obtenir une instance du service
    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent){return mBinder;}

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    // Initialise le gestionnaire et l'adaptateur Bluetooth.
    // Retourne true si l'initialisation est réussie, false sinon.
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e("BluetoothLeService", "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e("BluetoothLeService","Unable to obtain a BluetoothAdapter.");
            return false;
        }

        Log.i("BluetoothLeService", "Initialize BluetoothLeService success!");
        return true;
    }

    //Connection au device avec l'adresse MAC donnée.
    //Retourne true si la connexion a réussi, false sinon.
    public boolean connect(String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.e("BluetoothLeService","BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            //Log.w("BluetoothLeService","Trying to use an existing mBluetoothGatt for connection.");
            //return mBluetoothGatt.connect();
            // On reset le tout
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.e("BluetoothLeService","Device not found,Unable to connect.");
            return false;
        }
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.w("BluetoothLeService","Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        return true;
    }

    // Déconnexion au device
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e("BluetoothLeService","BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }
    //Ferme la connexion Bluetooth et libère les ressources utilisées par la connexion
    //Appeler la fonction disconnect() d'abord.
    public void close() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;;
        }
    }

    // Fournit des rappels pour les événements BLE,
    // tels que la découverte de services et la réception de notifications de characteristic.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {


        // Gère les événements de changement d'état de connexion
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                broadcastUpdate(ACTION_GATT_CONNECTED);
                Log.i("BluetoothLeService","Connected to GATT server.");
                mBluetoothGatt.discoverServices();
                Log.i("BluetoothLeService","Attempting to start service discovery:");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                broadcastUpdate(ACTION_GATT_DISCONNECTED);
                Log.w("BluetoothLeService","Disconnected from GATT server.");
            }
        }


        // Gère l'événement de découverte de services
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.e("BluetoothLeService","onServicesDiscovered received : " + status);
            }
        }

        // Gère l'événement de lecture de characteristic,
        // se produit si le device envoie des données
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i("BluetoothLeService","onCharacteristicRead()");
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        // Gère l'événement de modification de caractéristique
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            Log.i("BluetoothLeService","onCharacteristicChanged()");
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

        // Gère l'événement d'écriture de characteristic,
        // se produit lorsque l'application écrit des données sur une characteristic du device
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            Log.i("BluetoothLeService","onCharacteristicWrite()");
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (mOnWriteCompleteListener != null) {
                mOnWriteCompleteListener.onWriteComplete(characteristic, BluetoothGatt.GATT_SUCCESS);
            }
        }
    };

    // Envoie une action de diffusion en broadcast
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    // Envoie une action de diffusion avec des données supplémentaires
    // stocké dans la characteristic
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        final byte[] data = characteristic.getValue();
        intent.putExtra(EXTRA_DATA, data);
        sendBroadcast(intent);
    }

    // Récupère la liste des services Bluetooth Gatt pris en charge par le device.
    // Verifie si le service UUID_NOTIFY est disponible.
    // Active les notifications puis écrit un descripteur à la characterisitque
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) {
            return null;
        }

        List<BluetoothGattService> gattServices = mBluetoothGatt.getServices();

        for (BluetoothGattService gattService : gattServices) {
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                String uuid = gattCharacteristic.getUuid().toString();
                Log.i("BluetoothLeService","uuid : " + uuid);

                if(uuid.equalsIgnoreCase(UUID_NOTIFY.toString())){
                    mNotifyCharacteristic = gattCharacteristic;
                    mBluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);
                    Log.i("BluetoothLeService","setCharacteristicNotification : " + uuid);
                    UUID magic_uuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
                    BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(magic_uuid);
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    mBluetoothGatt.writeDescriptor(descriptor);
                }
            }
        }
        return gattServices;
    }

    // Ecrit les données data dans la characteristic
    public void writeCharacteristic(byte[] data) {
        mNotifyCharacteristic.setValue(data);
        mBluetoothGatt.writeCharacteristic(mNotifyCharacteristic);
    }


    private OnWriteCompleteListener mOnWriteCompleteListener;
    public interface OnWriteCompleteListener {
        void onWriteComplete(BluetoothGattCharacteristic characteristic, int status);
    }

    public void setOnWriteCompleteListener(OnWriteCompleteListener listener) {
        mOnWriteCompleteListener = listener;
    }
}
