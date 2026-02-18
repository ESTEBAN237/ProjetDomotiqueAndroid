package com.mezazem.polyhome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

class DeviceLightAdapter(
    private val context: Context,
    private val devices: List<DataDevice>
) : BaseAdapter() {

    override fun getCount(): Int = devices.size

    override fun getItem(position: Int): Any = devices[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_lights, parent, false)

        val device = devices[position]

        val textLightName = view.findViewById<TextView>(R.id.textLightName)
        val textLightPower = view.findViewById<TextView>(R.id.textLightPower)

        textLightName.text = device.id

        val powerValue = device.power?: 0

        val state = if (powerValue > 0) {
            "Etat : Allumé"
        } else {
            "Etat : Éteinte"
        }

        textLightPower.text = state


        val btnOn = view.findViewById<Button>(R.id.buttonTurnOn)
        val btnOff = view.findViewById<Button>(R.id.buttonTurnOff)

        btnOn.setOnClickListener {
            (context as LumieresActivity).sendCommand(device.id, "TURN ON")
        }

        btnOff.setOnClickListener {
            (context as LumieresActivity).sendCommand(device.id, "TURN OFF")
        }


        return view
    }
}
