package com.example.cryptodata

import android.content.Intent
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.google.gson.Gson
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
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
        var coinGraph= findViewById<GraphView>(R.id.coinGraph)

        var backbutton= findViewById<Button>(R.id.backButton)
        backbutton.setOnClickListener {
            val i= Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        val uuid= intent.getStringExtra("uuid")
        val url= "https://coinranking1.p.rapidapi.com/coin/"+uuid.toString()+"?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h"

        callApi(url) { x ->
            if (x != null) {

                coinMaxPrice.text = "$ " + x.allTimeHigh?.price.toString()
                coinCurrPrice.text = "$ " + x.price.toString()
                coinName.text= x.name.toString() + " ( " + x.symbol.toString() + " )"
                coinDesc.text= x.description.toString()
                val imageExt=  x.iconUrl?.takeLast(3)

                if(imageExt!= "svg")
                    Picasso.get().load(x.iconUrl.toString()).into(coinIcon)
                else {
                    GlideToVectorYou.justLoadImage(this, Uri.parse(x.iconUrl.toString()), coinIcon)
                }

                create_graph(coinGraph, x.sparkline)



            } else {
                Log.d("err", "error occured")
            }
        }

    }

    private fun create_graph(coinGraph: GraphView?, sparkline: List<String?>?) {
        val dataPoints = mutableListOf<DataPoint>()

        // Iterating through the sparkline data to create DataPoint objects
        for (i in sparkline?.indices!!) {
            val hour = i.toDouble() // hour value starting from 0
            val value = sparkline[i]?.toDouble()
            val dataPoint = value?.let { DataPoint(hour, it) }
            Log.d("Data",hour.toString()+ " " + value.toString() )
            if (dataPoint != null) {
                dataPoints.add(dataPoint)
            }
        }
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(dataPoints.toTypedArray())
//        coinGraph?.animate()
//        series.title= "24 Hr change"
        coinGraph!!.title="24 hr change"
        coinGraph.titleColor= getColor(R.color.yellow)
        coinGraph.titleTextSize= 40F
        coinGraph?.viewport!!.isScrollable = true

//        coinGraph?.viewport!!.isScalable = true

        // on below line we are setting scrollable y
//        coinGraph?.viewport!!.setScrollableY(true)

        // on below line we are setting color for series.
//        series.color = "#FFFFFF"
        // styling series
        // styling series
        series.title = "Random Curve 1"
//        series.color = getColor(R.color.purple_700)
        series.isDrawDataPoints = true
        series.dataPointsRadius = 5f
        series.thickness = 3

        if (coinGraph != null) {
            coinGraph.addSeries(series)
        }
//        coinGraph.takeSnapshotAndShare(this, "newImg", "test")


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