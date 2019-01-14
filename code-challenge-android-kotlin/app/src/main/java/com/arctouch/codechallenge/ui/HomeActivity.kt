package com.arctouch.codechallenge.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arctouch.codechallenge.viewmodel.HomeViewModel
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.home.HomeAdapter
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        viewModel.getGenres().observe(this, Observer<List<Genre>>{genres ->
            viewModel.getMovies().observe(this, Observer<List<Movie>>{ movies ->
                movies?.let{
                    recyclerView.adapter = HomeAdapter(this, movies, showMovieFragmentLayout,
                            hideMovieFragmentLayout, showProgressBar, hideProgressBar)
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
}
