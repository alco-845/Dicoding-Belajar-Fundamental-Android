package com.alcorp.githubuser.ui.detail

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.alcorp.githubuser.R
import com.alcorp.githubuser.ui.detail.DetailActivity.Companion.getUsername

class PagerAdapter(private val context: Context, fm: FragmentManager, private val username: String?) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when(position){
            0 -> fragment =
                FollowersFragment().apply {
                    getUsername = username
                }
            1 ->fragment =
                FollowingFragment().apply {
                    getUsername = username
                }
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabTittle[position])
    }

    override fun getCount(): Int {
        return 2
    }

    companion object {
        private val tabTittle = intArrayOf(
            R.string.txt_followers,
            R.string.txt_following
        )
    }
}