package ru.example.myfirstkotlinapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import kotlinx.android.synthetic.main.activity_main.pageNumber
import kotlinx.android.synthetic.main.activity_repo_info.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.example.myfirstkotlinapp.api.StargazersInterface3
import ru.example.myfirstkotlinapp.model.StargazersList2
import ru.example.myfirstkotlinapp.utils.removeTime
import java.util.*
import kotlin.collections.ArrayList


class RepoInfoActivity : AppCompatActivity() {

    var yearStr= 2020

    var dateArray: ArrayList <Date> = ArrayList()
    var dateArray2: ArrayList <Date> = ArrayList()
    private val convertDateArray: ArrayList<Int> = ArrayList()
    var floatArray2: List <Float> = ArrayList()

    private var page:Int = 1
    private var per_page : Int = 100

    private var ownerRecycle: String? = null
    private var repoRecycle: String? = null
    var userArray: ArrayList <Any> = ArrayList()



    fun prevClick(view : View) {

        yearStr = yearStr.minus(1)
        pageNumber.text=(yearStr.toString() +" "+ getString(R.string.yearStr) )
        convertDateArray.clear()
        floatArray2.isEmpty()
        userArray.clear()
        dateArray.clear()
        dateArray2.clear()
        getCurrentData()
    }

    fun nextClick(view : View) {

        yearStr = yearStr.plus(1)
        pageNumber.text=(yearStr.toString() + " "+getString(R.string.yearStr) )
        convertDateArray.clear()
        floatArray2.isEmpty()
        userArray.clear()
        dateArray.clear()
        dateArray2.clear()
        getCurrentData()
    }

    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_info)

        yearStr=2020

        val pageNumber: TextView = findViewById(R.id.pageNumber)

        pageNumber.text=(yearStr.toString() +" "+  getString(R.string.yearStr))

        val bundle =intent.extras

          repoRecycle = (bundle?.getCharSequence("repoName")).toString()
         ownerRecycle = (bundle?.getCharSequence("personName")).toString()

        getCurrentData ()
    }

    private fun getCurrentData () {
        convertDateArray.clear()
        floatArray2.isEmpty()

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client1 = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val moshi = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()

        val builder = Retrofit.Builder()
            .client(client1)
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))

        val retrofit = builder.build()

        val client = retrofit.create<StargazersInterface3>(StargazersInterface3::class.java)

        val call = client.stargazersForRepo(ownerRecycle!!, repoRecycle!!, page, per_page).also {

            it.enqueue(object : Callback<List<StargazersList2>> {

                override fun onResponse(
                    call: Call<List<StargazersList2>>,
                    response: Response<List<StargazersList2>>
                ) {

                    val repo2 = response.body()
                    repo2?.forEach { it.starred_at?.let { it1 -> dateArray.add(it1.removeTime()) } }
                    repo2?.forEach { it.starred_at?.let { it1 -> dateArray2.add(it1) } }
                    repo2?.forEach { it.user?.let { it1 -> userArray.add(it1) } }


                    val mapNumber = mutableMapOf<Any, Date>()
                        .apply {
                            for (i in userArray.indices) this[userArray[i]] =
                                dateArray2[i].removeTime()
                        }

                    val c1 = Calendar.getInstance()
                    c1[Calendar.DAY_OF_MONTH] = 31
                    c1[Calendar.MONTH] = 11
                    c1[Calendar.YEAR] = yearStr - 1
                    c1[Calendar.HOUR_OF_DAY] = 23
                    c1[Calendar.MINUTE] = 59
                    c1[Calendar.SECOND] = 59
                    c1[Calendar.MILLISECOND] = 0

                    val c2 = Calendar.getInstance()
                    c2[Calendar.DAY_OF_MONTH] = 1
                    c2[Calendar.MONTH] = 0
                    c2[Calendar.YEAR] = yearStr + 1
                    c2[Calendar.HOUR_OF_DAY] = 0
                    c2[Calendar.MINUTE] = 0
                    c2[Calendar.SECOND] = 0
                    c2[Calendar.MILLISECOND] = 0

                    val arraStars2 = mapNumber.filter {
                        it.value.after(c1.time) }.filter { it.value.before(c2.time) }

                    val arrayconv = dateArray.groupingBy { it }.eachCount().filter { it.value > 0 }

                    val arraconv33 = arrayconv.filter {
                        it.key.after(c1.time) }.filter { it.key.before(c2.time) } // for check items
                    textView3.text = arraconv33.toString() // for check items

                    val arraconv2 = arrayconv.filter { it.toString().contains(yearStr.toString()) }

                    val month = listOf(
                        " Jan",
                        " Feb",
                        " Mar",
                        " Apr",
                        " May",
                        " Jun",
                        " Jul",
                        " Aug",
                        " Sep",
                        " Oct",
                        " Nov",
                        " Dec")

                    for (i in month.indices) {
                        if (arraconv2.keys.toString().contains(month[i])) {
                            val position = arraconv2.filter { it.toString().contains(month[i]) }
                            convertDateArray.add(position.values.elementAt(0))
                        } else {
                            convertDateArray.add(0)
                        }
                    }

                 val values2: List<Float> = convertDateArray.map { it.toFloat() }
                 floatArray2 = values2

                 val bargroup = ArrayList<BarEntry>()

                 for (i in floatArray2.indices) {
                       bargroup.add(
                            BarEntry(
                                (i).toFloat(),
                                floatArray2.elementAt(i),
                                month.elementAt(i)
                            )
                        )
                    }

                    val barDataSet = BarDataSet(bargroup, "")
                    barDataSet.color = ContextCompat.getColor(this@RepoInfoActivity, R.color.amber)
                    val data = BarData(barDataSet)

                    barChart.setData(data)
                    barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                    barChart.xAxis.labelCount = 11
                    barChart.xAxis.valueFormatter = IndexAxisValueFormatter(month)
                    barChart.xAxis.enableGridDashedLine(5f, 5f, 0f)
                    barChart.axisRight.enableGridDashedLine(5f, 5f, 0f)
                    barChart.axisLeft.enableGridDashedLine(5f, 5f, 0f)
                    barChart.description.isEnabled = false
                    barChart.animateY(700)
                    barChart.legend.isEnabled = false
                    barChart.setPinchZoom(true)
                    barChart.data.setDrawValues(false)

                    barChart!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {

                        override fun onValueSelected(e: Entry, h: Highlight?) {

                            Log.i("chose bar", e.toString())
                            val intent = Intent(this@RepoInfoActivity, ChartActivity::class.java)
                            val bundle = Bundle()
                            val arrListStar2: ArrayList<String> = ArrayList()
                            val print =  arraStars2.filter { it.toString().contains(month[e.x.toInt()]) }
                            Log.i("print",print.toString())
                            print.keys.forEach { it.let { it1 -> arrListStar2.add(it1.toString()) } }


                            bundle.putStringArrayList("listStar", arrListStar2)
                            bundle.putString("current year", yearStr.toString())
                            bundle.putString("current month", month[e.x.toInt()])
                            intent.putExtras(bundle)
                            this@RepoInfoActivity.startActivity(intent)
                        }
                        override fun onNothingSelected() {}
                    })
                }

                override fun onFailure(call: Call<List<StargazersList2>>, t: Throwable) {
                    Toast.makeText(this@RepoInfoActivity, "Error :(", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}



















