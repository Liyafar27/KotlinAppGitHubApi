package ru.example.myfirstkotlinapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.activity_repo_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.example.myfirstkotlinapp.api.GitHubRepo
import ru.example.myfirstkotlinapp.api.StargazersInterface
import ru.example.myfirstkotlinapp.model.Stargazer
import ru.example.myfirstkotlinapp.model.User
import ru.example.myfirstkotlinapp.network.APIClient
import ru.example.myfirstkotlinapp.utils.*
import java.util.*

class ChartActivity : AppCompatActivity(), View.OnClickListener {

    companion object {

        private const val EXTRA_PERSON_NAME = "personName"
        private const val EXTRA_REPO_NAME = "repoName"
        private const val perPage: Int = 100

        fun createIntent(context: Context, person: GitHubRepo): Intent {
            return Intent(context, ChartActivity::class.java)
                .putExtra(EXTRA_PERSON_NAME, person.owner!!.login.toString())
                .putExtra(EXTRA_REPO_NAME, person.name)
        }
    }

    private var year: Int = Date().getYearInt()
    private var page: Int = 1
    private var ownerRecycle: String? = null
    private var repoRecycle: String? = null
    private val api = APIClient.retrofit.create(StargazersInterface::class.java)
    private var data: Map<User, Date> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_info)

        savedInstanceState?.getInt("year")

        val pageNumber: TextView = findViewById(R.id.pageNumber)
        val buttonNext: Button = findViewById(R.id.nextRepo)
        val buttonPrev: Button = findViewById(R.id.prevRepo)

        buttonNext.setOnClickListener(this)
        buttonPrev.setOnClickListener(this)

        pageNumber.text = getString(R.string.yearStr1, year)

        repoRecycle = intent.getStringExtra(EXTRA_REPO_NAME)
        ownerRecycle = intent.getStringExtra(EXTRA_PERSON_NAME)

        setupBarChart()

        requestDataForSelectedYear()
    }

    private fun setupBarChart() {
        barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {

            override fun onValueSelected(e: Entry, h: Highlight?) {
                val entryX = e.x.toInt()
                val stringForIntent = certainMonth(data, entryX)
                val avatarsUrl = stringForIntent.keys.map { user -> user.avatar_url!! }
                val loginList = stringForIntent.keys.map { it.login!! }
                val month = MonthObj.month[entryX]

                val intent = StargazersForMonthActivity.createIntent(
                    this@ChartActivity,
                    loginList, avatarsUrl, year, month
                )
                startActivity(intent)
            }

            override fun onNothingSelected() {}
        })
    }

    private fun requestDataForSelectedYear() {
        api.stargazersForRepo(ownerRecycle!!, repoRecycle!!,page, perPage)
            .enqueue(object : Callback<List<Stargazer>> {

            override fun onResponse(call: Call<List<Stargazer>>,response: Response<List<Stargazer>>
            ) {

                val repo = response.body()!!

                val listForChart = createValuesForChart(repo, year)

                data = createMapForIntent(repo, year)

                val barGroup = listForChart.mapBarEntry()

                setDataForBarChart(barGroup, this@ChartActivity, barChart)
            }

            override fun onFailure(call: Call<List<Stargazer>>, t: Throwable) {
                Toast.makeText(this@ChartActivity, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(view: View) {

        if (view.id == R.id.prevRepo) year -= 1
        if (view.id == R.id.nextRepo) year += 1
        pageNumber.text = getString(R.string.yearStr1, year)

        requestDataForSelectedYear()
    }

    private fun createValuesForChart(repo: List<Stargazer>?, yearInt: Int): List<Float> {

        val dateOfStars = repo?.map { it.starred_at.removeTime() }

        val dateOfStarsGrouping =
            dateOfStars?.groupingBy { it }!!.eachCount().filter { it.value > 0 }
        val dateOfStarsYear = dateOfStarsGrouping.filterYear2(yearInt)

        return certainMonth2(dateOfStarsYear)
    }

    private fun createMapForIntent(repo: List<Stargazer>, yearInt: Int): Map<User, Date> {

        val userList = repo.map { it.starred_at.removeTime() }

        val users = repo.map { it.user }

        val mapIntent = users
            .mapIndexed { i, user -> user to userList[i] }
            .toMap()

        return mapIntent.filterYear(yearInt)
    }

    private fun setDataForBarChart(barGroup: List<BarEntry>, context: Context, barChart: BarChart) {

        val barDataSet = BarDataSet(barGroup, "")
        barDataSet.color = ContextCompat.getColor(context, R.color.amber)
        val data = BarData(barDataSet)

        barChart.data = data
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.labelCount = 11
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(MonthObj.month)
        barChart.xAxis.enableGridDashedLine(5f, 5f, 0f)
        barChart.axisRight.enableGridDashedLine(5f, 5f, 0f)
        barChart.axisLeft.enableGridDashedLine(5f, 5f, 0f)
        barChart.description.isEnabled = false
        barChart.animateY(700)
        barChart.legend.isEnabled = false
        barChart.setPinchZoom(true)
        barChart.data.setDrawValues(false)
    }

    private fun List<Float>.mapBarEntry() = mapIndexed { i, fl ->
        BarEntry(i.toFloat(), fl, MonthObj.month[i])
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.run { putInt("year", year) }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val yearRest = savedInstanceState.getInt("year", year)
        year = yearRest
        pageNumber.text = getString(R.string.yearStr1, year)
    }
}






















