package com.mezazem.polyhome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class UserAdapter(
    private val context: Context,
    private val users: ArrayList<DataUser>
) : BaseAdapter() {

    override fun getCount() = users.size

    override fun getItem(position: Int) = users[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_user, parent, false)

        val user = users[position]

        val tvLogin = view.findViewById<TextView>(R.id.tvLogin)
        val tvUserRole = view.findViewById<TextView>(R.id.tvUserRole)

        tvLogin.text = user.userLogin
        tvUserRole.text = if (user.owner == 1) "Propriétaire" else "Utilisateur"

        return view
    }
}