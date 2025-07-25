package ca.georgiancollege.assignment01_kunwar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import ca.georgiancollege.assignment01_kunwar.model.Movie

class MovieViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun searchMovies(query: String, apiKey: String) {
        coroutineScope.launch {
            try {
                val url = URL("https://www.omdbapi.com/?apikey=$apiKey&s=${query.replace(" ", "+")}&type=movie")
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                if (connection.responseCode == 200) {
                    val response = connection.inputStream.bufferedReader().readText()
                    val json = JSONObject(response)

                    if (json.optString("Response") == "True") {
                        val searchArray = json.getJSONArray("Search")
                        val movieList = mutableListOf<Movie>()
                        for (i in 0 until searchArray.length()) {
                            val item = searchArray.getJSONObject(i)
                            movieList.add(
                                Movie(
                                    Title = item.optString("Title", "N/A"),
                                    Year = item.optString("Year", "N/A"),
                                    Rated = null,
                                    Released = null,
                                    Runtime = null,
                                    Genre = null,
                                    Director = null,
                                    Plot = null,
                                    Poster = item.optString("Poster", ""),
                                    imdbID = item.optString("imdbID", "")
                                )
                            )
                        }
                        _movies.postValue(movieList)
                    } else {
                        _movies.postValue(emptyList()) // No results found
                    }
                } else {
                    _movies.postValue(emptyList())
                }
                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                _movies.postValue(emptyList())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}
