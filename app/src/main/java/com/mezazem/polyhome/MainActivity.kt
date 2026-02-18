package com.mezazem.polyhome

import android.content.Intent
import android.os.Bundle
import android.util.JsonToken
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtp2.Api

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
            createAccount()
            login()

    }

    public fun createAccount(){
        val createAccount = findViewById<TextView>(R.id.tvRegister);
        createAccount.setOnClickListener {
            val intent = Intent(
                this, RegisterActivity::class.java
            );
            startActivity(intent)
        }
    }
    public fun onSuccessConnexion(responseCode: Int, response: AuthResponse?) {
        runOnUiThread {
            when (responseCode) {
                200 -> {
                    if ( response?.token != null) {
                        val intent = Intent(this, HousesList::class.java)
                        intent.putExtra("Token", response.token)
                        startActivity(intent)
                    }
                }
                404 -> {
                    Toast.makeText(this, "Identifiants incorrects", Toast.LENGTH_LONG).show()
                }
                400 -> {
                    Toast.makeText(this, "Champs invalides", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "Erreur serveur : $responseCode", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    public fun login(){
        val login = findViewById<EditText>(R.id.etLogin);
        val password = findViewById<EditText>(R.id.etPassword);
        val connexion = findViewById<Button>(R.id.btnLogin);

        connexion.setOnClickListener {

            val loginValue = login.text.toString()
            val passwordValue = password.text.toString()

            val dataConnexion = DataConnexion(
                login = loginValue,
                password = passwordValue
            )
            Api().post<DataConnexion, AuthResponse>("https://polyhome.lesmoulinsdudev.com/api/users/auth", dataConnexion,::onSuccessConnexion)
        }

    }
}