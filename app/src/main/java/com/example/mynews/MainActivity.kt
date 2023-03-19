package com.example.mynews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var adapter: NewsAdapter
    lateinit var newsList: RecyclerView
    private var articles = mutableListOf<Article>()
    var pageNum = 1
    var totalResults = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        newsList = findViewById(R.id.newsList)
        adapter = NewsAdapter(this@MainActivity, articles)
        newsList.adapter = adapter
        val layoutManager = LinearLayoutManager(this@MainActivity)

        newsList.layoutManager = layoutManager

        getNews()
        if(totalResults>layoutManager.itemCount && layoutManager.findFirstVisibleItemPosition() >= layoutManager.itemCount - 5) {
            pageNum++
            getNews()
        }
    }

    private fun getNews() {
        val news = NewsService.newsInstance.getHeadlines("in", pageNum)
        news.enqueue(object: Callback<News>{
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                if(news != null) {
                    totalResults = news.totalResults
                    articles.addAll(news.articles)
                    adapter.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("TAG", "Error in Fetching News", t)
            }

        })
    }
}