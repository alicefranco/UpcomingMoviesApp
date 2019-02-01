package com.arctouch.codechallenge.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arctouch.codechallenge.viewmodel.HomeViewModel
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.AbsListView
import com.arctouch.codechallenge.InfiniteScrollListener
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.home.HomeAdapter
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.webservice.MoviesWS
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity() {
    private var isScrolling = false
    private var currentItems = 0
    private var scrolledOutItems = 0
    private var totalItems = 0
    private var upcomingMovies = ArrayList<Movie>()
    private var pageNumber = 1L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        viewModel.getGenres().observe(this, Observer<List<Genre>>{genres ->
            viewModel.getUpcomingMovies(pageNumber).observe(this, Observer<List<Movie>>{ movies ->
                movies?.let{
                    upcomingMovies = ArrayList(movies)
                    val layoutManager = LinearLayoutManager(this)
                    val adapter = HomeAdapter(this, upcomingMovies, showMovieFragmentLayout,
                    hideMovieFragmentLayout, showProgressBar, hideProgressBar)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter
                    recyclerView.addOnScrollListener(object: InfiniteScrollListener(layoutManager){
                        override fun loadMore() {
                            progressBar.visibility = View.VISIBLE
                            pageNumber++
                            viewModel.getUpcomingMovies(pageNumber).observe(this@HomeActivity, Observer<List<Movie>>{ movies ->
                                progressBar.visibility = View.GONE
                            })
                            adapter.notifyDataSetChanged()
                        }
                    })
                }
                //TODO notify user if movies = null
            })
        })
    }

    var showMovieFragmentLayout: () -> Unit = {
        frameLayout.visibility = View.VISIBLE
    }

    var hideMovieFragmentLayout: () -> Unit = {
        frameLayout.visibility = View.GONE
    }

    var showProgressBar: () -> Unit = {
        progressBar.visibility = View.VISIBLE
    }

    var hideProgressBar: () -> Unit = {
        progressBar.visibility = View.GONE
    }
}
