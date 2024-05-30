package com.alcorp.githubuser.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.githubuser.R
import com.alcorp.githubuser.databinding.ActivityFavoriteBinding
import com.alcorp.githubuser.viewmodel.FavoriteViewModel
import com.alcorp.githubuser.viewmodel.SettingViewModel
import com.alcorp.githubuser.viewmodel.ViewModelFactory

class FavoriteActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityFavoriteBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(this, dataStore)
    }

    private val settingViewModel: SettingViewModel by viewModels {
        ViewModelFactory.getInstance(this, dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setToolbar()
        checkTheme()
        binding.refreshFav.setOnRefreshListener(this)

        favoriteViewModel.getFav().observe(this) {
            val favoriteAdapter = FavoriteAdapter(this, it)
            binding.rvFav.setHasFixedSize(true)
            binding.rvFav.layoutManager = LinearLayoutManager(this)
            binding.rvFav.adapter = favoriteAdapter
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

    override fun onRefresh() {
        favoriteViewModel.getFav()
        binding.refreshFav.isRefreshing = false
    }

    private fun setToolbar() {
        supportActionBar?.title = resources.getString(R.string.txt_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.menuShare).isVisible = false
        menu.findItem(R.id.menuFav).isVisible = false
        menu.findItem(R.id.menuSearch).isVisible = false
        menu.findItem(R.id.menuSetting).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}