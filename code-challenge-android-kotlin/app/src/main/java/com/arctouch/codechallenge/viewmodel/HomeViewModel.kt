package com.arctouch.codechallenge.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.webservice.MoviesWS

class HomeViewModel : ViewModel() {
    private lateinit var movies: MutableLiveData<List<Movie>>

    fun getMovies(): LiveData<List<Movie>> {
        if (!::movies.isInitialized) {
            movies = MutableLiveData()
            loadMovies()
        }
        return movies
    }

    private fun loadMovies()  {
        MoviesWS().getUpcomingMovies(1, {
            val moviesWithGenres = it.results.map { movie ->
                movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
            }
            movies.value = moviesWithGenres
        },{
            //TODO onError()
        })
    }

}