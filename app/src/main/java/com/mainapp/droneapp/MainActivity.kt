package com.mainapp.droneapp

import android.annotation.SuppressLint
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mainapp.droneapp.databinding.DroneControlBinding
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.coroutines.*
import java.io.IOException
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: DroneControlBinding

    private lateinit var myBluetooth: MyBluetooth

    private val _tag = MainActivity::class.qualifiedName
    private val _myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
    private val REQUEST_BLUETOOTH_PERMISSION = 100
    private var joystickJob: Job? = null
    private var throttleJob: Job? = null
    private val THROTTLE_INCREMENT = 5
    private val MAX_THROTTLE = 100
    private val MIN_THROTTLE = 0
   // private val surfaceView= binding.surfaceView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(_tag, "Starting...")

        // Check if Bluetooth permission is granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startBluetoothServer()
        } else {
            // Request Bluetooth permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH),
                REQUEST_BLUETOOTH_PERMISSION
            )
        }

        myBluetooth = MyBluetooth(this)

        binding = DroneControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Power Switch
        val pwerswtch = binding.powerswtch
        pwerswtch.setOnCheckedChangeListener { _, isChecked ->
            val command = if (isChecked) "on" else "off"
            myBluetooth.writeData(command)
        }

        /*Battery Percentage
        val batlvl: TextView = binding.batterylvl
        val receivedBatteryData = myBluetooth.readbattery()
        val batteryLevelText = getString(R.string.battery_level, receivedBatteryData)
        batlvl.text = batteryLevelText

         */

        /*Video Switch
        val switch = binding.videoSwitch
        switch.setOnClickListener{
            if(switch.isChecked){

                binding.surfaceView.visibility= View.VISIBLE
            }
            else{
                binding.surfaceView.visibility= View.GONE
                surfaceView.stop()
            }
        }

        val surfaceView= binding.surfaceView
        val uri = Uri.parse("rtsps://10.0.1.3/test.sdp")
        val username = "admin"
        val password = "secret"
        surfaceView.init(uri, username, password)
        surfaceView.start(requestVideo = true, requestAudio = false)
        */

        //Map
        binding.fabMap.setOnClickListener {
            val mapIntent = Intent(this, MapsActivity::class.java)
            startActivity(mapIntent)
           // surfaceView.stop()
        }

        //connecting to the drone
        binding.droneConnect.setOnClickListener {
            myBluetooth.connect()
        }

        //Joystick Controls
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)



        val joystickViewLeft: JoystickView = binding.joystickViewLeft
        val joystickViewRight: JoystickView = binding.joystickViewRight

        joystickViewLeft.setOnMoveListener { angle, _ ->
            joystickJob?.cancel()
            joystickJob = CoroutineScope(Dispatchers.Default).launch {
                val editor = sharedPreferences.edit()
                when (angle) {
                    in 120..210 -> {
                        editor.putString("yaw_left", "W")
                        editor.putString("yaw_right", "")
                        myBluetooth.writeData("W")
                    }
                    in 330..359, in 0..60 -> {
                        editor.putString("yaw_left", "")
                        editor.putString("yaw_right", "E")
                        myBluetooth.writeData("E")
                    }
                    else -> {
                        editor.putString("yaw_left", "")
                        editor.putString("yaw_right", "")
                    }
                }
                editor.apply()

                val yawLeft = sharedPreferences.getString("yaw_left", "")
                val yawRight = sharedPreferences.getString("yaw_right", "")
                Log.d("MainActivity", "Yaw Left: $yawLeft, Yaw Right: $yawRight")
            }
        }

        joystickViewRight.setOnMoveListener { angle, _ ->
            joystickJob?.cancel()
            joystickJob = CoroutineScope(Dispatchers.Default).launch {
                val editor = sharedPreferences.edit()
                when (angle) {
                    in 46..135 -> {
                        editor.putString("pitch_up", "1")
                        editor.putString("roll_left", "")
                        editor.putString("pitch_down", "")
                        editor.putString("roll_right", "")
                        myBluetooth.writeData("1")

                    }
                    in 136..225 -> {
                        editor.putString("pitch_up", "")
                        editor.putString("roll_left", "4")
                        editor.putString("pitch_down", "")
                        editor.putString("roll_right", "")
                        myBluetooth.writeData("4")
                    }
                    in 226..315 -> {
                        editor.putString("pitch_up", "")
                        editor.putString("roll_left", "")
                        editor.putString("pitch_down", "3")
                        editor.putString("roll_right", "")
                        myBluetooth.writeData("3")
                    }
                    in 316..359, in 0..45 -> {
                        editor.putString("pitch_up", "")
                        editor.putString("roll_left", "")
                        editor.putString("pitch_down", "")
                        editor.putString("roll_right", "2")
                        myBluetooth.writeData("2")
                    }
                    else -> {
                        editor.putString("pitch_up", "")
                        editor.putString("roll_left", "")
                        editor.putString("pitch_down", "")
                        editor.putString("roll_right", "")
                    }
                }
                editor.apply()

                val pitchUp = sharedPreferences.getString("pitch_up", "")
                val rollLeft = sharedPreferences.getString("roll_left", "")
                val pitchDown = sharedPreferences.getString("pitch_down", "")
                val rollRight = sharedPreferences.getString("roll_right", "")
                Log.d(
                    "MainActivity",
                    "Pitch Up: $pitchUp, Roll Left: $rollLeft, Pitch Down: $pitchDown, Roll Right: $rollRight"
                )
            }
        }

        //Throttle Controls
        val btnIncreaseThrottle: FloatingActionButton = binding.throttleup
        val btnDecreaseThrottle: FloatingActionButton = binding.throttledown

        btnIncreaseThrottle.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                throttleJob?.cancel()
                throttleJob = CoroutineScope(Dispatchers.Default).launch {
                    while (true) {
                        increaseThrottle()
                        delay(100)
                    }
                }
            } else if (event.action == MotionEvent.ACTION_UP) {
                throttleJob?.cancel()
            }
            true
        }

        btnDecreaseThrottle.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                throttleJob?.cancel()
                throttleJob = CoroutineScope(Dispatchers.Default).launch {
                    while (true) {
                        decreaseThrottle()
                        delay(100)
                    }
                }
            } else if (event.action == MotionEvent.ACTION_UP) {
                throttleJob?.cancel()
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        joystickJob?.cancel()
        throttleJob?.cancel()
    }

    private fun increaseThrottle() {
        var throttle = sharedPreferences.getInt("throttle", 0)
        if (throttle < MAX_THROTTLE) {
            throttle += THROTTLE_INCREMENT
            if (throttle > MAX_THROTTLE)
                throttle = MAX_THROTTLE
            sharedPreferences.edit().putInt("throttle", throttle).apply()
        }
        Log.d("MainActivity", "Throttle Increased: $throttle")
        myBluetooth.writeData("N")
    }

    private fun decreaseThrottle() {
        var throttle = sharedPreferences.getInt("throttle", 0)
        if (throttle > MIN_THROTTLE) {
            throttle -= THROTTLE_INCREMENT
            if (throttle < MIN_THROTTLE)
                throttle = MIN_THROTTLE
            sharedPreferences.edit().putInt("throttle", throttle).apply()
        }
        Log.d("MainActivity", "Throttle Decreased: $throttle")
        myBluetooth.writeData("S")
    }

    private fun startBluetoothServer() {
    val adapter = BluetoothAdapter.getDefaultAdapter()
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_CONNECT
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Request Bluetooth permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
            REQUEST_BLUETOOTH_PERMISSION
        )
    } else {
        val serverSocket: BluetoothServerSocket? =
            adapter?.listenUsingRfcommWithServiceRecord(
                "BluetoothServer",
                UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
            )
        var socket: BluetoothSocket? = null
        try {
            println("Waiting for connection...")
            socket = serverSocket?.accept()
            println("Connection established")
             // Handle the Bluetooth connection
            // ...
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                socket?.close()
                serverSocket?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBluetoothServer()
            } else {
                // Handle permission denied
            }
        }
    }

}
