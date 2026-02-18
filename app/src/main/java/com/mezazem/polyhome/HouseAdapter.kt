package com.mezazem.polyhome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class HouseAdapter(
    private val context: Context,
    private val houses: ArrayList<DataHouse>
) : BaseAdapter() {

    override fun getCount() = houses.size
    override fun getItem(position: Int) = houses[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_house, parent, false)

        val house = houses[position]

        val tvHouseId = view.findViewById<TextView>(R.id.tvHouseId)
        val tvTitleHouse = view.findViewById<TextView>(R.id.tvTitleHouse)
        val tvOwner = view.findViewById<TextView>(R.id.tvOwner)

        tvHouseId.text = house.houseId.toString()
        tvTitleHouse.text = "Maison"
        tvOwner.text = if (house.owner) "Propriétaire" else "Invité"

        return view
    }
}
