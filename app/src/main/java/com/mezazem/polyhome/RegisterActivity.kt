package com.mezazem.polyhome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtp2.Api

class RegisterActivity  : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        createAccount()
    }
    public fun onSuccessRegister(responseCode: Int) {
        if (responseCode==200){
            val intent = Intent(
                this,
                MainActivity::class.java
            )
            startActivity(intent)
        }
    }

    public fun createAccount(){
        val nom = findViewById<EditText>(R.id.etLogin);
        val  password = findViewById<EditText>(R.id.etPassword);

        val createAccount = findViewById<Button>(R.id.btnRegister);

        createAccount.setOnClickListener{
        val nomValue = nom.text.toString() ;
        val passwordValue =password.text.toString();
            val dataRegister = DataRegister(
                login = nomValue,
                password=passwordValue
            )
            Api().post<DataRegister>("https://polyhome.lesmoulinsdudev.com/api/users/register", dataRegister, ::onSuccessRegister)
        }
    }
}