package ca.georgiancollege.assignment01_kunwar.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.assignment01_kunwar.databinding.ActivityMainBinding
import ca.georgiancollege.assignment01_kunwar.viewmodel.MovieViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MovieViewModel by viewModels()
    private val apiKey = "1a7582ac"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView layout manager
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Search button click triggers movie search
        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchMovies(query, apiKey)
            }
        }

        // Observe movies LiveData and update RecyclerView adapter
        viewModel.movies.observe(this) { movieList ->
            binding.recyclerView.adapter = MovieAdapter(movieList)
        }
    }
}
