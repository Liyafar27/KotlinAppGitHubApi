package ru.example.myfirstkotlinapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chart.*
import ru.example.myfirstkotlinapp.adapters.CustomRecyclerAdapter
import kotlin.collections.ArrayList


class ChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        val textView = findViewById<TextView>(R.id.textViewYear)

        val bundle =intent.extras
        val bundleAr = bundle!!.getStringArrayList("listStar")
        val currentYear=bundle.getString("current year")
        val currentMonth = bundle.getString("current month")

        textView.text=(getString(R.string.star)+currentMonth + " "+ currentYear )

        val nameAr : ArrayList<String> = ArrayList()
        val urlAr : ArrayList<String> = ArrayList()

        for(i in bundleAr!!.indices) {
            val name1 = bundleAr[i].split("login=")
            val name2 = name1[1].split(", id")
            nameAr.add(name2[0])

            val url1= bundleAr[i].split(", gravatar_id")
            val url21 =url1[0].split("avatar_url=")
            val url3 = url21[1].trim()
            urlAr.add(url3)
         }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            CustomRecyclerAdapter(
                urlAr,
                nameAr
            )

    }
  }







