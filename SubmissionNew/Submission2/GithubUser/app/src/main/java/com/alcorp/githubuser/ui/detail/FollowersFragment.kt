package com.alcorp.githubuser.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alcorp.githubuser.R
import com.alcorp.githubuser.databinding.FragmentFollowersBinding
import com.alcorp.githubuser.ui.detail.DetailActivity.Companion.getUsername
import com.alcorp.githubuser.ui.main.UserAdapter
import com.alcorp.githubuser.utils.checkNetwork
import com.alcorp.githubuser.viewmodel.DetailViewModel

class FollowersFragment : Fragment() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: FragmentFollowersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkNetwork(context)){
            detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]
            detailViewModel.getUserFollowers(getUsername.toString())
            detailViewModel.followersList.observe(viewLifecycleOwner) {
                userAdapter = UserAdapter(requireContext(), it)

                binding.rvFollowers.adapter = userAdapter
                binding.rvFollowers.setHasFixedSize(true)
                binding.rvFollowers.layoutManager = LinearLayoutManager(context)
            }

            detailViewModel.isLoading.observe(requireActivity()) {
                binding.pbFollowers.visibility = if (it) View.VISIBLE else View.GONE
            }
        } else {
            binding.pbFollowers.visibility = View.GONE
            Toast.makeText(context, resources.getString(R.string.txt_toast_network), Toast.LENGTH_SHORT).show()
        }
    }
}