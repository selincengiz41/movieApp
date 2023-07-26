package com.selincengiz.movieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.selincengiz.movieapp.data.model.Film
import com.selincengiz.movieapp.data.source.Database
import com.selincengiz.movieapp.databinding.ItemFilmBinding

class FilmAdapter(
    private val onFilmClicked: (Film) -> Unit,
    private val onCheckedChange: () -> Unit
) : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {
    private val filmList = mutableListOf<Film>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FilmViewHolder(
            ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: FilmAdapter.FilmViewHolder, position: Int) =
        holder.bind(filmList[position])

    override fun getItemCount() = filmList.size

    inner class FilmViewHolder(private val binding: ItemFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(film: Film) {
            with(binding) {
                checkBox.isChecked = film.isWatched
                imageView.setImageBitmap(film.image)

                checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    var updatedFilm = Film(
                        id = film.id,
                        title = film.title,
                        description = film.description,
                        isWatched = isChecked,
                        image = film.image,
                        date = film.date
                    )
                    Database.updateFilm(Database.getFilmIndex(film), updatedFilm)
                    Database.isWatched()
                    onCheckedChange()
                    notifyItemChanged(position)
                }

                imageView.setOnClickListener {
                    onFilmClicked(film)
                }
            }

        }

    }

    fun updateList(list: List<Film>) {
        filmList.clear()
        filmList.addAll(list)
        notifyItemRangeChanged(0, list.size)


    }
}