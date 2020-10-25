package com.example.assignment


import android.app.Application
import androidx.room.Room
import com.example.assignment.ui.lists.MovieListViewModel
import com.example.assignment.api.MoviesApi
import com.example.assignment.models.Movie
import com.example.assignment.persistance.MovieDB
import com.example.assignment.repository.MoviesRepository
import com.example.assignment.util.AppExecutors
import com.example.assignment.util.BASE_URL
import com.example.assignment.util.DATABASE_NAME
import com.example.assignment.util.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class BaseApplication : Application() {
    private val appModule = module {
        single<MoviesApi> {
            val logger = HttpLoggingInterceptor()
            logger.setLevel(HttpLoggingInterceptor.Level.BASIC)
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(MoviesApi::class.java)
        }
        viewModel<MovieListViewModel> {
            val repository: MoviesRepository = get()
            MovieListViewModel(
                repository, this@BaseApplication
            )
        }
        single<MovieDB> {
            Room.databaseBuilder(
                this@BaseApplication.applicationContext, MovieDB::class.java,
                DATABASE_NAME
            ).build()
        }

        single<MoviesRepository> {
            val movieDB: MovieDB = get()
            val appExecutors: AppExecutors = get()
            val moviesApi: MoviesApi = get()
            MoviesRepository(movieDB.movieDao, appExecutors, moviesApi)
        }



        single<AppExecutors> {
            AppExecutors()
        }

    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(appModule)
        }
    }
}