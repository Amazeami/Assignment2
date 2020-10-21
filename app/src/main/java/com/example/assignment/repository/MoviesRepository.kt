package com.example.assignment.repository

import android.util.Log
import androidx.lifecycle.LiveData

import com.example.assignment.api.MoviesApi
import com.example.assignment.api.responses.ApiResponse
import com.example.assignment.api.responses.MoviesResponse
import com.example.assignment.models.Movie
import com.example.assignment.persistance.dao.MovieAndDetailDao
import com.example.assignment.util.AppExecutors
import com.example.assignment.util.Category
import com.example.assignment.util.NetworkBoundResource
import com.example.assignment.util.Resource


class MoviesRepository(
    private val movieDao: MovieAndDetailDao,
    private val appExecutors: AppExecutors,
    private val movieApi: MoviesApi
) {
    companion object {
        private const val TAG = "MoviesRepository"
    }

    fun getListMovie(pageNumber: Int, category: Category): LiveData<Resource<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, MoviesResponse>(appExecutors) {
            override fun saveCallResult(item: MoviesResponse?) {
                item?.let {
                    val list: ArrayList<Movie>? = (item.movies)?.let { ArrayList(it) }
                    val newList: ArrayList<Movie>? = ArrayList<Movie>()
                    list?.forEach {
                        val movie = it.copy(categoryType = category)
                        Log.d(TAG, "saveCallResult: ${movie}")
                        newList?.add(movie)
                    }
                    newList?.let {
                        movieDao.insertMovies(*newList.toTypedArray())
                    }
                }
            }

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Movie>> =
                movieDao.getMovies(pageNumber, category)

            override fun createCall(): LiveData<ApiResponse<MoviesResponse>> = when (category) {
                Category.TOPRATED -> movieApi.getTopRatedMovies(pageNumber)
                Category.UPCOMING -> movieApi.geUpcomingMovies(pageNumber)
                else -> movieApi.getPopularMovies(pageNumber)
            }

        }.asLiveData()
    }

    fun searchListMovie(pageNumber: Int, query: String): LiveData<Resource<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, MoviesResponse>(appExecutors) {
            override fun saveCallResult(item: MoviesResponse?) {
                item?.let {
                    val list: ArrayList<Movie>? = (item.movies)?.let { ArrayList(it) }
                    list?.let {
                        movieDao.insertMovies(*list.toTypedArray())
                    }
                }
            }

            override fun shouldFetch(data: List<Movie>?): Boolean = true //always refresh

            override fun loadFromDb(): LiveData<List<Movie>> =
                movieDao.searchListMovie(query, pageNumber)

            override fun createCall(): LiveData<ApiResponse<MoviesResponse>> =
                movieApi.searchMovies(
                    query, pageNumber
                )
        }.asLiveData()
    }
}