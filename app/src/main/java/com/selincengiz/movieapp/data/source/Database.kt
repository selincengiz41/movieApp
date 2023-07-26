package com.selincengiz.movieapp.data.source

import android.graphics.Bitmap
import android.util.Log
import com.selincengiz.movieapp.data.model.Film

object Database {

    private val filmList = mutableListOf<Film>()
    private val watchfilmList = mutableListOf<Film>()
    private val watchedfilmList = mutableListOf<Film>()

    fun getFilmIndex(film: Film) = filmList.indexOf(film)
    fun getWatchedFilm() = watchedfilmList
    fun getWatchFilm() = watchfilmList

    fun addFilm(
        title: String,
        description: String,
        isWatched: Boolean,
        date: String,
        image: Bitmap?
    ) {
        filmList.add(
            Film(
                id = (filmList.lastOrNull()?.id ?: 0) + 1,
                title,
                description,
                isWatched,
                date,
                image
            )
        )
    }

    fun removeFilm(film: Film) = filmList.remove(film)

    fun updateFilm(index: Int, film: Film) {
        Log.i("ilkhal", filmList.get(index).isWatched.toString())
        Log.i("updated",film.isWatched.toString())

        filmList.set(index, film)
        isWatched()
        Log.i("sonhal",filmList.get(index).isWatched.toString())
    }

    /////////////////////////////////////////////////////////////

    fun isWatched() {
        watchedfilmList.clear()
        watchfilmList.clear()
        filmList.forEach {
            if (it.isWatched) {
                watchedfilmList.add(it)
            } else {
                watchfilmList.add(it)
            }
        }
    }


}