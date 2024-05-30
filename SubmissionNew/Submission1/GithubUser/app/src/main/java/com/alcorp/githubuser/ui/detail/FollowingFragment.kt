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
import com.alcorp.githubuser.databinding.FragmentFollowingBinding
import com.alcorp.githubuser.ui.main.UserAdapter
import com.alcorp.githubuser.utils.checkNetwork
import com.alcorp.githubuser.viewmodel.DetailViewModel

class FollowingFragment : Fragment() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var detailViewModel: DetailViewModel

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkNetwork(context)){
            detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]
            detailViewModel.getUserFollowing(DetailActivity.getUsername.toString())
            detailViewModel.followingList.observe(viewLifecycleOwner) {
                userAdapter = UserAdapter(requireContext(), it)

                binding.rvFollowing.adapter = userAdapter
                binding.rvFollowing.setHasFixedSize(true)
                binding.rvFollowing.layoutManager = LinearLayoutManager(context)
                binding.pbFollowing.visibility = View.GONE
            }
        } else {
            binding.pbFollowing.visibility = View.GONE
            Toast.makeText(context, resources.getString(R.string.txt_toast_network), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}