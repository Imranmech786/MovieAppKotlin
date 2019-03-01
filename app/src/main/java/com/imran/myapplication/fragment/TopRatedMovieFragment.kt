package com.imran.myapplication.fragment

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.imran.myapplication.R
import com.imran.myapplication.activity.MainActivity
import com.imran.myapplication.adapter.TopRatedMovieAdapter
import com.imran.myapplication.model.Movie
import com.imran.myapplication.navigator.BaseNavigator
import com.imran.myapplication.navigator.MainActivityNavigator
import com.imran.myapplication.utils.Network
import com.imran.myapplication.utils.StateData
import com.imran.myapplication.viewmodel.TopRatedMovieViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.progressbar_layout.*
import kotlinx.android.synthetic.main.top_rated_fragment.*
import javax.inject.Inject

class TopRatedMovieFragment : DaggerFragment(), MainActivityNavigator, BaseNavigator {

    @Inject
    lateinit var mTopRatedMovieViewModel: TopRatedMovieViewModel

    var mPageNumber = 1
    var mIsSearch: Boolean = false
    var mQuery: String? = null
    var mContext: Context? = null
    var mTopRatedMovieAdapter: TopRatedMovieAdapter? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
        if (activity is MainActivity) {
            (activity as MainActivity).mainActivityModule.navigator = this
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gridLayoutManager = GridLayoutManager(activity, 2)
        recyclerview.layoutManager = gridLayoutManager
        setRecyclerViewScrollListener(gridLayoutManager)
        requestForTopRatedMovie()
        retry.setText(mContext?.getString(R.string.retry))
        retry.setOnClickListener { requestForTopRatedMovie() }

    }

    private fun requestForTopRatedMovie() {
        if (Network.isConnected(mContext?.getApplicationContext())) {
            retry.visibility = View.GONE
            mTopRatedMovieViewModel.loadTopRatedMovie(mPageNumber)
        } else {
            retry.visibility = View.VISIBLE
            Toast.makeText(mContext, mContext?.getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.top_rated_fragment, container, false)

        mTopRatedMovieViewModel.navigator = this

        /*Observing Movie List Live Data*/
        mTopRatedMovieViewModel.listMutableLiveData.observe(this,
            Observer { listStateData ->
                if (listStateData == null) return@Observer
                when (listStateData.status) {
                    StateData.DataStatus.SUCCESS -> {
                        setProgressBarVisibility(View.GONE)
                        val movieList = listStateData.data
                        if (mTopRatedMovieAdapter == null) {
                            mTopRatedMovieAdapter =
                                TopRatedMovieAdapter(
                                    mContext!!,
                                    movieList as ArrayList<Movie>?, mTopRatedMovieViewModel!!.navigator, false
                                )
                            recyclerview.setAdapter(mTopRatedMovieAdapter)
                        } else {
                            mTopRatedMovieAdapter!!.updateList(movieList!!)
                        }
                    }
                    StateData.DataStatus.ERROR -> {
                        setProgressBarVisibility(View.GONE)
                        if (listStateData.error != null && listStateData.error!!.message != null) {
                            val e = listStateData.error
                            Toast.makeText(mContext, e?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    StateData.DataStatus.LOADING -> setProgressBarVisibility(View.VISIBLE)
                    StateData.DataStatus.COMPLETE -> {
                        setProgressBarVisibility(View.GONE)
                        Toast.makeText(mContext?.getApplicationContext(), "No Results Found", Toast.LENGTH_SHORT).show()
                    }
                    StateData.DataStatus.CREATED -> TODO()
                }
            });

        /*Observing Boolean Live Data for showing Pagination Progress Bar*/
        mTopRatedMovieViewModel.isPaginationLoadingLiveData.observe(this, getPaginationLoadingObserver())

        /*Recyclerview on Scroll for loading more items via Pagination*/

        /*Observing Boolean Live Data for Added to Favourites*/
        mTopRatedMovieViewModel.isAddedToFavourite.observe(this, getAddedToFavouritesObserver())



        return view
    }

    fun getPaginationLoadingObserver(): Observer<Boolean> {
        return Observer { visibility -> pagination_progress_bar.setVisibility(if (visibility != null && visibility) View.VISIBLE else View.GONE) }
    }

    fun getAddedToFavouritesObserver(): Observer<Boolean> {
        return Observer { isFav ->
            if (isFav != null) {
                val message = if (isFav) "Added to Favourites" else "Removed from Favourites"
                showSnackBar(message)
            }
        }
    }

    fun setRecyclerViewScrollListener(gridLayoutManager: GridLayoutManager) {
        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val mVisibleItemCount = gridLayoutManager.childCount
                val mTotalItemCount = gridLayoutManager.itemCount
                val mPastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition()
                if (mVisibleItemCount + mPastVisibleItems >= mTotalItemCount) {
                    loadMore()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {}
        })
    }

    fun showSnackBar(message: String) {
        val snackbar = Snackbar.make(root_view, message, Snackbar.LENGTH_SHORT)
        val sbView = snackbar.view
        val textView = sbView.findViewById(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
        snackbar.show()
    }

    fun setProgressBarVisibility(visibility: Int) {
        progress_bar.setVisibility(visibility)
    }

    fun loadMore() {
        mPageNumber++
        if (mIsSearch) {
            mTopRatedMovieViewModel.getSearchMovie(mQuery!!, mPageNumber)
        } else {
            mTopRatedMovieViewModel.loadTopRatedMovie(mPageNumber)
        }

    }

    override fun onItemClick(view: View, movie: Movie) {
        mTopRatedMovieViewModel.handleFavourites(movie)
    }

    override fun topRatedMovie() {
        clearAdapterList(null, false)
        mTopRatedMovieViewModel.loadTopRatedMovie(mPageNumber)
    }

    override fun searchQuery(query: String) {
        clearAdapterList(query, true)
        mTopRatedMovieViewModel.getSearchMovie(query, mPageNumber)
    }

    fun clearAdapterList(query: String?, isSearch: Boolean) {
        mIsSearch = isSearch
        mQuery = query
        mPageNumber = 1
        if (mTopRatedMovieAdapter != null) {
            mTopRatedMovieAdapter!!.clear()
        }
    }
}