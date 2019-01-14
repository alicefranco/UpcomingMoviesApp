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
                    val scrollListener = getScrollListener(layoutManager, adapter)
                    recyclerView.addOnScrollListener(scrollListener)
                    progressBar.visibility = View.GONE
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

    fun getScrollListener(layoutManager: LinearLayoutManager, adapter: HomeAdapter) : RecyclerView.OnScrollListener {
        return object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = layoutManager.childCount
                scrolledOutItems = layoutManager.findFirstVisibleItemPosition()
                totalItems = layoutManager.itemCount

                if(isScrolling && (currentItems + scrolledOutItems == totalItems)){
                    isScrolling = false
                    pageNumber++
                    progressBar.visibility = View.VISIBLE
                    //TODO move api call to viewmodel
                    MoviesWS().getUpcomingMovies(pageNumber, {
                        val moviesWithGenres = it.results.map { movie ->
                            movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                        }
                        moviesWithGenres.forEach {
                            upcomingMovies.add(it)
                        }
                        adapter.notifyDataSetChanged()
                        progressBar.visibility = View.GONE
                    },{
                        //TODO onError()
                    })
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true
            }
        }
    }
}
