package com.example.cryptodata

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class ShowCoinDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_coin_details)

        var coinIcon= findViewById<ImageView>(R.id.coinIcon)
        var coinDesc= findViewById<TextView>(R.id.coinDesc)
        var coinName= findViewById<TextView>(R.id.coinName)
        var coinMaxPrice= findViewById<TextView>(R.id.coinMaxPrice)
        var coinCurrPrice= findViewById<TextView>(R.id.coinCurrentPrice)

        var backbutton= findViewById<Button>(R.id.backButton)
        backbutton.setOnClickListener {
            val i= Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        val uuid= intent.getStringExtra("uuid")
        val url= "https://coinranking1.p.rapidapi.com/coin/"+uuid.toString()+"?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h"

        callApi(url) { x ->
            if (x != null) {

                coinMaxPrice.text = x.allTimeHigh?.price.toString()
                coinCurrPrice.text = x.price.toString()
                coinName.text= x.name.toString() + "( " + x.symbol.toString() + " )"
                coinDesc.text= x.description.toString()
                val imageExt=  x.iconUrl?.takeLast(3)

                if(imageExt!= "svg")
                    Picasso.get().load(x.iconUrl.toString()).into(coinIcon)
                else {
                    GlideToVectorYou.justLoadImage(this, Uri.parse(x.iconUrl.toString()), coinIcon)
                }
            } else {
                Log.d("err", "error occured")
            }
        }

    }

    private fun callApi(url: String,  callback: (Coin?) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("X-RapidAPI-Key", "273f74489fmsh5eaa5aca7dac36cp1d24c4jsn7ebe1b17a6c4")
            .addHeader("X-RapidAPI-Host", "coinranking1.p.rapidapi.com")
            .build()
        var dataresp= "x"
        GlobalScope.launch(Dispatchers.IO) {
            val response = client.newCall(request).execute()
//            response.body?.let { Log.d("uuid", it.string()) }
            dataresp= response.body?.string() ?: "Not Found"
//
            val gson= Gson().fromJson<SingleCoinData>(dataresp, SingleCoinData::class.java)
            val x= gson.data?.coin

            withContext(Dispatchers.Main) {
                callback(x as Coin)
            }
//            GlobalScope.launch(Dispatchers.Main) {
//
//                val x= gson.data?.coin
//                if (x != null) {
//                    Log.d("value", x.symbol.toString())
//                }
//            }

        }
    }
}