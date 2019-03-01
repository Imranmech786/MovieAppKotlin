package com.imran.myapplication.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.imran.myapplication.R
import com.imran.myapplication.adapter.ViewPagerAdapter
import com.imran.myapplication.navigator.MainActivityNavigator
import com.imran.myapplication.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainActivityNavigator, SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener {

    @Inject
    lateinit var mainActivityModule: MainActivityViewModel

    override fun getResourceId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar("Movie App")
        mainActivityModule.navigator = this
        val title_array = arrayOf("Top Rated", "Favourites")
        container.setAdapter(ViewPagerAdapter(supportFragmentManager, title_array))
        tabs.setupWithViewPager(container)
        mainActivityModule.observable.subscribe(mainActivityModule.observer)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchManager = this@MainActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView?

        searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(this)
        searchItem.setOnActionExpandListener(this)
        searchView.clearFocus()

        searchView.setSearchableInfo(searchManager.getSearchableInfo(this@MainActivity.componentName))

        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        mainActivityModule.navigator!!.topRatedMovie()
        return true
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        mainActivityModule.query(p0!!)
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        mainActivityModule.query(p0!!)
        return false
    }

    override fun searchQuery(query: String) {
        mainActivityModule?.navigator!!.searchQuery(query)
    }

    override fun topRatedMovie() {
    }

}
