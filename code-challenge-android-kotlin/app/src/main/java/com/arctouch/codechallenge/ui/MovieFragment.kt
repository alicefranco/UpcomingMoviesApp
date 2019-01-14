package com.arctouch.codechallenge.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.arctouch.codechallenge.viewmodel.MovieViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_fragment.*

class MovieFragment : Fragment() {

    private var id: Int? = null
    private lateinit var genres: List<Genre>
    private var closeMovieFragment: () -> Unit = {}

    companion object {
        fun newInstance(id: Int, closeMovieFragment: () -> Unit) : MovieFragment {
            return MovieFragment().apply {
                this.id = id
                this.closeMovieFragment = closeMovieFragment
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)

        progressBar.visibility = View.VISIBLE

        viewModel.getMovie(id ?: 0).observe(this,   Observer<Movie> { movie ->
            movie?.genres?.let {
                this.genres = movie.genres
            }

            titleTextView.text = movie?.title
            releaseDateTextView.text = movie?.releaseDate

            if(movie?.overview.isNullOrBlank()){
                overviewTextView.text = getString(R.string.overview_not_available)
            }
            else
                overviewTextView.text = movie?.overview

            var genresNames = ""
            var ignoreFirst = true
            genres.forEach {
                if(ignoreFirst) {
                    ignoreFirst = false
                    genresNames = it.name
                }
                else{
                    genresNames = genresNames + ", " + it.name
                }
            }
            genreTextView.text = genresNames

            closeMovieImageView.setOnClickListener { closeMovieFragment() }

            Glide.with(posterImageView)
                    .load(movie?.posterPath?.let { MovieImageUrlBuilder().buildPosterUrl(it) })
                    .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(posterImageView)

            if (!movie?.backdropPath.isNullOrBlank()) {
                Glide.with(backdropImageView)
                        .load(movie?.backdropPath?.let { MovieImageUrlBuilder().buildBackdropUrl(it) })
                        .into(backdropImageView)
                backdropImageView.visibility = View.VISIBLE
            } else {
                backdropImageView.visibility = View.GONE
            }

            progressBar.visibility = View.GONE
        })
    }
}