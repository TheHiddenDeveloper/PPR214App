package com.mainapp.droneapp
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.UUID

class MyBluetooth(private val appCompatActivity: AppCompatActivity) {
    private var bluetoothAdapter: BluetoothAdapter
    private lateinit var btSocket : BluetoothSocket
    private val deviceAddress ="00:22:09:01:13:0A"
    private val _myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
    private var requestBluetooth = appCompatActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            //granted
        }else{
            //deny
        }
    }
    private val requestMultiplePermissions =
        appCompatActivity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
            }
        }
    init {
        val bluetoothManager = appCompatActivity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    }
    private fun requestBTPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH))
        }
        else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }
    }
    private var _isConnected : Boolean = false
    val isConnected : Boolean
        get() {
            _isConnected = this::btSocket.isInitialized && btSocket.isConnected
            return _isConnected
        }
    fun connect(): Boolean {
        val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
        Toast.makeText(appCompatActivity.applicationContext, "Connecting...", Toast.LENGTH_SHORT).show()
        if (ActivityCompat.checkSelfPermission(
                appCompatActivity,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                appCompatActivity.applicationContext,
                "BLUETOOTH_CONNECT permission is not granted! Please allow, and try again.",
                Toast.LENGTH_LONG
            ).show()
            requestBTPermissions()
            return false
        }
        Toast.makeText(
            appCompatActivity.applicationContext,
            "Connecting to ... ${device.name} mac: ${device.uuids?.get(0)} address: ${device.address}",
            Toast.LENGTH_SHORT
        ).show()
        bluetoothAdapter.cancelDiscovery()
        try {
            btSocket = device.createRfcommSocketToServiceRecord(_myUUID)
            btSocket.connect()
            Toast.makeText(appCompatActivity.applicationContext, "Connection made.", Toast.LENGTH_SHORT).show()
            return true
        } catch (e: IOException) {
            try {
                btSocket.close()
            } catch (e2: IOException) {
                Toast.makeText(appCompatActivity.applicationContext, "Unable to end the connection.", Toast.LENGTH_SHORT).show()
                return false
            }
            Toast.makeText(appCompatActivity.applicationContext, "Socket creation failed.", Toast.LENGTH_SHORT).show()
            return false
        }
    }
    fun writeData(data: String): Boolean {
        if (!isConnected)
            return false
        var outStream = btSocket.outputStream
        try {
            outStream = btSocket.outputStream
        } catch (e: IOException) {
            return false
        }
        val msgBuffer = data.toByteArray()
        try {
            outStream.write(msgBuffer)
        } catch (e: IOException) {
            return false
        }
        return true
    }
    /*fun receiveData() {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Request Bluetooth permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                REQUEST_BLUETOOTH_PERMISSION
            )
            // Handle the case where the permission is not granted yet
            return
        } else {
            val serverSocket: BluetoothServerSocket? = adapter?.listenUsingRfcommWithServiceRecord(
                "BluetoothServer",
                UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
            )
            if (serverSocket == null) {
                // Handle the case where the server socket is null
                return
            }
            var socket: BluetoothSocket? = null
            try {
                println("Waiting for connection...")
                socket = serverSocket.accept()
                println("Connection established")
                // Handle the Bluetooth connection
                // ...
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    socket?.close()
                    serverSocket.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }*/
     //private val REQUEST_BLUETOOTH_PERMISSION = 1
}
