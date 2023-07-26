package com.selincengiz.movieapp.ui.movie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.selincengiz.movieapp.R
import com.selincengiz.movieapp.adapter.FilmAdapter
import com.selincengiz.movieapp.common.viewBinding
import com.selincengiz.movieapp.data.model.Film
import com.selincengiz.movieapp.data.source.Database
import com.selincengiz.movieapp.databinding.FragmentWatchedListBinding


class WatchedListFragment : Fragment(R.layout.fragment_watched_list) {

    private val binding by viewBinding(FragmentWatchedListBinding::bind)

    private val filmAdapter = FilmAdapter(::onFilmClicked, ::onCheckedChange)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            setData()
        }
    }

    fun setData() {
        Database.isWatched()
        binding.watchedListRecycler.adapter = filmAdapter
        filmAdapter.updateList(Database.getWatchedFilm())

    }

    private fun onFilmClicked(s: Film) {

        val action = WatchedListFragmentDirections.actionWatchedListFragmentToDetailFragment(s)
        Navigation.findNavController(binding.root).navigate(action)
    }

    private fun onCheckedChange() {
        filmAdapter.updateList(Database.getWatchedFilm())
    }

}