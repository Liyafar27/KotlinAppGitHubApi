package ru.example.myfirstkotlinapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.example.myfirstkotlinapp.adapters.RecyclerAdapterMain
import ru.example.myfirstkotlinapp.api.GitHubClient
import ru.example.myfirstkotlinapp.api.GitHubRepo
import ru.example.myfirstkotlinapp.network.APIClient

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var repoName: String = ""
    private var page: Int = 1
    private var perPage: Int = 100
    private val api = APIClient.retrofit.create(GitHubClient::class.java)
    private var savedIn: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedIn = savedInstanceState?.getString("repoName")

        val pageNumber: TextView = findViewById(R.id.pageNumber)
        val buttonNext: Button = findViewById(R.id.next)
        val buttonPrev: Button = findViewById(R.id.prev)
        val buttonSearch: Button = findViewById(R.id.buttonSearch)

        pageNumber.text = getString(R.string.numberPage1, page)

        recyclerView2.layoutManager = LinearLayoutManager(this)

        buttonNext.setOnClickListener(this)
        buttonPrev.setOnClickListener(this)
        buttonSearch.setOnClickListener(this)
    }

    private fun personItemClicked(person: GitHubRepo) {

        val intent = ChartActivity.createIntent(this@MainActivity, person)
        startActivity(intent)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.prev || view?.id == R.id.next) {

            if (view.id == R.id.prev) page -= 1
            if (view.id == R.id.next) page += 1

            pageNumber.text = getString(R.string.numberPage1, page)
        }

        if (view?.id == R.id.buttonSearch) {
            buttonSearchFun()
        }
    }

    private fun buttonSearchFun() {
        repoName = editText.text.toString()
        requestRepo()
    }

    private fun requestRepo() {
        api.reposForUser(repoName, page, perPage).enqueue(object : Callback<List<GitHubRepo>> {

            override fun onResponse(
                call: Call<List<GitHubRepo>>, response: Response<List<GitHubRepo>>
            ) {
                val repos = response.body()
                val adapterM = RecyclerAdapterMain(repos!!, ::personItemClicked)
                recyclerView2.adapter = adapterM
            }

            override fun onFailure(call: Call<List<GitHubRepo>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error :(", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.run { putString("repoName", repoName) }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

//        if(repoName.isNotEmpty())
//        {
        val repoPersistent = savedInstanceState.getString("repoName", repoName)
        repoName = repoPersistent
        requestRepo()
//    }
//         else
//            Log.i("blablabla", "blablabla")
    }
}








