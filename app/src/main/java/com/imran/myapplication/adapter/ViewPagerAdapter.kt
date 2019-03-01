/*
 * Copyright (c) 2018.
 *
 * This file is part of MovieDB.
 *
 * MovieDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MovieDB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MovieDB.  If not, see <http://www.gnu.org/licenses/>.
 */

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
