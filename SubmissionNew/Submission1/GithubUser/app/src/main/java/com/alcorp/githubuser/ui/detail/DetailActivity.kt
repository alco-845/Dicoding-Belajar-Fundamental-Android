package com.alcorp.githubuser.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.githubuser.R
import com.alcorp.githubuser.data.remote.response.UserDetail
import com.alcorp.githubuser.databinding.ActivityDetailBinding
import com.alcorp.githubuser.utils.LoadingDialog
import com.alcorp.githubuser.utils.checkNetwork
import com.alcorp.githubuser.viewmodel.DetailViewModel
import com.alcorp.githubuser.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var loadingDialog: LoadingDialog
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    private lateinit var username: String
    private lateinit var userDetail: UserDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    @SuppressLint("RestrictedApi")
    private fun setToolbar() {
        supportActionBar?.title = resources.getString(R.string.txt_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0F
    }

    private fun init() {
        setToolbar()

        loadingDialog = LoadingDialog(this)
        loadingDialog.showDialog()
        binding.refreshDetail.setOnRefreshListener(this)
        username = intent.getStringExtra(EXTRA_DETAIL).toString()

        val sectionsPagerAdapter = PagerAdapter(this, supportFragmentManager, username)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        if (checkNetwork(this)){
            detailViewModel.getUserDetail(username)
            lifecycleScope.launch {
                delay(1000)

                withContext(Dispatchers.Main) {
                    detailViewModel.userDetailList.observe(this@DetailActivity) {
                        userDetail = it

                        binding.tvUsername.text = it.login
                        binding.tvLocation.text = it.location
                        binding.tvName.text = it.name
                        binding.tvCompany.text = it.company
                        binding.tvRepository.text = it.publicRepos.toString()
                        binding.tvFollowers.text = it.followers.toString()
                        binding.tvFollowing.text = it.following.toString()

                        Glide.with(this@DetailActivity)
                            .load(it.avatarUrl)
                            .apply(RequestOptions().override(100, 100))
                            .into(binding.imgProfile)
                    }

                    detailViewModel.errorMessage.observe(this@DetailActivity) {
                        if (it.isNotEmpty()) {
                            Toast.makeText(this@DetailActivity, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                loadingDialog.hideDialog()
            }
        } else {
            loadingDialog.hideDialog()
            Toast.makeText(this@DetailActivity, resources.getString(R.string.txt_toast_network), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.menuSearch).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.menuShare) {
            if (checkNetwork(this@DetailActivity)) {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.putExtra(
                    Intent.EXTRA_TEXT,
                    "${resources.getString(R.string.txt_name)} : ${userDetail.name}" +
                            "\n${resources.getString(R.string.txt_username)} : ${userDetail.login}" +
                            "\n${resources.getString(R.string.txt_location)} : ${userDetail.location}" +
                            "\n${resources.getString(R.string.txt_company)} : ${userDetail.company}"
                )
                startActivity(Intent.createChooser(share, resources.getString(R.string.txt_share_profile)))
            } else {
                Toast.makeText(this@DetailActivity, resources.getString(R.string.txt_toast_network), Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        init()
        binding.refreshDetail.isRefreshing = false
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        var getUsername: String? = null
    }
}