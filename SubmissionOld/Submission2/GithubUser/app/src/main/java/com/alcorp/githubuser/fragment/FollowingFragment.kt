package com.alcorp.githubuser.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alcorp.githubuser.R
import com.alcorp.githubuser.adapter.UserAdapter
import com.alcorp.githubuser.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_following.*

/**
 * A simple [Fragment] subclass.
 */
class FollowingFragment : Fragment() {

    companion object {
        var value_following: String? = null
    }

    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()

        rec_following.adapter = userAdapter
        rec_following.setHasFixedSize(true)
        rec_following.layoutManager = LinearLayoutManager(context)

        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        if (checkNetwork() == true){
            userViewModel.setFollowing(value_following.toString())
        } else {
            progBar_following.visibility = View.GONE
            Toast.makeText(context, resources.getString(R.string.txt_toast_network), Toast.LENGTH_SHORT).show()
        }
        userViewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
            if (user != null){
                userAdapter.setData(user)
                progBar_following.visibility = View.GONE
            }
        })

    }

    private fun checkNetwork(): Boolean{
        val cm: ConnectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network: NetworkInfo? = cm.activeNetworkInfo
        if (network != null){
            return true
        } else {
            return false
        }
    }

}
