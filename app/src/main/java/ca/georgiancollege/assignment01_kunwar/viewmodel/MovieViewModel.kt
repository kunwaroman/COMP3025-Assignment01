package ca.georgiancollege.assignment01_kunwar.viewmodel

import androidx.lifecycle.*
import ca.georgiancollege.assignment01_kunwar.model.Movie
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MovieViewModel : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    fun searchMovies(query: String, apiKey: String) {
        thread {
            val url = URL("https://www.omdbapi.com/?s=$query&apikey=1a7582ac")
            val conn = url.openConnection() as HttpURLConnection
            conn.connect()
            val json = JSONObject(conn.inputStream.bufferedReader().readText())
            val searchArray = json.optJSONArray("Search")

            val result = mutableListOf<Movie>()
            for (i in 0 until (searchArray?.length() ?: 0)) {
                val item = searchArray.getJSONObject(i)
                result.add(
                    Movie(
                        Title = item.getString("Title"),
                        Year = item.getString("Year"),
                        Rated = null,
                        Released = null,
                        Runtime = null,
                        Genre = null,
                        Director = null,
                        Plot = null,
                        Poster = item.getString("Poster"),
                        imdbID = item.getString("imdbID")
                    )
                )
            }
            _movies.postValue(result)
        }
    }
}
