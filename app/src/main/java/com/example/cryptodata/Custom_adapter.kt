package com.example.cryptodata

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso

class Custom_adapter(private val ctx: Context, private val cryptoList: List<CoinsItem>): BaseAdapter() {
    override fun getCount(): Int {
        return cryptoList.size
    }

    override fun getItem(position: Int): Any {
        return cryptoList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var myConvertView= convertView
        if (myConvertView== null){
            myConvertView= LayoutInflater.from(ctx).inflate(R.layout.single_row_items, parent, false)

        }
        val currentItem= getItem(position)
        val icon= myConvertView?.findViewById<ImageView>(R.id.CryptoIcon)
        val name= myConvertView?.findViewById<TextView>(R.id.CryptoName)
        val price= myConvertView?.findViewById<TextView>(R.id.CryptoPrice)

        val imageExt=  cryptoList[position].iconUrl?.takeLast(3)

        if(imageExt!= "svg")
        Picasso.get().load(cryptoList[position].iconUrl).into(icon)
        else {
//            Log.d("icon", cryptoList[position].iconUrl.toString() )
            GlideToVectorYou.justLoadImage(ctx as Activity?, Uri.parse(cryptoList[position].iconUrl), icon)
//            GlideToVectorYou.init().load(Uri.parse("https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg"), icon)
        }


//        }
        name?.text= (cryptoList[position].name)
        price?.text= "$ "+ (cryptoList[position].price)
        myConvertView?.setOnClickListener {
            Toast.makeText(ctx as Activity?, cryptoList[position].uuid, Toast.LENGTH_SHORT).show()
            val i= Intent(ctx as Activity?, ShowCoinDetails::class.java)
            i.putExtra("uuid", cryptoList[position].uuid)
            ctx.startActivity(i)



        }
        return myConvertView!!

    }



}


