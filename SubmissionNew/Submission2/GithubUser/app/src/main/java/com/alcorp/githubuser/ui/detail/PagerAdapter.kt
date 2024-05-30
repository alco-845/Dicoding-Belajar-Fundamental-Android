package com.alcorp.githubuser.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alcorp.githubuser.ui.detail.DetailActivity.Companion.getUsername

class PagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, private val username: String?) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment {
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

    override fun getItemCount(): Int {
        return 2
    }
}