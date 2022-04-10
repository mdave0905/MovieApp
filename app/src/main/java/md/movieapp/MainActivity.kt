package md.movieapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    //Initialize Variables
    private var movieSearchList = ArrayList<MovieSearchModel>()
    private lateinit var rv: RecyclerView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Initialize XML Views
        val search = findViewById<EditText>(R.id.search)
        val clear = findViewById<ImageButton>(R.id.clear)
        rv = findViewById<RecyclerView>(R.id.searchRv)

        //Setting RecyclerView Adapter
        rv.adapter = MovieSearchAdapter(movieSearchList,this,this)

        //Setting RecyclerView Layout Manager to Grid Layout Manager with Span Count 2
        rv.layoutManager = GridLayoutManager(this,2)

        //Listener for when search button on keyboard is pressed -> user has searched for a movie
        search.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                search.clearFocus()
                //Closes Keyboard
                val input = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                input.hideSoftInputFromWindow(search.windowToken, 0)
                getRequest(search.text.toString())
                true
            } else {
                false
            }
        }
        //Deletes text in the search box and opens keyboard
        clear.setOnClickListener{
            search.setText("")
            search.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT)
        }

        //Show delete text button when there is text in the search box and remove it when it is empty
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0?.isNotEmpty() == true){
                    clear.visibility = View.VISIBLE
                }else{
                    clear.visibility = View.INVISIBLE
                }
            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")

    //Function to get the API Request
    private fun getRequest(search: String) {
        if(search.trim().isNotEmpty()) {
            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)
            //Set the Request URL based on the user's search
            val url =
                "https://api.themoviedb.org/3/search/movie?api_key=ac6ea778a429e4a5ed47176486b5184f&query=" + search
            //Clear ArrayList
            movieSearchList.clear()
            //Specifying the GET Request Method and get the JsonObject from the URL
            //Volley, a library, handles all HTTP Requests
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    try {
                        //Get the results array that the URL returns
                        val resultArray = response.getJSONArray("results")
                        //Iterate through the array to get the list of movies
                        for (i in 0 until resultArray.length()){
                            //Create a model with all the details about the movie
                            val movie =resultArray.getJSONObject(i)
                            val model = MovieSearchModel(
                                "https://image.tmdb.org/t/p/w500"+movie.getString("poster_path"),
                                movie.getString("original_title"),
                                movie.getDouble("vote_average"),
                                movie.getLong("id")
                            )
                            //Add the model to the arraylist
                            movieSearchList.add(model)
                        }
                    }catch (e: Exception){

                    }
                    //Notify the adapter of the new changes to the arraylist
                    rv.adapter?.notifyDataSetChanged()
                },
                { error ->
                    Toast.makeText(this,error.message,Toast.LENGTH_SHORT).show()
                }
            )

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)
        }else{
            Toast.makeText(this, "Enter a movie name", Toast.LENGTH_SHORT).show()
        }
    }
}