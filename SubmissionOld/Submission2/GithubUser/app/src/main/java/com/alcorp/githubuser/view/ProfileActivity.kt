package com.alcorp.githubuser.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.githubuser.R
import com.alcorp.githubuser.adapter.PagerAdapter
import com.alcorp.githubuser.model.User
import com.alcorp.githubuser.viewModel.UserViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val EXTRA_USER = "user"
    }

    private lateinit var userViewModel: UserViewModel

    var name: String = "name"
    var company: String = "company"
    var location: String = "location"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setToolbar()

        refresh_profile.setOnRefreshListener(this)

        val user = intent.getParcelableExtra(EXTRA_USER) as User

        val sectionsPagerAdapter = PagerAdapter(this, supportFragmentManager, user.login)
        view_pager.adapter = sectionsPagerAdapter
        tabLayout.setupWithViewPager(view_pager)

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        if (checkNetwork() == true) {
            userViewModel.setUserDetail(user.login, resources.getString(R.string.value_place))
        } else {
            progBar_profile.visibility = View.GONE
            Toast.makeText(
                this@ProfileActivity,
                resources.getString(R.string.txt_toast_network),
                Toast.LENGTH_SHORT
            ).show()
        }

        userViewModel.getUserDetail().observe(this, Observer { usr ->
            if (usr != null) {
                userViewModel.setUserDetail(user.login, resources.getString(R.string.value_place))

                name = usr.get(0).name.toString()
                company = usr.get(0).company.toString()
                location = usr.get(0).location.toString()

                tvUsername.text = usr.get(0).login
                tvLocation.text = location
                tvName.text = name
                tvCompany.text = company
                tvRepository.text = usr.get(0).public_repos
                tvFollowers.text = usr.get(0).followers
                tvFollowing.text = usr.get(0).following

                Glide.with(this@ProfileActivity)
                    .load(usr.get(0).avatar_url)
                    .apply(RequestOptions().override(100, 100))
                    .into(profile_img)

                progBar_profile.visibility = View.GONE
            }
        })

    }

    @SuppressLint("RestrictedApi")
    private fun setToolbar() {
        supportActionBar?.title = resources.getString(R.string.txt_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0F
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.menu_search).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val user = intent.getParcelableExtra(EXTRA_USER) as User

        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.menu_share) {
            if (checkNetwork() == true) {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.putExtra(
                    Intent.EXTRA_TEXT,
                    "${resources.getString(R.string.txt_name)} : ${name}" +
                            "\n${resources.getString(R.string.txt_username)} : ${user.login}" +
                            "\n${resources.getString(R.string.txt_location)} : ${location}" +
                            "\n${resources.getString(R.string.txt_company)} : ${company}"
                )
                startActivity(Intent.createChooser(share, resources.getString(R.string.txt_share_profile)))
            } else {
                Toast.makeText(
                    this@ProfileActivity,
                    resources.getString(R.string.txt_toast_network),
                    Toast.LENGTH_SHORT
                ).show()
            }

        } else if (item.itemId == R.id.menu_setting) {
            startActivityForResult(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS), 0)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun checkNetwork(): Boolean {
        val cm: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network: NetworkInfo? = cm.activeNetworkInfo
        return network != null
    }

    override fun onRefresh() {
        val user = intent.getParcelableExtra(EXTRA_USER) as User
        if (checkNetwork() == true) {
            userViewModel.setUserDetail(user.login, resources.getString(R.string.value_place))

            val sectionsPagerAdapter = PagerAdapter(this, supportFragmentManager, user.login)
            view_pager.adapter = sectionsPagerAdapter
            tabLayout.setupWithViewPager(view_pager)
        } else {
            Toast.makeText(
                this@ProfileActivity,
                resources.getString(R.string.txt_toast_network),
                Toast.LENGTH_SHORT
            ).show()
        }
        refresh_profile.isRefreshing = false
    }

}
