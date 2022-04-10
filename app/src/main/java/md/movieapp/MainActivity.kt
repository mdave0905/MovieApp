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
        rv.adapter = MovieSearchAdapter(movieSearchList,this,this)
        rv.layoutManager = GridLayoutManager(this,2)

        //Listener for when search button on keyboard is pressed -> user has searched for a movie
        search.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                search.clearFocus()
                val input = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                input.hideSoftInputFromWindow(search.windowToken, 0)
                getRequest(search.text.toString())
                true
            } else {
                false
            }
        }

        clear.setOnClickListener{
            search.setText("")
            search.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT)
        }

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
    private fun getRequest(search: String) {
        if(search.trim().isNotEmpty()) {
            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)
            val url =
                "https://api.themoviedb.org/3/search/movie?api_key=ac6ea778a429e4a5ed47176486b5184f&query=" + search
            movieSearchList.clear()
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    try {
                        val resultArray = response.getJSONArray("results")
                        for (i in 0 until resultArray.length()){
                            val movie =resultArray.getJSONObject(i)
                            val model = MovieSearchModel(
                                "https://image.tmdb.org/t/p/w500"+movie.getString("poster_path"),
                                movie.getString("original_title"),
                                movie.getDouble("vote_average"),
                                movie.getLong("id")
                            )
                            movieSearchList.add(model)
                        }
                    }catch (e: Exception){

                    }
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