package ca.georgiancollege.assignment01_kunwar.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.assignment01_kunwar.databinding.ItemMovieBinding
import ca.georgiancollege.assignment01_kunwar.model.Movie
import ca.georgiancollege.assignment01_kunwar.view.MovieDetailsActivity

class MovieAdapter(private val movieList: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.binding.titleText.text = movie.Title
        holder.binding.yearText.text = movie.Year
        holder.binding.ratingText.text = "Tap for details"
        holder.binding.directorText.text = "IMDb ID: ${movie.imdbID}"

        // Hide poster image in list (you can enable if you want)
        holder.binding.imageViewPoster.visibility = android.view.View.GONE

        // Navigate to details screen on click
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra("imdbID", movie.imdbID)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = movieList.size
}
