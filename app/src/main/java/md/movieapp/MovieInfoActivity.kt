package md.movieapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MovieInfoActivity : AppCompatActivity() {

    //Declare Variables/Views
    private var movieGenreList = ArrayList<GenreModel>()
    private lateinit var image: ImageView
    private lateinit var movie: TextView
    private lateinit var info: TextView
    private lateinit var genreRv: RecyclerView
    private lateinit var synopsis: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_info)

        //Get the passed movieId from the MovieSearchAdapter activity
        val id = intent.getStringExtra("movieId")

        //Initialize XML Views
        image = findViewById(R.id.image)
        movie = findViewById(R.id.title)
        info = findViewById(R.id.info)
        genreRv = findViewById(R.id.genreRv)
        synopsis = findViewById(R.id.synopsis)
        genreRv = findViewById<RecyclerView>(R.id.genreRv)
        //Initialize GenreAdapter and set it to the RecyclerView
        genreRv.adapter = GenreAdapter(movieGenreList,this)

        //Back button initialization and onClick to go back to home page
        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener{
            onBackPressed()
        }
        getRequest(id.toString())

    }

    @SuppressLint("SimpleDateFormat")
    //Function to get the API Request
    private fun getRequest(id: String) {
            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)
            // Request URL with the movieId
            val url =
                "https://api.themoviedb.org/3/movie/$id?api_key=ac6ea778a429e4a5ed47176486b5184f"
            //Specifying the GET Request Method and get the JsonObject from the URL
            //Volley, a library, handles all HTTP Requests
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        //Adding the result for the selected movie into their respective views.
                        movie.text = response.getString("title")
                        Picasso.get().load("https://image.tmdb.org/t/p/w500"+response.getString("poster_path")).into(image)
                        val originalFormat = SimpleDateFormat("yyyy-mm-dd")
                        val finalFormat = SimpleDateFormat("dd MMM yyyy")
                        val date = originalFormat.parse(response.getString("release_date"))
                        val formattedDate = finalFormat.format(date)
                        info.text = formattedDate+"  •  "+response.getString("runtime")+" mins  •  "+response.getString("vote_average")+" ⭐"
                        synopsis.text = "Synopsis \n\n"+response.getString("overview")

                        //Getting the genres of the movie and adding them into a Recyclerview
                        val genreArray = response.getJSONArray("genres")
                        for (i in 0 until genreArray.length()){
                            val genre =genreArray.getJSONObject(i)
                            val genreModel = GenreModel(genre.getString("name"))
                            movieGenreList.add(genreModel,)
                        }
                        //If there are genres present, show the Recyclerview //Default is to keep it hidden
                        if(genreArray.length()>0){
                            genreRv.visibility = View.VISIBLE
                        }
                    }catch (e: Exception){

                    }
                    //Notify the adapter of the new changes to the arraylist
                    genreRv.adapter?.notifyDataSetChanged()

                },
                { error ->
                    Toast.makeText(this,error.message.toString(), Toast.LENGTH_SHORT).show()
                }
            )

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)

    }

}