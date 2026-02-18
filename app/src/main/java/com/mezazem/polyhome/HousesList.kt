package com.mezazem.polyhome

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtp2.Api

class HousesList: AppCompatActivity() {
   lateinit var token : String;
   lateinit var  houseList: ArrayList<DataHouse> ;
    lateinit var listView: ListView;
    lateinit var adapter: HouseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_houses)
        token = intent.getStringExtra("Token") ?: ""
        houseList = ArrayList()
        listView = findViewById(R.id.listHouses)
        adapter = HouseAdapter(this, houseList)
        listView.adapter = adapter
        showHouses()
        onClickHouse()
    }

    public fun onClickHouse(){
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedHouse = houseList[position]
            val intent = Intent(this, HouseDetails::class.java)
            intent.putExtra("HouseId", selectedHouse.houseId)
            intent.putExtra("statut",selectedHouse.owner)
            intent.putExtra("Token", token)
            startActivity(intent)
        }
    }

    public fun onSuccessShowHouses(responseCode: Int, housesList: Array<DataHouse> ?) {
        if (responseCode == 200 && housesList != null) {
            houseList.clear()
            houseList.addAll(housesList)
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }

    public fun showHouses(){
        Api().get<Array<DataHouse>>("https://polyhome.lesmoulinsdudev.com/api/houses", ::onSuccessShowHouses, token)
    }
}