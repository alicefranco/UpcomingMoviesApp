package com.arctouch.codechallenge.webservice

import com.arctouch.codechallenge.webservice.api.TmdbApi
import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult

class MoviesWS {

    fun getGenres(onSuccess: (genres: GenreResponse) -> Unit, onError: () -> Unit) {
        launch(UI){
            try {
                BaseRequest.api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                        .awaitResult().let { result ->
                    when (result) {
                        is Result.Ok -> onSuccess(result.value)
                        is Result.Error -> {}//todo onError(result.response.code())
                        is Result.Exception -> {}//todo onError(Constants.ERROR_BAD_REQUEST)
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                //todo onError() Constants.ERROR_BAD_REQUEST)
            }
        }
    }

    fun getUpcomingMovies(page: Long, onSuccess: (movies: UpcomingMoviesResponse) -> Unit, onError: () -> Unit){
        launch(UI){
            try {
                BaseRequest.api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page,
                        TmdbApi.DEFAULT_REGION).awaitResult().let { result ->
                    when (result) {
                        is Result.Ok -> onSuccess(result.value)
                        is Result.Error -> {}//todo onError(result.response.code())
                        is Result.Exception -> {}//todo onError(Constants.ERROR_BAD_REQUEST)
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                //todo onError() Constants.ERROR_BAD_REQUEST)
            }
        }
    }

    fun getMovie(id: Int, onSuccess: (movie: Movie) -> Unit, onError: () -> Unit){
        launch(UI){
            try {
                BaseRequest.api.movie(id.toLong(), TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE).awaitResult().let { result ->
                    when (result) {
                        is Result.Ok -> onSuccess(result.value)
                        is Result.Error -> {}//todo onError(result.response.code())
                        is Result.Exception -> {}//todo onError(Constants.ERROR_BAD_REQUEST)
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                //todo onError() Constants.ERROR_BAD_REQUEST)
            }
        }
    }
}