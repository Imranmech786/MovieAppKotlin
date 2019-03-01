package com.imran.myapplication.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.imran.myapplication.R
import com.imran.myapplication.model.Movie
import com.imran.myapplication.utils.Constants
import com.imran.myapplication.utils.StateData
import com.imran.myapplication.viewmodel.MovieDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.progressbar_layout.*
import javax.inject.Inject

class DetailActivity : BaseActivity(), View.OnClickListener {


    @Inject
    lateinit var movieDetailViewModel: MovieDetailViewModel

    override fun getResourceId(): Int {
        return R.layout.activity_detail; }

    private var movieId: Int = 0
    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar("")

        /*Observing Boolean Live Data for showing Progress Bar*/
        movieDetailViewModel.listMutableLiveData.observe(this, getObserver())

        val bundle = intent.extras
        if (bundle != null) {
            movieId = bundle.getInt("id")
            progress_bar.visibility = View.VISIBLE
            requestForMovieDetail()
        }
        fab.setOnClickListener(this)
        retry.setOnClickListener(this)
    }


    private fun getObserver(): Observer<StateData<Movie>> {
        return Observer { movieStateData ->
            if (movieStateData == null) return@Observer
            when (movieStateData.status) {
                StateData.DataStatus.SUCCESS -> {
                    setVisibility(View.GONE, View.VISIBLE)
                    movie = movieStateData.data
                    updateData(movie)
                }
                StateData.DataStatus.ERROR -> {
                    setVisibility(View.GONE, View.GONE)
                    if (movieStateData.error != null && movieStateData.error!!.message != null) {
                        val e = movieStateData.error
                        Toast.makeText(this@DetailActivity, e?.message, Toast.LENGTH_SHORT).show()
                    }
                }
                StateData.DataStatus.LOADING -> {
                    setVisibility(View.VISIBLE, View.GONE)
                }
                StateData.DataStatus.COMPLETE -> {
                    /*No Results Found*/
                    setVisibility(View.GONE, View.VISIBLE)
                    Toast.makeText(this@DetailActivity, "No Results Found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun setVisibility(progressVisibility: Int, mainVisibility: Int) {
        progress_bar.setVisibility(progressVisibility)
        main_layout.setVisibility(mainVisibility)
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
        /*Observing Boolean Live Data for Added to Favourites*/
        movieDetailViewModel.isAddedToFavourite.observe(this, getAddedToFavouritesObserver())

    }

    private fun getAddedToFavouritesObserver(): Observer<Boolean> {
        return Observer { isFav ->
            if (isFav != null) {
                val message = if (isFav) "Added to Favourites" else "Removed from Favourites"
                showSnackBar(message)
            }
        }
    }

    private fun showSnackBar(message: String) {
        val snackbar = Snackbar.make(main_layout, message, Snackbar.LENGTH_SHORT)
        val sbView = snackbar.view
        val textView = sbView.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }

    override fun onClick(v: View?) {
        when (v?.getId()) {
            R.id.fab -> movieDetailViewModel.handleFavourites(movie!!)
            R.id.retry -> requestForMovieDetail()
        }
    }

    private fun requestForMovieDetail() {
        if (com.imran.myapplication.utils.Network.isConnected(applicationContext)) {
            retry.visibility = View.GONE
            movieDetailViewModel.loadMovieDetail(movieId)
        } else {
            retry.visibility = View.VISIBLE
            Toast.makeText(this, getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show()
        }
    }

}