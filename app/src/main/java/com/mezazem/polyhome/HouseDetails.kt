package com.mezazem.polyhome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtp2.Api

class HouseDetails : AppCompatActivity() {

    lateinit var token: String
    var houseId: Int = 0
    var owner: Boolean = false
    lateinit var users: ArrayList<DataUser>
    lateinit var listView: ListView
    lateinit var adapter: UserAdapter
    private var login: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = intent.getStringExtra("Token") ?: ""
        houseId = intent.getIntExtra("HouseId", 0)
        owner = intent.getBooleanExtra("statut", false)

        if (owner) {
            setContentView(R.layout.activity_users_proprietaire)
            login = findViewById(R.id.etUserLogin2)
        } else {
            setContentView(R.layout.activity_users_invite)
        }

        listView = findViewById(R.id.listUsers)

        users = ArrayList()
        adapter = UserAdapter(this, users)
        listView.adapter = adapter

        updateHouseInformation()
        getListUsers()
        manageHouse()

        if (owner) {
            addUser()
            removeUser()
        }
    }

    fun updateHouseInformation() {
        val id = findViewById<TextView>(R.id.tvHouseId)
        val houseStatut = findViewById<TextView>(R.id.tvOwner)

        id.text = houseId.toString()
        houseStatut.text = if (owner) "Propriétaire" else "Invité"
    }

    fun onSuccessGetUsers(responseCode: Int, responseData: Array<DataUser>?) {
        if (responseCode == 200 && responseData != null) {
            users.clear()
            users.addAll(responseData)
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun getListUsers() {
        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users"
        Api().get<Array<DataUser>>(url, ::onSuccessGetUsers, token)
    }

    fun onSuccessAddUser(responseCode: Int) {
        runOnUiThread {
            when (responseCode) {
                200 -> {
                    Toast.makeText(this, "Accès accordé !", Toast.LENGTH_SHORT).show()
                    getListUsers()
                    login?.text?.clear()
                }
                409 -> Toast.makeText(this, "Already Added to Users !", Toast.LENGTH_SHORT).show()
                400 -> Toast.makeText(this, "Incorrect Login, Try Again !", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this, "Sorry, you can't make this action !", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun addUser() {
        val confirm = findViewById<Button>(R.id.btnAddUser)

        confirm.setOnClickListener {
            val loginValue = login?.text?.toString()?.trim().orEmpty()
            if (loginValue.isEmpty()) {
                Toast.makeText(this, "Veuillez saisir un login", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginToSend = DataUserRequest(userLogin = loginValue)
            val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users"
            Api().post<DataUserRequest>(url, loginToSend, ::onSuccessAddUser, token)
        }
    }

    private fun onSuccessRemoveUser(code: Int) {
        runOnUiThread {
            when (code) {
                200 -> {
                    Toast.makeText(this, "Suppression réalisée !", Toast.LENGTH_SHORT).show()
                    login?.text?.clear()
                    getListUsers()
                }
                400 -> Toast.makeText(this, "Login incorrect", Toast.LENGTH_SHORT).show()
                403 -> Toast.makeText(this, "Interdit (pas propriétaire)", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this, "Erreur serveur ($code)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun removeUser() {
        val btnRemoveUser = findViewById<Button>(R.id.btnRemoveUser)

        btnRemoveUser.setOnClickListener {
            val loginValue = login?.text?.toString()?.trim().orEmpty()
            if (loginValue.isEmpty()) {
                Toast.makeText(this, "Veuillez saisir un login", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginToSend = DataUserRequest(userLogin = loginValue)
            val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users"
            Api().delete<DataUserRequest>(url, loginToSend, ::onSuccessRemoveUser, token)
        }
    }

    fun manageHouse() {
        val btnManageHouse = findViewById<Button>(R.id.btnManageHouse)
        btnManageHouse.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("houseId", houseId)
            startActivity(intent)
        }
    }
}
