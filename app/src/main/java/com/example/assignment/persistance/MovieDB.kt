package com.example.assignment.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.assignment.models.Movie
import com.example.assignment.persistance.dao.MovieAndDetailDao

@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converter::class)
abstract class MovieDB : RoomDatabase() {
    abstract val movieDao: MovieAndDetailDao


}