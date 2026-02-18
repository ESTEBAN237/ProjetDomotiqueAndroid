package com.mezazem.polyhome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtp2.Api

class MenuActivity : AppCompatActivity() {

    lateinit var token: String
    lateinit var devices: ArrayList<DataDevice>
    var houseId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        token = intent.getStringExtra("token") ?: ""
        houseId = intent.getIntExtra("houseId", 0)
        devices = ArrayList()
        switchToLights()
        setupButtons()
        getDevices()
    }

    fun onSuccessDevices(code: Int, data: DataDeviceResponse?) {
        if (code == 200 && data != null) {
            devices.clear()
            devices.addAll(data.devices)
        }
    }

    fun getDevices() {
        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices"
        Api().get<DataDeviceResponse>(url, ::onSuccessDevices, token)
    }

    fun switchToLights() {
        val btnLumieres = findViewById<ImageButton>(R.id.btnLumieres)
        val btnVolets = findViewById<ImageButton>(R.id.btnVolets)

        btnLumieres.setOnClickListener {
            val intent = Intent(this, LumieresActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("houseId", houseId)
            startActivity(intent)
        }

        btnVolets.setOnClickListener {
            val intent = Intent(this, VoletsActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("houseId", houseId)
            startActivity(intent)
        }
    }

    fun sendCommand(deviceId: String, command: String) {
        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices/$deviceId/command"
        val body = DataCommand(command)
        Api().post<DataCommand>(url, body, ::onSuccessCommand, token)
    }

    fun onSuccessCommand(code: Int) {
         getDevices()
    }

    private fun getLights(): List<DataDevice> =
        devices.filter { it.type.equals("light", ignoreCase = true) }

    private fun getShutters(): List<DataDevice> =
        devices.filter { it.type.contains("rolling", ignoreCase = true) || it.type.contains("shutter", ignoreCase = true) }

    private fun getGarage(): DataDevice? =
        devices.find { it.type.contains("garage", ignoreCase = true) }

    fun setupButtons() {

        val buttonDay = findViewById<Button>(R.id.buttonDay)
        val buttonNight = findViewById<Button>(R.id.buttonNight)

        val buttonLightsOff = findViewById<Button>(R.id.buttonLightsOff)

        val buttonOpenBlinds = findViewById<Button>(R.id.buttonOpenBlinds)
        val buttonCloseBlinds = findViewById<Button>(R.id.buttonCloseBlinds)

        val buttonOpenGarage = findViewById<Button>(R.id.buttonOpenGarage)
        val buttonCloseGarage = findViewById<Button>(R.id.buttonCloseGarage)

        buttonDay.setOnClickListener {
            for (light in getLights()) {
                sendCommand(light.id, "TURN OFF")
            }
            for (shutter in getShutters()) {
                sendCommand(shutter.id, "OPEN")
            }
        }

        buttonNight.setOnClickListener {
            for (shutter in getShutters()) {
                sendCommand(shutter.id, "CLOSE")
            }
            getGarage()?.let { garage ->
                sendCommand(garage.id, "CLOSE")
            }
        }

        buttonLightsOff.setOnClickListener {
            for (light in getLights()) {
                sendCommand(light.id, "TURN OFF")
            }
        }

        buttonOpenBlinds.setOnClickListener {
            for (shutter in getShutters()) {
                sendCommand(shutter.id, "OPEN")
            }
        }

        buttonCloseBlinds.setOnClickListener {
            for (shutter in getShutters()) {
                sendCommand(shutter.id, "CLOSE")
            }
        }

        buttonOpenGarage.setOnClickListener {
            getGarage()?.let { garage ->
                sendCommand(garage.id, "OPEN")
            }
        }

        buttonCloseGarage.setOnClickListener {
            getGarage()?.let { garage ->
                sendCommand(garage.id, "CLOSE")
            }
        }
    }
}
