package com.imran.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.imran.myapplication.R
import com.imran.myapplication.activity.DetailActivity
import com.imran.myapplication.model.Movie
import com.imran.myapplication.navigator.BaseNavigator
import com.squareup.picasso.Picasso


class TopRatedMovieAdapter(
    val mContext: Context?,
    var mList: ArrayList<Movie>?,
    val mCallback: BaseNavigator?,
    val mIsMyFavourites: Boolean
) : RecyclerView.Adapter<TopRatedMovieAdapter.ViewHolder>() {

    fun clear() {
        mList?.clear()
        notifyDataSetChanged()
    }

    private val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {

        val movie = mList!![pos]
        Picasso.get().load(IMAGE_BASE_URL + movie.moviePoster)
            .placeholder(R.color.app_background)
            .into(holder.movie_image)

        holder.movie_image.setOnClickListener {
            val intent = Intent(mContext, DetailActivity::class.java)
            intent.putExtra("id", movie.movieId)
            mContext?.startActivity(intent)
        }

        if (movie.isFavourite && mIsMyFavourites) {
            holder.favourite_image.setImageDrawable(ContextCompat.getDrawable(mContext!!, R.drawable.ic_favorite))
        } else {
            holder.favourite_image.setImageDrawable(ContextCompat.getDrawable(mContext!!, R.drawable.ic_unfavorite))
        }

        holder.favourite_image.setOnClickListener { v ->
            movie.isFavourite = !movie.isFavourite
            notifyItemChanged(pos)
            if (mCallback != null) {
                mCallback.onItemClick(v, movie)
            }
        }

    }

    override fun getItemCount(): Int {
        return if (mList != null && !mList!!.isEmpty()) mList!!.size else 0
    }

    fun updateList(movieListResponse: List<Movie>) {
        val s = mList!!.size
        mList!!.addAll(movieListResponse)
        notifyItemRangeInserted(s, mList!!.size)
    }

    fun updateFavourite(movieListResponse: ArrayList<Movie>) {
        mList = movieListResponse
        notifyDataSetChanged()
    }


    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val movie_image: ImageView
        val favourite_image: ImageView

        init {
            movie_image = itemView.findViewById(R.id.movie_image)
            favourite_image = itemView.findViewById(R.id.favourite_image)
        }
    }
}
