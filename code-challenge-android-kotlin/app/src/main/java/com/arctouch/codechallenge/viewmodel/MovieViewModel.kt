package com.arctouch.codechallenge.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.webservice.MoviesWS

class MovieViewModel : ViewModel() {
    private lateinit var movie : MutableLiveData<Movie>

    fun getMovie(id: Int) : LiveData<Movie> {
        if(!::movie.isInitialized)
            movie = MutableLiveData()
            loadMovie(id)
        return movie
    }

    private fun loadMovie(id: Int) {
        MoviesWS().getMovie(id, { movie ->
           this.movie.value = movie
        },{
            //TODO onError()
        })
    }
}