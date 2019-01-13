package com.arctouch.codechallenge.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.arctouch.codechallenge.HomeViewModel
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.base.BaseRequest
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.webservice.MoviesWS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        viewModel.getMovies().observe(this, Observer<List<Movie>>{ movies ->
            movies?.let{
                recyclerView.adapter = HomeAdapter(this, movies, showMovieFragmentLayout,
                        hideMovieFragmentLayout, showProgressBar, hideProgressBar)
                progressBar.visibility = View.GONE
            }
            //TODO notify user if movies = null
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
