package com.mezazem.polyhome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtp2.Api

class VoletsActivity : AppCompatActivity() {

    lateinit var devices: ArrayList<DataDevice>
    lateinit var devicesBlinds: ArrayList<DataDevice>

    lateinit var adapter: DeviceBlindAdapter
    lateinit var listViewBlinds: ListView

    lateinit var token: String
    var houseId: Int = 0

    var garageDeviceId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volets)

        token = intent.getStringExtra("token") ?: ""
        houseId = intent.getIntExtra("houseId", 0)

        devices = ArrayList()
        devicesBlinds = ArrayList()

        listViewBlinds = findViewById(R.id.listViewBlinds)
        adapter = DeviceBlindAdapter(this, devicesBlinds)
        listViewBlinds.adapter = adapter

        switchToOrdersView()
        getDevices()
        setupGarageButtons()
        manageAllVolets()
    }

    fun switchToOrdersView() {
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        val btnLumiere = findViewById<ImageButton>(R.id.btnLumieres)

        btnLumiere.setOnClickListener {
            val intent = Intent(this, LumieresActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("houseId", houseId)
            startActivity(intent)
        }

        btnMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("houseId", houseId)
            startActivity(intent)
        }
    }

    fun onSuccessDevices(code: Int, data: DataDeviceResponse?) {
        if (code == 200 && data != null) {
            for (d in data.devices) {
                println("Device ${d.id} opening = ${d.opening}")
            }
            devices.clear()
            devices.addAll(data.devices)
            trierLesDonnees()
        }
    }

    fun getDevices() {
        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices"
        Api().get<DataDeviceResponse>(url, ::onSuccessDevices, token)
    }

    fun trierLesDonnees() {

        devicesBlinds.clear()

        for (device in devices) {

            if (device.type.contains("rolling", true)) {
                devicesBlinds.add(device)
            }

            if (device.type.contains("garage", true)) {

                garageDeviceId = device.id

                runOnUiThread {

                    val textGarageName = findViewById<TextView>(R.id.textGarageName)
                    val textGarageOpening = findViewById<TextView>(R.id.textView2)

                    textGarageName.text = device.id
                    val state = if (device.opening == 0) {
                        "Etat : Fermé"
                    } else {
                        "Etat : Ouvert"
                    }

                    textGarageOpening.text = state

                }
            }

        }
        runOnUiThread {
            adapter.notifyDataSetChanged()
        }
    }
    fun onSuccessCommand(code: Int) {

        listViewBlinds.postDelayed({
            getDevices()
        }, 7000)
    }

    fun sendCommand(deviceId: String, command: String) {

        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices/$deviceId/command"
        val body = DataCommand(command)

        Api().post<DataCommand>(url, body, ::onSuccessCommand, token)
    }

    fun setupGarageButtons() {

        val btnOpen = findViewById<Button>(R.id.buttonOpenGarage)
        val btnClose = findViewById<Button>(R.id.buttonCloseGarage)
        val btnStop = findViewById<Button>(R.id.buttonStopGarage)

        btnOpen.setOnClickListener {
            garageDeviceId?.let {
                sendCommand(it, "OPEN")
            }
        }

        btnClose.setOnClickListener {
            garageDeviceId?.let {
                sendCommand(it, "CLOSE")
            }
        }

        btnStop.setOnClickListener {
            garageDeviceId?.let {
                sendCommand(it, "STOP")
            }
        }

    }

    public fun manageAllVolets(){

        val btnAllVoletsOn = findViewById<Button>(R.id.buttonAllOn2)
        val btnAllVoletsOff = findViewById<Button>(R.id.buttonAllOff2)

        btnAllVoletsOn.setOnClickListener {

            for (device in devicesBlinds.toList()) {
                sendCommand(device.id, "OPEN")
            }
        }

        btnAllVoletsOff.setOnClickListener {

            for (device in devicesBlinds.toList()) {
                sendCommand(device.id, "CLOSE")
            }
        }
    }
}
