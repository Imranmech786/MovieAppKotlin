package com.imran.myapplication.fragment

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.imran.myapplication.R
import com.imran.myapplication.adapter.TopRatedMovieAdapter
import com.imran.myapplication.model.Movie
import com.imran.myapplication.navigator.BaseNavigator
import com.imran.myapplication.viewmodel.FavouriteMovieViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.favourite_movie_fragment.*
import javax.inject.Inject

class FavouriteMovieFragment : DaggerFragment(), BaseNavigator {


    private var mContext: Context? = null
    private var topRatedMovieAdapter: TopRatedMovieAdapter? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    @Inject
    lateinit var favouriteMovieViewModel: FavouriteMovieViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview.setLayoutManager(GridLayoutManager(mContext, 2))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.favourite_movie_fragment, container, false)
        favouriteMovieViewModel.navigator = (this)

        favouriteMovieViewModel.listMutableLiveData.observe(this,
            Observer<List<Movie>> { movieList ->
                if (topRatedMovieAdapter == null) {
                    topRatedMovieAdapter =
                        TopRatedMovieAdapter(
                            mContext,
                            movieList as ArrayList<Movie>?, favouriteMovieViewModel.navigator, true
                        )
                    recyclerview.setAdapter(topRatedMovieAdapter)
                } else {
                    topRatedMovieAdapter!!.updateFavourite(movieList as ArrayList<Movie>)
                }
            })

        /*Observing Boolean Live Data for Added to Favourites*/
        favouriteMovieViewModel.isAddedToFavourite.observe(this, getAddedToFavouritesObserver())

        return view
    }

    private fun getAddedToFavouritesObserver(): Observer<Boolean> {
        return Observer { isFav ->
            if (isFav != null) {
                favouriteMovieViewModel.getFavMovieList()
                val message = if (isFav) "Added to Favourites" else "Removed from Favourites"
                showSnackBar(message)

            }
        }
    }

    private fun showSnackBar(message: String) {
        val snackbar = Snackbar.make(root_view, message, Snackbar.LENGTH_SHORT)
        val sbView = snackbar.view
        val textView = sbView.findViewById(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
        snackbar.show()
    }

    override fun onItemClick(view: View, movie: Movie) {
        favouriteMovieViewModel.handleFavourites(movie)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            favouriteMovieViewModel.getFavMovieList()
        }
    }

}