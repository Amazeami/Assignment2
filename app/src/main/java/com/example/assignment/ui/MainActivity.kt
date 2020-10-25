package com.example.assignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.assignment.ui.lists.MovieListFragment
import com.example.assignment.R
import com.example.assignment.api.MoviesApi
import com.example.assignment.models.Movie
import org.koin.android.ext.android.inject

class MainActivity :AppCompatActivity(),
    MovieListFragment.Callbacks{
    val movieApi: MoviesApi by inject()
    private lateinit var toolbar: Toolbar
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpToolbar()

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_container,
                    MovieListFragment()
                )
                .commit()
        }
    }
    private fun setUpToolbar(){
        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        (supportFragmentManager.findFragmentById(R.id.main_container) as MovieListFragment).onBackPressed()
    }

    override fun onMovieClick(movie: Movie) {
        val intent = DetailActivity.getIntent(movie,this)
        startActivity(intent)
    }

}