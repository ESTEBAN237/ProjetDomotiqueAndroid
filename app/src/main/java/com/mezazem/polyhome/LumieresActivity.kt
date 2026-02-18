package com.mezazem.polyhome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtp2.Api

class LumieresActivity : AppCompatActivity() {

    lateinit var devices: ArrayList<DataDevice>
    lateinit var devicesLight: ArrayList<DataDevice>
    lateinit var adapter: DeviceLightAdapter
    lateinit var listViewLights: ListView
    lateinit var token: String
    var houseId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lumieres)

        token = intent.getStringExtra("token") ?: ""
        houseId = intent.getIntExtra("houseId", 0)

        listViewLights = findViewById(R.id.listViewLights)

        devices = ArrayList()
        devicesLight = ArrayList()

        adapter = DeviceLightAdapter(this, devicesLight)
        listViewLights.adapter = adapter
        switchToOrders()
        getDevices()
        manageAllLights()
    }

    fun onSuccessDevices(code: Int, data: DataDeviceResponse?) {
        if (code == 200 && data != null) {
            devices.clear()
            devices.addAll(data.devices)
            trierLesDonnees()
        }
    }

    fun trierLesDonnees() {
        devicesLight.clear()

        for (device in devices) {
            if (device.type.lowercase() == "light") {
                devicesLight.add(device)
            }
        }

        runOnUiThread {
            adapter.notifyDataSetChanged()
        }
    }

    fun getDevices() {
        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices"
        Api().get<DataDeviceResponse>(url, ::onSuccessDevices, token)
    }
    fun switchToOrders(){
        val btnVolets = findViewById<ImageButton>(R.id.btnVolets)
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        btnMenu.setOnClickListener {
            val intent = Intent(
                this, MenuActivity::class.java
            )
            intent.putExtra("token", token)
            intent.putExtra("houseId", houseId)
            startActivity(intent)
        }
        btnVolets.setOnClickListener {
            val intent = Intent(
                this, VoletsActivity::class.java
            )
            intent.putExtra("token", token)
            intent.putExtra("houseId", houseId)
            startActivity(intent)
        }
    }

    fun onSuccessCommand(code: Int) {

        when (code) {
            200 -> {
                println("Commande envoyée avec succès")
                getDevices()
            }
            403 -> {
                println("Accès interdit")
            }
            else -> {
                println("Erreur serveur")
            }
        }
    }

    fun manageAllLights() {

        val btnAllOff = findViewById<Button>(R.id.buttonAllOff)

        btnAllOff.setOnClickListener {

            for (device in devicesLight.toList()) {
                sendCommand(device.id, "TURN OFF")
            }
        }
    }


    fun sendCommand(deviceId: String, command: String) {

        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices/$deviceId/command"
        val body = DataCommand(command)
        Api().post<DataCommand>(url, body, ::onSuccessCommand, token)
    }
}
