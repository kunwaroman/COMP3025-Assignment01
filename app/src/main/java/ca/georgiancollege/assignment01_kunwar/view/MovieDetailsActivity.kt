package ca.georgiancollege.assignment01_kunwar.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.assignment01_kunwar.R
import ca.georgiancollege.assignment01_kunwar.databinding.ActivityMovieDetailsBinding
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private val apiKey = "1a7582ac"

    // Handler to post updates on the main/UI thread
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imdbID = intent.getStringExtra("imdbID")
        if (imdbID == null) {
            Toast.makeText(this, "No movie ID provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchMovieDetails(imdbID)

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun fetchMovieDetails(imdbID: String) {
        thread {
            try {
                val url = URL("https://www.omdbapi.com/?i=$imdbID&apikey=$apiKey")
                val conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = 5000
                conn.readTimeout = 5000
                conn.connect()

                if (conn.responseCode == 200) {
                    val jsonText = conn.inputStream.bufferedReader().readText()
                    val json = JSONObject(jsonText)

                    val responseOk = json.optString("Response") == "True"
                    if (!responseOk) {
                        mainHandler.post {
                            Toast.makeText(this, "Movie details not found", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        conn.disconnect()
                        return@thread
                    }

                    val title = json.optString("Title", "N/A")
                    val year = json.optString("Year", "N/A")
                    val rated = json.optString("Rated", "N/A")
                    val plot = json.optString("Plot", "No description available")
                    val posterUrl = json.optString("Poster", "")

                    mainHandler.post {
                        binding.textViewTitle.text = title
                        binding.textViewYear.text = year
                        binding.textViewRating.text = rated
                        binding.textViewDescription.text = plot
                    }

                    if (posterUrl.isNotEmpty() && posterUrl != "N/A") {
                        try {
                            val inputStream = URL(posterUrl).openStream()
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            mainHandler.post {
                                binding.imageViewPoster.setImageBitmap(bitmap)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            mainHandler.post {
                                binding.imageViewPoster.setImageResource(R.drawable.placeholder_image)
                            }
                        }
                    } else {
                        mainHandler.post {
                            binding.imageViewPoster.setImageResource(R.drawable.placeholder_image)
                        }
                    }
                } else {
                    mainHandler.post {
                        Toast.makeText(this, "Failed to load movie details", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                conn.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                mainHandler.post {
                    Toast.makeText(this, "Failed to load movie details", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
