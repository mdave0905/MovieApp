package md.movieapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MovieSearchAdapter(private  val movieSearchList: ArrayList<MovieSearchModel>,private val context: Context,private val activity: Activity) : RecyclerView.Adapter<MovieSearchAdapter.MovieSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSearchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item,parent,false)
        return MovieSearchViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MovieSearchViewHolder, position: Int) {
        val model = movieSearchList[position]

        holder.title.text = model.title
        holder.rating.progress = (model.rating*10).toInt()
        holder.ratingTv.text = model.rating.toString()+" ⭐"
        Picasso.get().load(model.image).into(holder.image)

        holder.layout.setOnClickListener{
            val intent = Intent(context,MovieInfoActivity::class.java)
            intent.putExtra("movieId",model.id.toString())
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.image, "poster")
            context.startActivity(intent,options.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return  movieSearchList.size
    }

    class MovieSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val rating: ProgressBar = itemView.findViewById(R.id.rating)
        val ratingTv: TextView = itemView.findViewById(R.id.ratingTv)
        val layout: LinearLayout = itemView.findViewById(R.id.layout)
    }

}