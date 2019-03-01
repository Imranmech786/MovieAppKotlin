package com.imran.myapplication.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.imran.myapplication.R
import com.imran.myapplication.model.Movie
import com.imran.myapplication.utils.Constants
import com.imran.myapplication.utils.StateData
import com.imran.myapplication.viewmodel.MovieDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject

class DetailActivity : BaseActivity() {

    @Inject
    lateinit var movieDetailViewModel: MovieDetailViewModel

    override fun getResourceId(): Int {
        return R.layout.activity_detail; }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar("")

        /*Observing Boolean Live Data for showing Progress Bar*/
        movieDetailViewModel.listMutableLiveData.observe(this, getObserver())

        val bundle = intent.extras
        if (bundle != null) {
            val id = bundle.getInt("id")
            movieDetailViewModel.loadMovieDetail(id)
        }
    }

    private fun getObserver(): Observer<StateData<Movie>> {
        return Observer { movieStateData ->
            if (movieStateData == null) return@Observer
            when (movieStateData.status) {
                StateData.DataStatus.SUCCESS -> {
                    progress_bar.setVisibility(View.GONE)
                    main_layout.setVisibility(View.VISIBLE)
                    val movie = movieStateData.data
                    updateData(movie)
                }
                StateData.DataStatus.ERROR -> {
                    progress_bar.setVisibility(View.GONE)
                    main_layout.setVisibility(View.GONE)
                    if (movieStateData.error != null && movieStateData.error!!.message != null) {
                        val e = movieStateData.error
                        Toast.makeText(this@DetailActivity, e?.message, Toast.LENGTH_SHORT).show()
                    }
                }
                StateData.DataStatus.LOADING -> {
                    progress_bar.setVisibility(View.VISIBLE)
                    main_layout.setVisibility(View.GONE)
                }
                StateData.DataStatus.COMPLETE -> {
                    /*No Results Found*/
                    progress_bar.setVisibility(View.GONE)
                    main_layout.setVisibility(View.VISIBLE)
                    Toast.makeText(this@DetailActivity, "No Results Found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updateData(movie: Movie?) {
        movie_title.setText(movie?.movieTitle)
        release_date.setText(movie?.movieReleaseDate)
        movie_description.setText(movie?.movieOverview)
        rating_bar.setRating(movie?.voterAverage!!)
        Picasso.get().load(Constants.IMAGE_BASE_URL + movie.backdrop)
            .into(poster_image)
        Picasso.get().load(Constants.IMAGE_BASE_URL + movie.moviePoster)
            .into(movie_image)
    }
}