package com.alcorp.githubuser.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.alcorp.githubuser.R
import com.alcorp.githubuser.fragment.FollowersFragment
import com.alcorp.githubuser.fragment.FollowersFragment.Companion.value_followers
import com.alcorp.githubuser.fragment.FollowingFragment
import com.alcorp.githubuser.fragment.FollowingFragment.Companion.value_following

class PagerAdapter(private val context: Context, fm: FragmentManager, val listUser: String?) :FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val TAB_TITLES = intArrayOf(
        R.string.txt_followers,
        R.string.txt_following
    )

    override fun getItem(position: Int): Fragment {
        var fragment:Fragment? = null

        when(position){
            0 -> fragment =
                FollowersFragment().apply {
                value_followers = listUser
            }
            1 ->fragment =
                FollowingFragment().apply {
                value_following = listUser
            }
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}