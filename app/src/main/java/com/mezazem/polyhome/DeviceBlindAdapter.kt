package com.mezazem.polyhome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

class DeviceBlindAdapter(
    private val context: Context,
    private val devices: List<DataDevice>
) : BaseAdapter() {

    override fun getCount(): Int = devices.size

    override fun getItem(position: Int): Any = devices[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_volets, parent, false)

        val device = devices[position]

        val btnOpen = view.findViewById<Button>(R.id.buttonOpen)
        val btnClose = view.findViewById<Button>(R.id.buttonClose)
        val btnStop = view.findViewById<Button>(R.id.buttonStop)

        btnOpen.setOnClickListener {
            (context as VoletsActivity).sendCommand(device.id, "OPEN")

            val textBlindName = view.findViewById<TextView>(R.id.textBlindName)
            val textOpening = view.findViewById<TextView>(R.id.textView)
            textBlindName.text = device.id
            val state = if (device.opening == 0) {
                "Etat : Fermé"
            } else {
                "Etat : Ouvert"
            }

            textOpening.text = state
        }

        btnClose.setOnClickListener {
            (context as VoletsActivity).sendCommand(device.id, "CLOSE")
        }

        btnStop.setOnClickListener {
            (context as VoletsActivity).sendCommand(device.id, "STOP")
        }

        val textBlindName = view.findViewById<TextView>(R.id.textBlindName)
        val textOpening = view.findViewById<TextView>(R.id.textView)

        textBlindName.text = device.id

        val state = if (device.opening == 0) {
            "Etat : Fermé"
        } else {
            "Etat : Ouvert"
        }

        textOpening.text = state

        return view
    }
}
