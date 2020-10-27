package ru.example.myfirstkotlinapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_chart.*
import ru.example.myfirstkotlinapp.adapters.CustomRecyclerAdapter
import kotlin.collections.ArrayList


class StargazersForMonthActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_LOGIN = "loginIntent"
        private const val EXTRA_AVATAR = "avatarIntent"
        private const val EXTRA_CURRENT_YEAR = "current year"
        private const val EXTRA_CURRENT_MONTH = "current month"

        fun createIntent( context: Context, loginList: List<String>, avatarUrls: List<String>,
            year: Int, month: String ): Intent {

            return Intent(context, StargazersForMonthActivity::class.java)
                .putExtra(EXTRA_LOGIN, ArrayList(loginList))
                .putExtra(EXTRA_AVATAR, ArrayList(avatarUrls))
                .putExtra(EXTRA_CURRENT_YEAR, year.toString())
                .putExtra(EXTRA_CURRENT_MONTH, month)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        val textView = findViewById<TextView>(R.id.textViewYear)

        val loginIntent = intent.getStringArrayListExtra(EXTRA_LOGIN)!!
        val urlIntent = intent.getStringArrayListExtra(EXTRA_AVATAR)!!
        val currentYear = intent.getStringExtra(EXTRA_CURRENT_YEAR)
        val currentMonth = intent.getStringExtra(EXTRA_CURRENT_MONTH)

        textView.text = getString(R.string.star1, currentMonth, currentYear)

        recyclerView.adapter = CustomRecyclerAdapter(urlIntent, loginIntent)
    }
}







