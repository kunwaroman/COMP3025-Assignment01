package ca.georgiancollege.assignment01_kunwar.model

data class Movie(
    val Title: String,
    val Year: String,
    val Rated: String? = null,
    val Released: String? = null,
    val Runtime: String? = null,
    val Genre: String? = null,
    val Director: String? = null,
    val Plot: String? = null,
    val Poster: String? = null,
    val imdbID: String
)
