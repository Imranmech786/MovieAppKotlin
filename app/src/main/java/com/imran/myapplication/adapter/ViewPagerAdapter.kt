package com.imran.myapplication.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.imran.myapplication.fragment.FavouriteMovieFragment
import com.imran.myapplication.fragment.TopRatedMovieFragment

class ViewPagerAdapter(fm: FragmentManager,  val title_array: Array<String>) : FragmentPagerAdapter(fm) {

    override fun getItem(index: Int): Fragment {
        when (index) {
            0 -> return TopRatedMovieFragment()
            else -> return FavouriteMovieFragment()
        }
    }

    override fun getCount(): Int {
        return title_array.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title_array[position]
    }
}
