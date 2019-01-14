package com.arctouch.codechallenge.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.webservice.MoviesWS

class HomeViewModel : ViewModel() {
    private lateinit var movies: MutableLiveData<List<Movie>>
    private lateinit var genres: MutableLiveData<List<Genre>>
    private lateinit var totalPages: MutableLiveData<Int>


    fun getUpcomingMovies(page: Long) : LiveData<List<Movie>> {
        if (!::movies.isInitialized) {
            movies = MutableLiveData()
            loadMovies(page)
        }
        return movies
    }

    fun getTotalPages() : LiveData<Int>{
        if (!::totalPages.isInitialized) {
            totalPages.value = 0
        }
        return totalPages
    }

    private fun loadMovies(page: Long)  {
        MoviesWS().getUpcomingMovies(page, {
            val moviesWithGenres = it.results.map { movie ->
                movie.copy(genres = genres.value?.filter { movie.genreIds?.contains(it.id) == true })
            }
            movies.value = moviesWithGenres
            totalPages.value = it.totalPages
        },{
            //TODO onError()
        })
    }

    fun getGenres(): LiveData<List<Genre>> {
        if (!::genres.isInitialized) {
            genres = MutableLiveData()
            loadGenres()
        }
        return genres
    }

    private fun loadGenres()  {
        MoviesWS().getGenres({
            genres.value = it.genres
            Cache.cacheGenres(it.genres)
        },{
            //TODO onError()
        })
    }

}