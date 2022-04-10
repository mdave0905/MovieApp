package md.movieapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class GenreAdapter(private  val genreList: ArrayList<GenreModel>,private val context: Context) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        //Attach the item layout to the ViewHolder
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.genre_item,parent,false)
        return GenreViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        //Add the details stored in each item of the arraylist (type -> GenreModel) to the respective view. This code will run for each item separately by accessing the 'position'

        val model = genreList[position]
        holder.genre.text = model.genre
    }

    override fun getItemCount(): Int {
        //Get the number of items in the list
        return  genreList.size
    }

    class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //From the item layout passed from OnCreateViewHolder (itemView object), declare the genre text view
        val genre: TextView = itemView.findViewById(R.id.genre)
    }

}