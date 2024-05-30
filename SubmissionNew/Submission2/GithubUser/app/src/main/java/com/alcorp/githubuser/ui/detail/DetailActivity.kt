package com.alcorp.githubuser.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.githubuser.R
import com.alcorp.githubuser.data.local.entity.FavoriteEntity
import com.alcorp.githubuser.data.remote.response.UserDetail
import com.alcorp.githubuser.databinding.ActivityDetailBinding
import com.alcorp.githubuser.utils.LoadingDialog
import com.alcorp.githubuser.utils.checkNetwork
import com.alcorp.githubuser.viewmodel.DetailViewModel
import com.alcorp.githubuser.viewmodel.FavoriteViewModel
import com.alcorp.githubuser.viewmodel.SettingViewModel
import com.alcorp.githubuser.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var loadingDialog: LoadingDialog

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this, dataStore)
    }

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(this, dataStore)
    }

    private val settingViewModel: SettingViewModel by viewModels {
        ViewModelFactory.getInstance(this, dataStore)
    }

    private lateinit var username: String
    private lateinit var userDetail: UserDetail
    private lateinit var userFav: String
    private lateinit var sharedPreferences: SharedPreferences

    private val tabTittle = arrayOf(
        "Followers",
        "Following"
    )

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
        checkTheme()

        loadingDialog = LoadingDialog(this)
        showLoading(true)

        binding.refreshDetail.setOnRefreshListener(this)
        binding.btnFav.setOnClickListener(this)
        username = intent.getStringExtra(EXTRA_DETAIL).toString()

        setTabLayout()
        setPreference()

        if (checkNetwork(this)){
            detailViewModel.getUserDetail(username)

            detailViewModel.userDetailList.observe(this) {
                userDetail = it

                binding.apply {
                    tvUsername.text = it.login
                    tvLocation.text = it.location
                    tvName.text = it.name
                    tvCompany.text = it.company
                    tvRepository.text = it.publicRepos.toString()
                    tvFollowers.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                }

                Glide.with(this)
                    .load(it.avatarUrl)
                    .apply(RequestOptions().override(100, 100))
                    .into(binding.imgProfile)

                detailViewModel.isLoading.observe(this) { status ->
                    showLoading(status)
                }
            }

            detailViewModel.errorMessage.observe(this) {
                if (it.isNotEmpty()) {
                    showLoading(false)
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            showLoading(false)
            Toast.makeText(this, resources.getString(R.string.txt_toast_network), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setTabLayout() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = PagerAdapter(supportFragmentManager, lifecycle, username)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTittle[position]
        }.attach()
    }

    private fun setPreference() {
        sharedPreferences = this.getSharedPreferences("fav", Context.MODE_PRIVATE)
        userFav = sharedPreferences.getString(username + "fav", "unfav").toString()

        if (userFav == "unfav"){
            Glide.with(this@DetailActivity)
                .load(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_border_24, null))
                .into(binding.btnFav)
        } else {
            Glide.with(this@DetailActivity)
                .load(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_24, null))
                .into(binding.btnFav)
        }
    }

    private fun checkTheme() {
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.menuSearch).isVisible = false
        menu.findItem(R.id.menuFav).isVisible = false
        menu.findItem(R.id.menuSetting).isVisible = false
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

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnFav -> {
                val favoriteEntity = FavoriteEntity(
                    0,
                    userDetail.login,
                    userDetail.avatarUrl
                )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                if (checkNetwork(this@DetailActivity)) {
                    if (userFav == "unfav"){
                        editor.putString(userDetail.login + "fav", "fav")

                        favoriteViewModel.insertFav(favoriteEntity)

                        Glide.with(this@DetailActivity)
                            .load(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_24, null))
                            .into(binding.btnFav)

                        Toast.makeText(this@DetailActivity, resources.getString(R.string.txt_toast_fav), Toast.LENGTH_SHORT).show()
                        reload()
                    } else {
                        editor.putString(userDetail.login + "fav", "unfav")

                        favoriteViewModel.deleteFav(userDetail.login!!)

                        Glide.with(this@DetailActivity)
                            .load(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_border_24, null))
                            .into(binding.btnFav)

                        Toast.makeText(this@DetailActivity, resources.getString(R.string.txt_toast_unfav), Toast.LENGTH_SHORT).show()
                        reload()
                    }
                } else {
                    Toast.makeText(this@DetailActivity, resources.getString(R.string.txt_toast_network), Toast.LENGTH_SHORT).show()
                }
                editor.apply()
            }
        }
    }

    override fun onRefresh() {
        reload()
        binding.refreshDetail.isRefreshing = false
    }

    private fun reload() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loadingDialog.showDialog() else loadingDialog.hideDialog()
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        var getUsername: String? = null
    }
}