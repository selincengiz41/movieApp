package com.selincengiz.movieapp.ui.movie

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.selincengiz.movieapp.R
import com.selincengiz.movieapp.common.viewBinding
import com.selincengiz.movieapp.data.model.Film
import com.selincengiz.movieapp.data.source.Database
import com.selincengiz.movieapp.databinding.AlertDialogBinding
import com.selincengiz.movieapp.databinding.AlertDialogDetailBinding
import com.selincengiz.movieapp.databinding.FragmentDetailBinding


class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)
    private lateinit var film: Film
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            film = DetailFragmentArgs.fromBundle(it).film
        }

        with(binding) {
            image.setImageBitmap(film.image)
            title.text = film.title
            desc.text = film.description
            date.text = film.date

            silBtn.setOnClickListener {

                val builder = AlertDialog.Builder(requireContext())
                val binding = AlertDialogDetailBinding.inflate(layoutInflater)
                builder.setView(binding.root)
                val dialog = builder.create()


                with(binding) {

                    noBtn.setOnClickListener {
                        dialog.dismiss()
                    }

                    yesBtn.setOnClickListener {
                        Database.removeFilm(film)
                        Database.isWatched()
                        dialog.dismiss()
                    }
                }

                dialog.show()


            }
        }
    }

}