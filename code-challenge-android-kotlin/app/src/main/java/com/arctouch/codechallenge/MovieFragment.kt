package com.arctouch.codechallenge

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_fragment.*

class MovieFragment : Fragment() {

    private var title: String? = ""
    private var releaseDate: String? = ""
    private var overview: String? = ""
    private var posterPath: String? = ""
    private var backdropPath: String? = ""
    private lateinit var genres: List<Genre>
    private var closeMovieFragment: () -> Unit = {}

    companion object {
        fun newInstance(movie: Movie, closeMovieFragment: () -> Unit) : MovieFragment {
            return MovieFragment().apply {
                this.title = movie.title
                this.releaseDate = movie.releaseDate
                this.overview = movie.overview
                this.posterPath = movie.posterPath
                this.backdropPath = movie.backdropPath
                this.closeMovieFragment = closeMovieFragment
                movie.genres?.let{
                    this.genres = movie.genres
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextView.text = title
        releaseDateTextView.text = releaseDate
        overviewTextView.text = overview

        var genresNames = ""
        genres.forEach{
            genresNames =  ", " + it.name + genresNames
        }
        //genresNames.removePrefix(", ")
        genreTextView.text = genresNames

        closeMovieImageView.setOnClickListener { closeMovieFragment() }

        Glide.with(posterImageView)
                .load(posterPath?.let { MovieImageUrlBuilder().buildPosterUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(posterImageView)

        if(backdropPath != null){
            Glide.with(backdropImageView)
                    .load(backdropPath?.let { MovieImageUrlBuilder().buildBackdropUrl(it) })
                    .into(backdropImageView)
        }
        else{
            backdropImageView.visibility = View.GONE
        }

    }
}