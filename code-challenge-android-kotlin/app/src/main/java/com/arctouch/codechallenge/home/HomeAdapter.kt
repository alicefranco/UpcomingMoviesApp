package com.arctouch.codechallenge.home

import android.app.FragmentManager
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_item.view.*
import com.arctouch.codechallenge.MovieFragment
import com.arctouch.codechallenge.webservice.MoviesWS

class HomeAdapter(private val context: Context, private val movies: List<Movie>,
                  private var showMovieFragmentLayout: () -> Unit, private var hideMovieFragmentLayout: () -> Unit,
                  var showProgressbar: () -> Unit, var hideProgressbar: () -> Unit) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var firstFragment = false
    private val fragmentManager = (context as FragmentActivity).supportFragmentManager

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var movieRelativeLayout = itemView.movieRelativeLayout

        private val movieImageUrlBuilder = MovieImageUrlBuilder()

        fun bind(movie: Movie) {
            itemView.titleTextView.text = movie.title
            itemView.genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
            itemView.releaseDateTextView.text = movie.releaseDate

            Glide.with(itemView)
                .load(movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(itemView.posterImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(movies[position])
        holder.movieRelativeLayout.setOnClickListener {
            showProgressbar()
            openMovieFragment(movies[position].id)
        }
        holder
    }

    fun openMovieFragment(id: Int){
        //TODO remove API call
        MoviesWS().getMovie(id, { movie ->
            val fragment = MovieFragment.newInstance(movie, closeMovieFragment)
            val fragmentTransaction = fragmentManager.beginTransaction()
            if(firstFragment == false) fragmentTransaction.add(R.id.frameLayout, fragment,"MOVIE_FRAGMENT")
            else fragmentTransaction.replace(R.id.frameLayout, fragment,"MOVIE_FRAGMENT")
            fragmentTransaction.commit()
            showMovieFragmentLayout()
            hideProgressbar()
        },{
            //TODO onError()
        })
    }

    private var closeMovieFragment: () -> Unit = {
        hideMovieFragmentLayout()
        fragmentManager.popBackStack()
    }

}
