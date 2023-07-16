package com.example.cryptodata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.TextView
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val url = "https://coinranking1.p.rapidapi.com/coins?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h&tiers%5B0%5D=1&orderBy=marketCap&orderDirection=desc&limit=50&offset=0"
        val textView= findViewById<TextView>(R.id.textView1)
        val listView= findViewById<ListView>(R.id.listView)
        // Create an empty adapter initially
        callApi(url) { x ->
            if (x != null) {

                listView.adapter= Custom_adapter(this, x)
//                for (coin in x) {
//                    textView.text= coin.toString()
//                    Log.d("coin", coin.toString())
//                }
            } else {
                Log.d("err", "error occured")
                // Handle the case when x is null or the API call failed
            }
        }




    }



    private fun callApi(url: String, callback: (List<CoinsItem>?) -> Unit) {

//        Sorry for showing my API KEY
        val client = OkHttpClient()
        var textView= findViewById<TextView>(R.id.textView1)
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("X-RapidAPI-Key", "273f74489fmsh5eaa5aca7dac36cp1d24c4jsn7ebe1b17a6c4")
            .addHeader("X-RapidAPI-Host", "coinranking1.p.rapidapi.com")
            .build()
            var data= "x"

        GlobalScope.launch(Dispatchers.IO){
            val response = client.newCall(request).execute()
//            val data= response.body.toString()
            data= response.body?.string() ?: "Not Found"

            val gson= Gson().fromJson<Response>(data, Response::class.java)
            val x= gson.data?.coins

//            Log.d("gson resp", x.toString())
            withContext(Dispatchers.Main) {
                callback(x as List<CoinsItem>?)
            }
//            Log.d("New Response", data)

        }
//        Log.d("Response", data)
    }
}