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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.genre_item,parent,false)
        return GenreViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {

        val model = genreList[position]
        holder.genre.text = model.genre
    }

    override fun getItemCount(): Int {
        return  genreList.size
    }

    class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val genre: TextView = itemView.findViewById(R.id.genre)
    }

}