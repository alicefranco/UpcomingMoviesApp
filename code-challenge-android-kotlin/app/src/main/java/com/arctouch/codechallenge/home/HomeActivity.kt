package com.arctouch.codechallenge.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.base.BaseRequest
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.webservice.MoviesWS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        MoviesWS().getUpcomingMovies(1, {
            val moviesWithGenres = it.results.map { movie ->
                movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
            }
            recyclerView.adapter = HomeAdapter(moviesWithGenres)
            progressBar.visibility = View.GONE
        },{
            //TODO onError
        })
    }
}
